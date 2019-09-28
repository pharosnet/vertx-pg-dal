package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class IntegerRowConvert implements RowConvert<Integer> {

    private static final IntegerRowConvert convert = new IntegerRowConvert();

    public static IntegerRowConvert convert() {
        return convert;
    }

    public Integer convert(Row row) {
       return row.getInteger(0);
    }

}
