package org.pharosnet.vertx.pg.dal.core.convert;

public class RowConverts {

    public static BooleanRowConvert booleanRowConvert() {
        return new BooleanRowConvert();
    }

    public static DoubleRowConvert doubleRowConvert() {
        return new DoubleRowConvert();
    }

    public static FloatRowConvert floatRowConvert() {
        return new FloatRowConvert();
    }

    public static IntegerRowConvert integerRowConvert() {
        return new IntegerRowConvert();
    }

    public static JsonObjectRowConvert jsonObjectRowConvert() {
        return new JsonObjectRowConvert();
    }

    public static LongRowConvert longRowConvert() {
        return new LongRowConvert();
    }

    public static StringRowConvert stringRowConvert() {
        return new StringRowConvert();
    }

}
