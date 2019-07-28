package org.pharosnet.vertx.pg.dal.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Table {

    String schema() default "";
    String name();
    TableKind kind() default TableKind.TABLE;

}
