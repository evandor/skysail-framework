package io.skysail.server.codegen.apt.processors;

import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.stringtemplate.MySTGroupFile;
import io.skysail.server.codegen.model.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.STGroup;

@SupportedAnnotationTypes("io.skysail.server.codegen.annotations.GenerateResources")
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
@Slf4j
public class EntityProcessor extends Processors {

    public static final String GENERATED_ANNOTATION = "@Generated(\"" + EntityProcessor.class.getName() + "\")";

    @Override
    public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        Set<? extends Element> generateResourceElements = roundEnv.getElementsAnnotatedWith(GenerateResources.class);
        String applicationName = getOneAndOnlyApplicationName(generateResourceElements);
        JavaApplication application = new JavaApplication(applicationName);
        analyse(application, roundEnv, generateResourceElements);
        application.getEntities().stream().forEach(entity -> {
            try {
                createRepository((JavaEntity) entity);
                createEntityResource(roundEnv, (JavaEntity) entity);
                createListResource(roundEnv, (JavaEntity) entity);
                createPostResource(roundEnv, (JavaEntity) entity);
                createPutResource(roundEnv, (JavaEntity) entity);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        return true;
    }

    private String getOneAndOnlyApplicationName(Set<? extends Element> elements) {
        if (elements == null || elements.size() == 0) {
            throw new IllegalStateException("no elements found for code generation");
        }
        Set<String> result = new HashSet<>();
        elements.stream().forEach(element -> {
            result.add(element.getAnnotation(GenerateResources.class).application());
        });
        if (result.size() != 1) {
            throw new IllegalStateException("two many (" + result.size() + ") applications defined");
        }
        return result.iterator().next();
    }

    private void analyse(JavaApplication application, RoundEnvironment roundEnv,
            Set<? extends Element> generateResourceElements) {

        printHeadline("Analysing project for code generation");
        for (Element entityElement : generateResourceElements) {
            printMessage("adding entity: " + entityElement.toString());
            application.add(new JavaEntity(application, entityElement));
        }
        printMessage("");
    }

    private void createRepository(JavaEntity entity) throws IOException {
        JavaFileObject jfo = createSourceFile(entity.getId() + "Repo");
        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile("repository/Repository.stg", '$', '$');
        writer.append(group.getInstanceOf("repository").add("entity", entity).render());
        writer.close();
    }

    private void createEntityResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        if (methodExcluded(entity.getGenerateResourcesAnnotation(), ResourceType.GET)) {
            return;
        }

        JavaFileObject jfo = createSourceFile(entity.getId() + "Resource");
        Writer writer = jfo.openWriter();
        URL url = getURL("entityResource/EntityResource.stg");
        printMessage("URL " + url.toString());
        STGroup group = new MySTGroupFile("entityResource/EntityResource.stg", '$', '$');
        writer.append(group.getInstanceOf("entity").add("entity", entity).render());
        writer.close();
    }

    private void createListResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        GenerateResources annotation = entity.getGenerateResourcesAnnotation();
        if (methodExcluded(annotation, ResourceType.LIST)) {
            return;
        }

        JavaFileObject jfo = createSourceFile(entity.getId() + "sResource");
        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile("listResource/ListResource.stg", '$', '$');
        writer.append(group.getInstanceOf("list").add("entity", entity).render());
        writer.close();
    }

    private void createPostResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        GenerateResources annotation = entity.getGenerateResourcesAnnotation();
        if (methodExcluded(annotation, ResourceType.POST)) {
            return;
        }

        String typeName = entity.getPackageName() + ".Post" + entity.getSimpleName() + "Resource";
        JavaFileObject jfo = createSourceFile(typeName);
        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile("postResource/PostResource2.stg", '$', '$');
        writer.append(group.getInstanceOf("post").add("entity", entity).render());
        writer.close();
    }

    private void createPutResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        GenerateResources annotation = entity.getGenerateResourcesAnnotation();
        if (methodExcluded(annotation, ResourceType.PUT)) {
            return;
        }

        String typeName = entity.getPackageName() + ".Put" + entity.getSimpleName() + "Resource";
        JavaFileObject jfo = createSourceFile(typeName);
        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile("putResource/PutResource.stg", '$', '$');
        writer.append(group.getInstanceOf("put").add("entity", entity).render());
        writer.close();
    }

    private URL getURL(String fileName) {
        URL url;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(fileName);
        if (url == null) {
            cl = this.getClass().getClassLoader();
            url = cl.getResource(fileName);
        }
        return url;
    }

    private void printHeadline(String msg) {
        printMessage("");
        printMessage(msg);
        printMessage(StringUtils.repeat("-", msg.length()));
        printMessage("");
    }

    private boolean methodExcluded(GenerateResources annotation, ResourceType resourceType) {
        return Arrays.asList(annotation.exclude()).contains(resourceType);
    }

}
