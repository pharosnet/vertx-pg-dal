package org.pharosnet.vertx.pg.dal.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_PARAMETER})
public @interface Arg {

    // positions
    int[] value();

}
