package org.pharosnet.vertx.pg.dal.gen.dal;

import java.util.List;

public class QueryMethod {

    private String sql;
    private String name;

    private List<MethodParam> params;
    private List<QueryArg> args;
    private QueryHandler handler;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QueryArg> getArgs() {
        return args;
    }

    public void setArgs(List<QueryArg> args) {
        this.args = args;
    }

    public QueryHandler getHandler() {
        return handler;
    }

    public void setHandler(QueryHandler handler) {
        this.handler = handler;
    }

    public List<MethodParam> getParams() {
        return params;
    }

    public void setParams(List<MethodParam> params) {
        this.params = params;
    }
}
