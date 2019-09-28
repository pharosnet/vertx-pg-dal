package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.*;
import io.vertx.pgclient.PgPool;
import org.pharosnet.vertx.pg.dal.core.PostgresDAL;
import org.pharosnet.vertx.pg.dal.core.annotations.Arg;
import org.pharosnet.vertx.pg.dal.core.annotations.ArgKind;
import org.pharosnet.vertx.pg.dal.core.annotations.Query;
import org.pharosnet.vertx.pg.dal.gen.SourceGenerator;
import org.pharosnet.vertx.pg.dal.gen.commons.StringUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DalGenerator implements SourceGenerator {

    private QueryInterface queryInterface;

    @Override
    public DalGenerator load(Elements elementUtils, TypeElement typeElement) {
        this.queryInterface = new QueryInterface();
        String pkg = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        queryInterface.setPkg(pkg);
        String name = typeElement.getSimpleName().toString();
        queryInterface.setName(name + "Impl");
        queryInterface.setSuperClassName(ClassName.get(pkg, name));

        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Element element : elements) {
            if (!(element instanceof ExecutableElement)) {
                continue;
            }
            ExecutableElement funcElement = (ExecutableElement) element;
            Query query = funcElement.getAnnotation(Query.class);
            if (query == null) {
                continue;
            }
            String sql = query.value();
            QueryMethod queryMethod = new QueryMethod();
            queryMethod.setSql(sql);
            String funcName = funcElement.getSimpleName().toString();
            queryMethod.setName(funcName);
            // param
            List<? extends VariableElement> params = funcElement.getParameters();
            for (VariableElement variableElement : params) {
                String paramName = variableElement.getSimpleName().toString();
                // handler
                TypeName paramTypeName = TypeName.get(variableElement.asType());
                if (paramTypeName instanceof ParameterizedTypeName) {
                    ParameterizedTypeName parameterizedTypeName = ((ParameterizedTypeName) paramTypeName);
                    if (parameterizedTypeName.rawType.toString().equals("io.vertx.core.Handler")) {
                        queryMethod.setHandler(new QueryHandler(paramName, parameterizedTypeName));
                    }
                }
                Arg arg = variableElement.getAnnotation(Arg.class);
                if (arg == null) {
                    continue;
                }
                if (arg.kind() == ArgKind.PLACEHOLDER) {
                    queryMethod.getPlaceholders().add(new QueryArg(paramName, 0, paramTypeName));
                } else {
                    int[] pos = arg.value();
                    for (int p : pos) {
                        queryMethod.getArgs().add(new QueryArg(paramName, p, paramTypeName));
                    }
                }
                MethodParam methodParam = new MethodParam(paramName, paramTypeName);
                queryMethod.getParams().add(methodParam);

            }
            Collections.sort(queryMethod.getArgs());
            queryInterface.getMethods().add(queryMethod);
        }
        return this;
    }

    @Override
    public DalGenerator generate(Filer filer) throws Exception {

        List<FieldSpec.Builder> sqlFieldBuilders = new ArrayList<>();
        List<MethodSpec.Builder> methodBuilders = new ArrayList<>();
        for (QueryMethod queryMethod : queryInterface.getMethods()) {

            String sqlName = String.format("%sSQL", queryMethod.getName());
            // sql
            ClassName stringClassName = ClassName.get("java.lang", "String");
            FieldSpec.Builder staticSqlField = FieldSpec.builder(stringClassName, sqlName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("$S", queryMethod.getSql());
            sqlFieldBuilders.add(staticSqlField);

            // method
            MethodSpec.Builder buildMethod = MethodSpec.methodBuilder(queryMethod.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID);

            for (MethodParam methodParam : queryMethod.getParams()) {
                buildMethod.addParameter(methodParam.getTypeName(), methodParam.getName());
            }
            if (queryMethod.getHandler() == null) {
                throw new IllegalAccessException("miss handler param");
            }
            buildMethod.addParameter(queryMethod.getHandler().getTypeName(), queryMethod.getHandler().getName());
            // tuple
            String argsCode = "";
            if (!queryMethod.getParams().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (QueryArg queryArg : queryMethod.getArgs()) {
                    sb.append(", ").append(queryArg.getName());
                }
                argsCode = sb.substring(2);
            }
            if (argsCode.length() > 0) {
                ClassName tupleClassName = ClassName.get("io.vertx.sqlclient", "Tuple");
                buildMethod.addStatement(String.format("$T args = $T.of(%s)", argsCode), tupleClassName, tupleClassName);
            }

            // one or list
            QueryKind queryKind = new QueryKind();
            this.loadQueryKind(queryKind, queryMethod.getHandler().getTypeName());
            ClassName convertClassName = null;
            if (queryKind.getName().isBoxedPrimitive()) {
                convertClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core.convert", String.format("%sRowConvert", queryKind.getName().simpleName()));
            } else if (queryKind.getName().simpleName().equals("JsonObject")) {
                convertClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core.convert", "JsonObjectRowConvert");
            } else {
                convertClassName = ClassName.get(queryKind.getName().packageName(), queryKind.getName().simpleName() + "Convert");
            }

            // placeholders
            if (!queryMethod.getPlaceholders().isEmpty()) {
                for (QueryArg phArg : queryMethod.getPlaceholders()) {
                    buildMethod.addCode("String sql = \"\";\n");
                    String sbName = "sb" + StringUtils.toUpperCaseFirstOne(phArg.getName());
                    buildMethod.addStatement(String.format("StringBuilder %s = new StringBuilder()", sbName), ClassName.get(StringBuilder.class));
                    ParameterizedTypeName phArgTypeName = ((ParameterizedTypeName) phArg.getTypeName());
                    StringBuilder codeB = new StringBuilder();
                    if (phArgTypeName.typeArguments.get(0).toString().equals("java.lang.String")) {
                        codeB.append("for (String item : ").append(phArg.getName()).append(") {\n");
                        codeB.append("\t").append(sbName).append(".append(\", '\").append(item).append(\"'\");\n");
                    } else {
                        String argTypeName = ((ClassName)phArgTypeName.typeArguments.get(0)).simpleName();
                        codeB.append("for (").append(argTypeName).append(" item : ").append(phArg.getName()).append(") {\n");
                        codeB.append("\t").append(sbName).append(".append(\", \").append(item);\n");
                    }
                    codeB.append("}\n");
                    codeB.append("String ").append("sql").append(StringUtils.toUpperCaseFirstOne(phArg.getName())).append(" = ").append(sbName).append(".toString();\n");
                    codeB.append("if (").append("sql").append(StringUtils.toUpperCaseFirstOne(phArg.getName())).append(".length() > 2) {\n");
                    codeB.append("\t").append("sql").append(StringUtils.toUpperCaseFirstOne(phArg.getName())).append(" = ").append("sql").append(StringUtils.toUpperCaseFirstOne(phArg.getName())).append(".substring(2);\n");
                    codeB.append("}\n");

                    codeB.append("sql = ").append(sqlName).append(".replaceAll(\"%").append(phArg.getName()).append("\", ").append("sql").append(StringUtils.toUpperCaseFirstOne(phArg.getName())).append(");\n");

                    buildMethod.addCode(codeB.toString());
                }
                if (queryKind.getOne()) {
                    buildMethod.addStatement(String.format("this.queryOne(sql, args, $T.convert()::convert, %s)", queryMethod.getHandler().getName()), convertClassName);
                } else {
                    buildMethod.addStatement(String.format("this.query(sql, args, $T.convert()::convert, %s)", queryMethod.getHandler().getName()), convertClassName);
                }
            } else {
                if (queryKind.getOne()) {
                    buildMethod.addStatement(String.format("this.queryOne(%s, args, $T.convert()::convert, %s)", sqlName, queryMethod.getHandler().getName()), convertClassName);
                } else {
                    buildMethod.addStatement(String.format("this.query(%s, args, $T.convert()::convert, %s)", sqlName, queryMethod.getHandler().getName()), convertClassName);
                }
            }
            buildMethod.addStatement("return");
            methodBuilders.add(buildMethod);
        }

        if (sqlFieldBuilders.size() != methodBuilders.size() || methodBuilders.isEmpty()) {
            throw new IllegalAccessException("no code to be generated");
        }

        MethodSpec constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(PgPool.class, "client")
                .addStatement("super($N)", "client")
                .build();


        // type
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(this.queryInterface.getName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(PostgresDAL.class)
                .addSuperinterface(this.queryInterface.getSuperClassName())
                .addMethod(constructorBuilder);

        int size = sqlFieldBuilders.size();
        for (int i = 0; i < size; i++) {
            typeBuilder.addField(sqlFieldBuilders.get(i).build());
            typeBuilder.addMethod(methodBuilders.get(i).build());
        }

        // file
        JavaFile javaFile = JavaFile.builder(this.queryInterface.getPkg(), typeBuilder.build())
                .addFileComment("Generated code from Vertx Pg DAL. Do not modify!")
                .build();

        // write
        javaFile.writeTo(filer);
        return this;
    }

    private void loadQueryKind(QueryKind queryKind, ParameterizedTypeName typeName) {
        List<TypeName> typeArgNames = typeName.typeArguments;
        if (typeArgNames.isEmpty()) {
            return;
        }
        TypeName typeArgName = typeArgNames.get(0);
        if (typeName.rawType.toString().equals("java.util.stream.Stream")) {
            queryKind.setOne(false);
        }
        if (typeArgName instanceof ParameterizedTypeName) {
            this.loadQueryKind(queryKind, (ParameterizedTypeName) typeArgName);
        } else {
            queryKind.setName((ClassName) typeArgName);
        }
    }

}
