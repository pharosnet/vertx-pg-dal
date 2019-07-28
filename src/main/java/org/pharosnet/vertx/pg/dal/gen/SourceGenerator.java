package org.pharosnet.vertx.pg.dal.gen;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public interface SourceGenerator {

    SourceGenerator load(Elements elementUtils, TypeElement type);

    SourceGenerator generate(Filer filer) throws Exception;

}
