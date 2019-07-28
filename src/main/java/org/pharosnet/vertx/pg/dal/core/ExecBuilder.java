package org.pharosnet.vertx.pg.dal.core;

import io.vertx.sqlclient.Tuple;

import java.util.List;

public interface ExecBuilder<T> {

    String query();

    Tuple args(T row);

    List<Tuple> batchArgs(List<T> rows);

}
