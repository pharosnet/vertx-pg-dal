package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class FloatRowConvert implements RowConvert<Float> {

    private static final FloatRowConvert convert = new FloatRowConvert();

    public static FloatRowConvert convert() {
        return convert;
    }

    public Float convert(Row row) {
       return row.getFloat(0);
    }

}
