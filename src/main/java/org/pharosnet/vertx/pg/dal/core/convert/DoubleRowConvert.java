package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class DoubleRowConvert implements RowConvert<Double> {

    private static final DoubleRowConvert convert = new DoubleRowConvert();

    public static DoubleRowConvert convert() {
        return convert;
    }

    public Double convert(Row row) {
       return row.getDouble(0);
    }

}
