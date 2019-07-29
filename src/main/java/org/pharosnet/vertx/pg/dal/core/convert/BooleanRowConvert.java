package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class BooleanRowConvert implements RowConvert<Boolean> {

    public Boolean convert(Row row) {
       return row.getBoolean(0);
    }

}
