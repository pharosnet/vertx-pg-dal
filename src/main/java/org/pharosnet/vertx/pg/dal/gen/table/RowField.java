package org.pharosnet.vertx.pg.dal.gen.table;

import javax.lang.model.type.TypeMirror;

public class RowField {

    private String name;
    private String column;
    private TypeMirror type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }
}
