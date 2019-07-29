package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.ParameterizedTypeName;

public class QueryHandler {

    public QueryHandler() {
    }

    public QueryHandler(String name, ParameterizedTypeName typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    private String name;

    private ParameterizedTypeName typeName;

    public ParameterizedTypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(ParameterizedTypeName typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
