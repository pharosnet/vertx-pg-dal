package org.pharosnet.vertx.pg.dal;


import org.junit.Test;
import org.pharosnet.vertx.pg.dal.core.commons.Case;

public class CaseTest {


    @Test
    public void snake() {

        String s = "CREATE_AT";

        System.out.println(Case.SNAKE.parse(s));
        System.out.println(Case.CAMEL.parse(s));

    }

}
