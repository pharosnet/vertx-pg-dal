package org.pharosnet.vertx.pg.dal.gen;

import com.google.auto.service.AutoService;
import org.pharosnet.vertx.pg.dal.core.annotations.Column;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SupportedAnnotationTypes({"org.pharosnet.vertx.pg.dal.core.annotations.Table"})
@SupportedOptions({"codegen.output"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class PgCodeGenProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
       Map<String, String> options = processingEnv.getOptions();

//        System.out.println(options);

        for (TypeElement typeElement : annotations) {

            for (Element e : roundEnv.getElementsAnnotatedWith(typeElement)) {
                try {
                    Class clazz = Class.forName(e.toString());
                    System.out.println("class " + clazz);
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        System.out.println(field.getName());
                    }
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
                System.out.println("xxxx " + e.toString());
                System.out.println("xxxx " + e.getSimpleName());
                System.out.println("xxxx " + e.getEnclosedElements());
                System.out.println(e.getKind());

                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.toString());
                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.getSimpleName());
                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.getEnclosedElements().toString());

               List<? extends Element> elements = e.getEnclosedElements();
                for (Element ee : elements) {
                    System.out.println(ee.getKind() + " " + ee.getSimpleName());
                    if (ee.getKind().isField()) {
                        Column column =  ee.getAnnotation(Column.class);
                        System.out.println(column.name());
                    }
                }


            }


        }

        return true;
    }
}
