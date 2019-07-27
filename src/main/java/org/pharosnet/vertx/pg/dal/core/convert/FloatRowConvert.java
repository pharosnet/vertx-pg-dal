package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class FloatRowConvert implements RowConvert<Float> {

    public Float convert(Row row) {
       return row.getFloat(0);
    }

}
