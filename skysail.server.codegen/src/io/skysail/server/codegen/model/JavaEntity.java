package io.skysail.server.codegen.model;

import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.processors.EntityProcessor;
import io.skysail.server.domain.core.Entity;

import javax.lang.model.element.Element;

import lombok.Getter;

public class JavaEntity extends Entity implements JavaModel {

    @Getter
    private String elementName;

    @Getter
    private String simpleName;

    @Getter
    private String packageName;

    private Element element;

    @Getter
    private String applicationName;

    @Getter
    private String applicationPackage;

    public JavaEntity(JavaApplication application, Element element) {
        super(element.toString());
        this.element = element;
        this.elementName = element.toString();
        this.simpleName = toSimpleName(element.toString());
        this.packageName = getPackageFromName(element.toString());
        this.applicationName = application.getId();
        this.applicationPackage = application.getPackageName();
    }

    @Override
    public String getTypeName() {
        return null;
    }

    public GenerateResources getGenerateResourcesAnnotation() {
        return element.getAnnotation(GenerateResources.class);
    }

    public String getClassAnnotations() {
        return EntityProcessor.GENERATED_ANNOTATION;
    }

}
