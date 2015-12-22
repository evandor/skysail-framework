package io.skysail.server.app.designer.codegen;

import java.util.*;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.*;
import lombok.Getter;

public class SkysailEntityCompiler extends SkysailCompiler {

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    private List<RouteModel> routes = new ArrayList<>();

    public SkysailEntityCompiler(CodegenApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        super(applicationModel, stGroup, compiler);
        this.stGroupDir = stGroup;
    }

    public void createEntity(CodegenEntityModel entityModel) {
        ST template = getStringTemplateIndex("javafile");
        entityClassName = setupEntityForCompilation(template, applicationModel, entityModel);
        entityModel.setClassName(entityClassName);
    }

    public void createResources(CodegenEntityModel entityModel) {
        ST template = getStringTemplateIndex("entityResource");
        entityResourceClassName = setupEntityResourceForCompilation(template, applicationModel, entityModel);

        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}", entityResourceClassName));
        
        ST postResourceTemplate = getStringTemplateIndex("postResource");
        String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, applicationModel,
                entityModel);
        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("/" + entityModel.getId() + "s/", postResourceClassName));
        } else {
            CodegenEntityModel parentEntityModel = entityModel.getReferencedBy().get();
            routes.add(new RouteModel("/" + parentEntityModel.getId() + "/{id}/" + entityModel.getId() + "s/", postResourceClassName));
        }
        ST putResourceTemplate = getStringTemplateIndex("putResource");
        String putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}/", putResourceClassName));

        ST listResourceTemplate = getStringTemplateIndex("listResource");
        String listResourceClassName = setupListResourceForCompilation(listResourceTemplate, applicationModel,
                entityModel);
        routes.add(new RouteModel("/" + entityModel.getId() + "s", listResourceClassName));

        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private String setupEntityForCompilation(ST template, CodegenApplicationModel applicationModel, CodegenEntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = entityModel.getId();
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupEntityResourceForCompilation(ST template, CodegenApplicationModel applicationModel,
            CodegenEntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        List<String> linkedClasses = new ArrayList<>();
        linkedClasses.add("Put" + entityModel.getSimpleName() + "Resource.class");

        entityModel.getReferences().forEach(r -> {
            linkedClasses.add("Post" + r.getReferencedEntityName() + "Resource.class");
        });

        String getLinksCode = "return super.getLinks(" + linkedClasses.stream().collect(Collectors.joining(",")) + ");";
        template.add("links", getLinksCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getId() + "Resource";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPostResourceForCompilation(ST template, CodegenApplicationModel applicationModel,
            CodegenEntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);

        StringBuilder addEntityCode;
        addEntityCode = new StringBuilder("Subject subject = SecurityUtils.getSubject();\n");
        addEntityCode.append(entityModel.getActionFields().stream().map(actionField -> {
            return actionField.getCode("postEntity#addEntity").replace("$Methodname$", withFirstCapital(actionField.getName()));
        }).collect(Collectors.joining("\n")));
        if (entityModel.isAggregate()) {
            addEntityCode.append("String id = app.getRepository("+entityModel.getId()+".class).save(entity, app.getApplicationModel()).toString();\n");
            addEntityCode.append("entity.setId(id);\n");
        } else {
            CodegenEntityModel parent = entityModel.getReferencedBy().get();
            addEntityCode.append(parent.getId() + " root = app.getRepository().getById("+parent.getId()+".class, getAttribute(\"id\"));\n");
            addEntityCode.append("root.add"+entityModel.getId()+"(entity);\n");
            addEntityCode.append("app.getRepository().update(getAttribute(\"id\"), root);\n");
        }
        template.add("addEntity", addEntityCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPutResourceForCompilation(ST template, CodegenApplicationModel applicationModel,
            CodegenEntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getSimpleName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);
        String updateEntityCode = entityModel.getId() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";
        template.add("updateEntity", updateEntityCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupListResourceForCompilation(ST template, CodegenApplicationModel applicationModel,
            CodegenEntityModel entityModel) {
        final String simpleClassName = entityModel.getSimpleName() + "sResource";
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }
    
    private CharSequence withFirstCapital(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }


}
