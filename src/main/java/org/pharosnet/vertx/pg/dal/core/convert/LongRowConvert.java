package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class LongRowConvert implements RowConvert<Long> {

    public Long convert(Row row) {
       return row.getLong(0);
    }

}
