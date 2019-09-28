package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class StringRowConvert implements RowConvert<String> {

    private static final StringRowConvert convert = new StringRowConvert();

    public static StringRowConvert convert() {
        return convert;
    }

    public String convert(Row row) {
       return row.getString(0);
    }

}
