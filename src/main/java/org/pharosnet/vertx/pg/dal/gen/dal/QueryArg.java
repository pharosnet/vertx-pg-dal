package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.TypeName;

public class QueryArg {


    private String name;
    private int[] pos;

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

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
}
