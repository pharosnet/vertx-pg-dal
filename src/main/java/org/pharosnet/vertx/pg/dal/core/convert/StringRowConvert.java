package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.sqlclient.Row;

public class StringRowConvert implements RowConvert<String> {

    public String convert(Row row) {
       return row.getString(0);
    }

}
