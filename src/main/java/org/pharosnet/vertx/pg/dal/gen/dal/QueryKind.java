package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.ClassName;

public class QueryKind {

    public QueryKind() {
        this.one = true;
    }

    private Boolean one;
    private ClassName name;

    public Boolean getOne() {
        return one;
    }

    public void setOne(Boolean one) {
        this.one = one;
    }

    public ClassName getName() {
        return name;
    }

    public void setName(ClassName name) {
        this.name = name;
    }
}
