package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class BooleanRowConvert implements RowConvert<Boolean> {

    private static final BooleanRowConvert convert = new BooleanRowConvert();

    public static BooleanRowConvert convert() {
        return convert;
    }

    public Boolean convert(Row row) {
       return row.getBoolean(0);
    }

}
