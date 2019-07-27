package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class DoubleRowConvert implements RowConvert<Double> {

    public Double convert(Row row) {
       return row.getDouble(0);
    }

}
