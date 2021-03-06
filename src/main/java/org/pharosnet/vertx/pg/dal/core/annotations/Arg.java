package org.pharosnet.vertx.pg.dal.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER})
public @interface Arg {

    int[] value() default {};
    ArgKind kind() default ArgKind.NORMAL;

}
