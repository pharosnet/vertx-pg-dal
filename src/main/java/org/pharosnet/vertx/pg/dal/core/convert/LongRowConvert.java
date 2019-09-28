package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class LongRowConvert implements RowConvert<Long> {

    private static final LongRowConvert convert = new LongRowConvert();

    public static LongRowConvert convert() {
        return convert;
    }

    public Long convert(Row row) {
       return row.getLong(0);
    }

}
