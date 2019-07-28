package org.pharosnet.vertx.pg.dal.core;

import io.vertx.sqlclient.Tuple;

import java.util.List;

public interface ExecBatchBuilder<T> {

    String query();

    List<Tuple> args();

    ExecBuilder build(List<T> rows);

}
