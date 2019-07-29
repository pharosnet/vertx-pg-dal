package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.TypeName;

public class QueryArg implements Comparable<QueryArg> {

    public QueryArg() {
    }

    public QueryArg(String name, int pos, TypeName typeName) {
        this.name = name;
        this.pos = pos;
        this.typeName = typeName;
    }

    private String name;
    private int pos;

    private TypeName typeName;

    @Override
    public int compareTo(QueryArg o) {
        if (this.pos < o.pos) {
            return -1;
        }
        return 0;
    }

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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
