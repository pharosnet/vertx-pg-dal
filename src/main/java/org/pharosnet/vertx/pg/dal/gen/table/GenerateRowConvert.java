package org.pharosnet.vertx.pg.dal.gen.table;

import com.squareup.javapoet.*;
import io.vertx.sqlclient.Row;
import org.pharosnet.vertx.pg.dal.gen.commons.StringUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class GenerateRowConvert {

    static void generate(Filer filer, TableGenerator tableGenerator) throws Exception {
        String pkg = tableGenerator.getPkg();
        String className = tableGenerator.getName();
        String newClassName = String.format("%sConvert", className);


        // static
        ClassName newClass = ClassName.get(pkg, newClassName);

        FieldSpec.Builder staticField = FieldSpec.builder(newClass, "convert", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("new " + newClassName + "()");

        MethodSpec.Builder staticFunc = MethodSpec.methodBuilder("convert")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(newClass)
                .addStatement("return convert");


        // convert
        MethodSpec.Builder convertFunc = MethodSpec.methodBuilder("convert")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(Row.class), "row")
                .returns(TypeName.get(tableGenerator.getTypeMirror()))
                .addStatement("$T obj = new $T()", TypeName.get(tableGenerator.getTypeMirror()), TypeName.get(tableGenerator.getTypeMirror()));


        for (RowField rowField : tableGenerator.getIds()) {
            buildRowFieldCode(convertFunc, rowField);
        }

        buildRowFieldCode(convertFunc, tableGenerator.getCreateBY());
        buildRowFieldCode(convertFunc, tableGenerator.getCreateAT());
        buildRowFieldCode(convertFunc, tableGenerator.getModifyBy());
        buildRowFieldCode(convertFunc, tableGenerator.getModifyAt());
        buildRowFieldCode(convertFunc, tableGenerator.getDeleteBy());
        buildRowFieldCode(convertFunc, tableGenerator.getDeleteAt());
        buildRowFieldCode(convertFunc, tableGenerator.getVersion());

        for (RowField rowField : tableGenerator.getColumns()) {
            buildRowFieldCode(convertFunc, rowField);
        }


        convertFunc.addStatement("return obj");

        // type
        TypeSpec typeBuilder = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(staticField.build())
                .addMethod(staticFunc.build())
                .addMethod(convertFunc.build())
                .build();

        // file
        JavaFile javaFile = JavaFile.builder(pkg, typeBuilder)
                .addFileComment("Generated code from Vertx Pg DAL. Do not modify!")
                .build();

        // write
        javaFile.writeTo(filer);

    }

    private static void buildRowFieldCode(MethodSpec.Builder methodBuilder, RowField rowField) {
        if (rowField == null) {
            return;
        }
        String typeString = rowField.getType().toString();
        if (typeString.equals("java.lang.String")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getString(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.lang.Integer")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getInteger(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.lang.Float")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getFloat(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.lang.Double")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getDouble(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.lang.Long")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getLong(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.math.BigDecimal")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("java.lang.Boolean")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getBoolean(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.lang.Short")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.getShort(" + "\"" + rowField.getColumn() + "\"" + ")" + ")");
        } else if (typeString.equals("java.util.UUID")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("java.time.OffsetDateTime")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("java.time.OffsetTime")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("java.time.LocalDateTime")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("java.time.LocalDate")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "row.get$T(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("io.vertx.core.json.JsonObject")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "($T) row.getValue(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        } else if (typeString.equals("io.vertx.core.json.JsonArray")) {
            methodBuilder.addStatement("obj.set" + StringUtils.toUpperCaseFirstOne(rowField.getName()) + "(" + "($T) row.getValue(" + "\"" + rowField.getColumn() + "\"" + ")" + ")", TypeName.get(rowField.getType()));
        }
    }
}
