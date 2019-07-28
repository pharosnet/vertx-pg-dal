package org.pharosnet.vertx.pg.dal.gen.dal;

import org.pharosnet.vertx.pg.dal.gen.SourceGenerator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class DalGenerator implements SourceGenerator {

    @Override
    public DalGenerator load(Elements elementUtils, TypeElement type) {

        return this;
    }

    @Override
    public DalGenerator generate(Filer filer) throws Exception {

        return this;
    }
    
}
