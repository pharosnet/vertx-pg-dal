package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class IntegerRowConvert implements RowConvert<Integer> {

    public Integer convert(Row row) {
       return row.getInteger(0);
    }

}
