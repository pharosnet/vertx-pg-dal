package org.pharosnet.vertx.pg.dal.gen;

import com.google.auto.service.AutoService;
import org.pharosnet.vertx.pg.dal.core.annotations.Column;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
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

                // 获取包名
                String packageName = elementUtils.getPackageOf(e).getQualifiedName().toString();
                // 根据旧Java类名创建新的Java文件
                String className = typeElement.getQualifiedName().toString().substring(packageName.length() + 1);

                System.out.println("xxxx packageName " + packageName);
                System.out.println("xxxx className " + className);
                System.out.println("xxxx " + e.toString());
                System.out.println("xxxx SimpleName " + e.getSimpleName());
                System.out.println("xxxx EnclosedElements" + e.getEnclosedElements());
                System.out.println("xxxx Kind" + e.getKind());

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
