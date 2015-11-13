package io.skysail.server.codegen.model;

import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.processors.EntityProcessor;
import io.skysail.server.domain.core.Entity;

import javax.lang.model.element.Element;

import lombok.Getter;

public class JavaEntity extends Entity implements JavaModel {

    @Getter
    private String elementName;

    private Element element;

    @Getter
    private String applicationName;

    @Getter
    private String applicationPackage;

    public JavaEntity(JavaApplication application, Element element) {
        super(element.toString());
        this.element = element;
        this.elementName = element.toString();
        this.applicationName = application.getId();
        this.applicationPackage = application.getPackageName();
    }

    public GenerateResources getGenerateResourcesAnnotation() {
        return element.getAnnotation(GenerateResources.class);
    }

    public String getClassAnnotations() {
        return EntityProcessor.GENERATED_ANNOTATION;
    }

    public String getLinkedResources() {
        return getPostResourceClassName() + ".class";
    }

}
