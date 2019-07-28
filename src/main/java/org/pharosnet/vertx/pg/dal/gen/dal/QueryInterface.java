package org.pharosnet.vertx.pg.dal.gen.dal;

import com.squareup.javapoet.TypeName;

import java.util.List;

public class QueryInterface {

    private String name;
    private String pkg;
    private TypeName typeName;

    private List<QueryMethod> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public List<QueryMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<QueryMethod> methods) {
        this.methods = methods;
    }
}
