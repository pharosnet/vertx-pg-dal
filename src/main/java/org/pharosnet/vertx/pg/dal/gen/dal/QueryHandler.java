package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.TypeName;

public class QueryHandler {

    private String name;

    private TypeName typeName;

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
