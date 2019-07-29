package org.pharosnet.vertx.pg.dal.core;

import io.vertx.sqlclient.Tuple;

import java.util.List;

public interface ExecBatchBuilder<T> {

    String query();

    List<Tuple> args();

    ExecBatchBuilder build(List<T> rows);

}
