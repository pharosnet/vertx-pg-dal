package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

@FunctionalInterface
public interface RowConvert<R> {

    R convert(Row row);

}
