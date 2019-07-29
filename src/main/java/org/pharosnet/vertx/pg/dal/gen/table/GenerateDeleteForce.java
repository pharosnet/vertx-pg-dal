package org.pharosnet.vertx.pg.dal.gen.table;

import com.squareup.javapoet.*;
import org.pharosnet.vertx.pg.dal.gen.commons.StringUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class GenerateDeleteForce {

    static void generate(Filer filer, TableGenerator tableGenerator) throws Exception {
        if (tableGenerator.getIds().isEmpty()) {
            return;
        }
        String pkg = tableGenerator.getPkg();
        String className = tableGenerator.getName();
        String newClassName = String.format("%sDeleteForceBuilder", className);
        TypeName rowType = TypeName.get(tableGenerator.getTypeMirror());


        StringBuilder buildTuples = new StringBuilder();

        // sql
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        if (tableGenerator.getSchemaName().length() > 0) {
            sb.append("\"").append(tableGenerator.getSchemaName()).append("\".");
        }
        sb.append("\"").append(tableGenerator.getTableName()).append("\" WHERE ");
        StringBuilder ids = new StringBuilder();
        for (RowField rowField : tableGenerator.getIds()) {
            ids.append("AND ").append("\"").append(rowField.getColumn()).append("\" = ? ");
            buildTuples.append(", ");
            buildTuples.append("row.get").append(StringUtils.toUpperCaseFirstOne(rowField.getName())).append("()");

        }
        String idCond = ids.toString().substring(4);
        sb.append(idCond);
        if (tableGenerator.getVersion() != null) {
            sb.append("AND ").append("\"").append(tableGenerator.getVersion().getColumn()).append("\" = ? ");
            buildTuples.append(", ");
            buildTuples.append("row.get").append(StringUtils.toUpperCaseFirstOne(tableGenerator.getVersion().getName())).append("()");
        }

        String sql = sb.toString();

        ClassName stringClassName = ClassName.get("java.lang", "String");
        FieldSpec.Builder staticSqlField = FieldSpec.builder(stringClassName, "sql", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer("$S", sql);

        //args
        ClassName tupleClassName = ClassName.get("io.vertx.sqlclient", "Tuple");
        FieldSpec.Builder argField = FieldSpec.builder(tupleClassName, "args", Modifier.PRIVATE);

        // query()
        MethodSpec.Builder queryMethod = MethodSpec.methodBuilder("query")
                .addModifiers(Modifier.PUBLIC)
                .returns(stringClassName)
                .addStatement("return sql");

        //args()
        MethodSpec.Builder argsMethod = MethodSpec.methodBuilder("args")
                .addModifiers(Modifier.PUBLIC)
                .returns(tupleClassName)
                .addStatement("return args");

        // build()
        ClassName rowClassName = ClassName.get(pkg, className);
        ClassName buildClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core", "ExecBuilder");

        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(buildClassName)
                .addParameter(rowClassName, "row")
                .addStatement(String.format("this.args = $T.of(%s)", buildTuples.toString().substring(2)), tupleClassName)
                .addStatement("return this");

        ClassName superClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core", "ExecBuilder");

        TypeName superClass = ParameterizedTypeName.get(superClassName, rowType);

        // type
        TypeSpec typeBuilder = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superClass)
                .addField(staticSqlField.build())
                .addField(argField.build())
                .addMethod(queryMethod.build())
                .addMethod(argsMethod.build())
                .addMethod(buildMethod.build())
                .build();

        // file
        JavaFile javaFile = JavaFile.builder(pkg, typeBuilder)
                .addFileComment("Generated code from Vertx Pg DAL. Do not modify!")
                .build();

        // write
        javaFile.writeTo(filer);
    }

    static void generateBatch(Filer filer, TableGenerator tableGenerator) throws Exception {
        if (tableGenerator.getIds().isEmpty()) {
            return;
        }
        String pkg = tableGenerator.getPkg();
        String className = tableGenerator.getName();
        String newClassName = String.format("%sDeleteForceBatchBuilder", className);
        StringBuilder buildTuples = new StringBuilder();

        TypeName rowType = TypeName.get(tableGenerator.getTypeMirror());
        // sql
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        if (tableGenerator.getSchemaName().length() > 0) {
            sb.append("\"").append(tableGenerator.getSchemaName()).append("\".");
        }
        sb.append("\"").append(tableGenerator.getTableName()).append("\" WHERE ");
        StringBuilder ids = new StringBuilder();
        for (RowField rowField : tableGenerator.getIds()) {
            ids.append("AND ").append("\"").append(rowField.getColumn()).append("\" = ? ");
            buildTuples.append(", ");
            buildTuples.append("row.get").append(StringUtils.toUpperCaseFirstOne(rowField.getName())).append("()");

        }
        String idCond = ids.toString().substring(4);
        sb.append(idCond);
        if (tableGenerator.getVersion() != null) {
            sb.append("AND ").append("\"").append(tableGenerator.getVersion().getColumn()).append("\" = ? ");
            buildTuples.append(", ");
            buildTuples.append("row.get").append(StringUtils.toUpperCaseFirstOne(tableGenerator.getVersion().getName())).append("()");
        }

        String sql = sb.toString();

        ClassName stringClassName = ClassName.get("java.lang", "String");
        FieldSpec.Builder staticSqlField = FieldSpec.builder(stringClassName, "sql", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer("$S", sql);

        //args
        ClassName tupleClassName = ClassName.get("io.vertx.sqlclient", "Tuple");

        ClassName listClassName = ClassName.get("java.util", "List");
        TypeName listOfTuple = ParameterizedTypeName.get(listClassName, tupleClassName);
        FieldSpec.Builder argField = FieldSpec.builder(listOfTuple, "args", Modifier.PRIVATE);

        // query()
        MethodSpec.Builder queryMethod = MethodSpec.methodBuilder("query")
                .addModifiers(Modifier.PUBLIC)
                .returns(stringClassName)
                .addStatement("return sql");

        //args()
        MethodSpec.Builder argsMethod = MethodSpec.methodBuilder("args")
                .addModifiers(Modifier.PUBLIC)
                .returns(listOfTuple)
                .addStatement("return args");

        // build()
        ClassName buildClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core", "ExecBatchBuilder");
        TypeName buildOfRowClassName = ParameterizedTypeName.get(buildClassName, rowType);
        TypeName listOfRowClassName = ParameterizedTypeName.get(listClassName, rowType);
        ClassName collectorsClassName = ClassName.get("java.util.stream", "Collectors");

        // this.args = list.stream().map(row -> Tuple.of(row.getDeleteBY(), row.getDeleteAT(), row.getId(), row.getVersion())).collect(Collectors.toList());
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(buildOfRowClassName)
                .addParameter(listOfRowClassName, "rows")
                .addStatement(String.format("this.args = rows.stream().map(row -> $T.of(%s)).collect($T.toList())", buildTuples.toString().substring(2)), tupleClassName, collectorsClassName)
                .addStatement("return this");

        ClassName superClassName = ClassName.get("org.pharosnet.vertx.pg.dal.core", "ExecBatchBuilder");

        TypeName superClass = ParameterizedTypeName.get(superClassName, rowType);
        // type
        TypeSpec typeBuilder = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superClass)
                .addField(staticSqlField.build())
                .addField(argField.build())
                .addMethod(queryMethod.build())
                .addMethod(argsMethod.build())
                .addMethod(buildMethod.build())
                .build();

        // file
        JavaFile javaFile = JavaFile.builder(pkg, typeBuilder)
                .addFileComment("Generated code from Vertx Pg DAL. Do not modify!")
                .build();

        // write
        javaFile.writeTo(filer);
    }
}
