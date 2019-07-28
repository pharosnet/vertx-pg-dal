package org.pharosnet.vertx.pg.dal.core.convert;

public class RowConverts {

    private static BooleanRowConvert booleanRowConvert = new BooleanRowConvert();
    private static DoubleRowConvert doubleRowConvert = new DoubleRowConvert();
    private static FloatRowConvert floatRowConvert = new FloatRowConvert();
    private static IntegerRowConvert integerRowConvert = new IntegerRowConvert();
    private static JsonObjectRowConvert jsonObjectRowConvert = new JsonObjectRowConvert();
    private static LongRowConvert longRowConvert = new LongRowConvert();
    private static StringRowConvert stringRowConvert = new StringRowConvert();

    public static BooleanRowConvert booleanRowConvert() {
        return booleanRowConvert;
    }

    public static DoubleRowConvert doubleRowConvert() {
        return doubleRowConvert;
    }

    public static FloatRowConvert floatRowConvert() {
        return floatRowConvert;
    }

    public static IntegerRowConvert integerRowConvert() {
        return integerRowConvert;
    }

    public static JsonObjectRowConvert jsonObjectRowConvert() {
        return jsonObjectRowConvert;
    }

    public static LongRowConvert longRowConvert() {
        return longRowConvert;
    }

    public static StringRowConvert stringRowConvert() {
        return stringRowConvert;
    }

}
