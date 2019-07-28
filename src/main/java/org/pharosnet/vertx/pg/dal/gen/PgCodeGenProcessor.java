package org.pharosnet.vertx.pg.dal.gen;

import com.google.auto.service.AutoService;
import org.pharosnet.vertx.pg.dal.core.annotations.Dal;
import org.pharosnet.vertx.pg.dal.core.annotations.Table;
import org.pharosnet.vertx.pg.dal.gen.dal.DalGenerator;
import org.pharosnet.vertx.pg.dal.gen.table.TableGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"org.pharosnet.vertx.pg.dal.core.annotations.Table", "org.pharosnet.vertx.pg.dal.core.annotations.Dal"})
//@SupportedAnnotationTypes({"org.pharosnet.vertx.pg.dal.core.annotations.Table"})
//@SupportedOptions({"codegen.output"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class PgCodeGenProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = processingEnv.getTypeUtils();
        this.elementUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        List<TableGenerator> tableGenerators = roundEnv.getElementsAnnotatedWith(Table.class)
                .stream()
                .map(e -> new TableGenerator().load(elementUtils, (TypeElement) e)).collect(Collectors.toList());
        for (TableGenerator generator : tableGenerators) {
            try {
                generator.generate(filer);
            } catch (Exception cause) {
                cause.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, cause.getMessage());
                return false;
            }
        }


        List<DalGenerator> dalGenerators =  roundEnv.getElementsAnnotatedWith(Dal.class)
                .stream()
                .map(e -> new DalGenerator().load(elementUtils, (TypeElement) e)).collect(Collectors.toList());
        for (DalGenerator generator : dalGenerators) {
            try {
                generator.generate(filer);
            } catch (Exception cause) {
                cause.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, cause.getMessage());
                return false;
            }
        }
        return true;
    }

}
