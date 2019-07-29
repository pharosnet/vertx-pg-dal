package org.pharosnet.vertx.pg.dal.core.convert;

import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class JsonObjectRowConvert implements RowConvert<JsonObject> {

    public JsonObject convert(Row row) {
        JsonObject jsonObject = new JsonObject();
        int size = row.size();
        for (int i = 0; i < size; i++) {
            String column = row.getColumnName(i);
            Object value = row.getValue(i);
            if (value instanceof OffsetDateTime) {
                OffsetDateTime v = (OffsetDateTime) value;
                jsonObject.put(column, v.toInstant());
            } else if (value instanceof OffsetTime) {
                OffsetTime v = (OffsetTime) value;
                jsonObject.put(column, v.format(DateTimeFormatter.ISO_OFFSET_TIME));
            } else if (value instanceof LocalDateTime) {
                LocalDateTime v = (LocalDateTime) value;
                jsonObject.put(column, ZonedDateTime.of(v, ZoneId.systemDefault()).toInstant());
            } else if (value instanceof LocalDate) {
                LocalDate v = (LocalDate) value;
                jsonObject.put(column, ZonedDateTime.of(v.atTime(0, 0, 0), ZoneId.systemDefault()).toInstant());
            } else {
                jsonObject.put(column, value);
            }
        }
        return jsonObject;
    }

}
