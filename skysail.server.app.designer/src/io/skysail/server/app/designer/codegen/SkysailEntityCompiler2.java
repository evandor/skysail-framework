package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.RouteModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import org.stringtemplate.v4.ST;

public class SkysailEntityCompiler2 extends SkysailCompiler2 {

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    private List<RouteModel> routes = new ArrayList<>();

    public SkysailEntityCompiler2(ApplicationModel applicationModel, STGroupBundleDir stGroup) {
        super(applicationModel, stGroup);
        this.stGroupDir = stGroup;
    }

    public void createEntity(EntityModel entityModel) {
        ST template = getStringTemplateIndex("javafile");
        entityClassName = setupEntityForCompilation(template, applicationModel, entityModel);
        entityModel.setClassName(entityClassName);
    }

    public void createResources(EntityModel entityModel) {
        ST template = getStringTemplateIndex("entityResource");
        entityResourceClassName = setupEntityResourceForCompilation(template, applicationModel, entityModel);

        ST postResourceTemplate = getStringTemplateIndex("postResource");
        String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, applicationModel,
                entityModel);
        if (entityModel.isRootEntity()) {
            routes.add(new RouteModel("/" + entityModel.getEntityName() + "s/", postResourceClassName));
        } else {
            EntityModel parentEntityModel = entityModel.getReferencedBy().get();
            routes.add(new RouteModel("/" + parentEntityModel.getEntityName() + "/{id}/" + entityModel.getEntityName() + "s/", postResourceClassName));
        }
        ST putResourceTemplate = getStringTemplateIndex("putResource");
        String putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getEntityName() + "s/{id}", putResourceClassName));

        ST listResourceTemplate = getStringTemplateIndex("listResource");
        String listResourceClassName = setupListResourceForCompilation(listResourceTemplate, applicationModel,
                entityModel);
        routes.add(new RouteModel("/" + entityModel.getEntityName() + "s", listResourceClassName));

        if (entityModel.isRootEntity()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private String setupEntityForCompilation(ST template, ApplicationModel applicationModel, EntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName();
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupEntityResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        List<String> linkedClasses = new ArrayList<>();
        linkedClasses.add("Put" + entityModel.getEntityName() + "Resource.class");

        entityModel.getReferences().forEach(r -> {
            linkedClasses.add("Post" + r.getReferencedEntityName() + "Resource.class");
        });

        String getLinksCode = "return super.getLinks(" + linkedClasses.stream().collect(Collectors.joining(",")) + ");";
        template.add("links", getLinksCode);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName() + "Resource";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPostResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getEntityName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);

        StringBuilder addEntityCode;
        addEntityCode = new StringBuilder("Subject subject = SecurityUtils.getSubject();\n");
        addEntityCode.append(entityModel.getActionFields().stream().map(actionField -> {
            return actionField.getCode("postEntity#addEntity").replace("$Methodname$", actionField.getName());
        }).collect(Collectors.joining("\n")));
        if (entityModel.isRootEntity()) {
            addEntityCode.append("String id = app.getRepository().add(entity).toString();\n");
            addEntityCode.append("entity.setId(id);\n");
        } else {
            EntityModel parent = entityModel.getReferencedBy().get();
            addEntityCode.append(parent.getEntityName() + " root = app.getRepository().getById("+parent.getEntityName()+".class, getAttribute(\"id\"));\n");
            addEntityCode.append("root.add"+entityModel.getEntityName()+"(entity);\n");
            addEntityCode.append("app.getRepository().update(getAttribute(\"id\"), root);\n");
        }
        addEntityCode.append("return new SkysailResponse<String>();\n");
        template.add("addEntity", addEntityCode);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPutResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getEntityName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);
        String updateEntityCode = entityModel.getEntityName() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";
        template.add("updateEntity", updateEntityCode);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupListResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = entityModel.getEntityName() + "sResource";
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }

}
