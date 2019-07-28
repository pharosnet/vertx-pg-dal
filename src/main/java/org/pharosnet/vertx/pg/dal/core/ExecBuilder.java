package org.pharosnet.vertx.pg.dal.core;

import io.vertx.sqlclient.Tuple;

public interface ExecBuilder<T> {


    String query();

    Tuple args();

    ExecBuilder build(T row);

}
