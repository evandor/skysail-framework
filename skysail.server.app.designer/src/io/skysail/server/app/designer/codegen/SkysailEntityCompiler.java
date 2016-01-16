package io.skysail.server.app.designer.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.RouteModel;
import lombok.Getter;

public class SkysailEntityCompiler extends SkysailCompiler {

    private static final String BUILD_PATH_SOURCE = "src-gen";

    private static final String ENTITY_IDENTIFIER = "entity";

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    private List<RouteModel> routes = new ArrayList<>();

    public SkysailEntityCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        super(applicationModel, stGroup, compiler);
        this.stGroupDir = stGroup;
    }

    public CompiledCode createEntity(DesignerEntityModel entityModel) {
        ST template = getStringTemplateIndex("javafile");
        CompiledCode compiledCode = setupEntityForCompilation(template, entityModel);
        entityClassName = compiledCode.getClassName();
        entityModel.setClassName(entityClassName);
        return compiledCode;
    }

    public void createResources(DesignerEntityModel entityModel) {
        
        
        if (entityModel.isAggregate()) {
            ST template = getStringTemplateIndex("entityResource");
            entityResourceClassName = setupEntityResourceForCompilation(template, entityModel);
            routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}", entityResourceClassName));
        } else {
            ST template = getStringTemplateIndex("entityResourceNonAggregate");
            entityResourceClassName = setupEntityResourceForCompilation(template, entityModel);
            routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}", entityResourceClassName));
        }

        if (entityModel.isAggregate()) {
            ST postResourceTemplate = getStringTemplateIndex("postResource");
            String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, applicationModel,
                entityModel);
            routes.add(new RouteModel("/" + entityModel.getId() + "s/", postResourceClassName));
        } else {
            ST postResourceTemplate = getStringTemplateIndex("postResourceNonAggregate");
//            DesignerEntityModel parentEntityModel = entityModel.getReferencedBy().get(); // NOSONAR
//            routes.add(new RouteModel("/" + parentEntityModel.getId() + "/{id}/" + entityModel.getId() + "s/", postResourceClassName));
            String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, applicationModel,
                    entityModel);
                routes.add(new RouteModel("/" + entityModel.getId() + "s/", postResourceClassName));
        }
        ST putResourceTemplate = getStringTemplateIndex("putResource");
        String putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}/", putResourceClassName));

        String listResourceClassName;
        if (entityModel.isAggregate()) {
            ST listResourceTemplate = getStringTemplateIndex("listResource");
            String collectionLinks = entityModel.getApplicationModel().getRootEntities().stream().map(e -> "," + e.getSimpleName() + "sResource.class").collect(Collectors.joining());
            listResourceTemplate.add("listLinks", "       return super.getLinks(Post"+entityModel.getSimpleName()+"Resource.class"+ collectionLinks+");");
            listResourceClassName = setupListResourceForCompilation(listResourceTemplate, applicationModel,
                entityModel);
            routes.add(new RouteModel("/" + entityModel.getId() + "s", listResourceClassName));
        } else {
            ST listResourceTemplate = getStringTemplateIndex("listResourceNonAggregate");
            listResourceClassName = setupListResourceForCompilation(listResourceTemplate, applicationModel,
                entityModel);
            routes.add(new RouteModel("/" + entityModel.getId() + "s", listResourceClassName));
        }
        
        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private CompiledCode setupEntityForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityName = entityModel.getId();
        return collect(entityName, entityCode, BUILD_PATH_SOURCE);
    }

    private String setupEntityResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        List<String> linkedClasses = new ArrayList<>();
        linkedClasses.add("Put" + entityModel.getSimpleName() + "Resource.class");
        entityModel.getRelations().stream().forEach(relation -> {
            String targetName = relation.getTargetEntityModel().getSimpleName();
            linkedClasses.add("Post"+targetName+"Resource.class");
            linkedClasses.add(targetName+"sResource.class");
        });

        entityModel.getReferences().forEach(r -> linkedClasses.add("Post" + r.getReferencedEntityName() + "Resource.class"));

        String getLinksCode = "return super.getLinks(" + linkedClasses.stream().collect(Collectors.joining(",")) + ");";
        template.add("links", getLinksCode);
        String entityCode = template.render();
        String entityName = entityModel.getId() + "Resource";
        collect(entityName, entityCode, BUILD_PATH_SOURCE);
        return entityName;
    }

    private String setupPostResourceForCompilation(ST template, DesignerApplicationModel applicationModel,
            DesignerEntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "Resource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);

        StringBuilder addEntityCode;
        addEntityCode = new StringBuilder("Subject subject = SecurityUtils.getSubject();\n");
        addEntityCode.append(entityModel.getActionFields().stream().map(actionField -> {
            return actionField.getCode("postEntity#addEntity").replace("$Methodname$", withFirstCapital(actionField.getName()));
        }).collect(Collectors.joining("\n")));
        if (entityModel.isAggregate()) {
            addEntityCode.append("String id = app.getRepository("+entityModel.getId()+".class).save(entity, app.getApplicationModel()).toString();\n");
            addEntityCode.append("entity.setId(id);\n");
        } else {
//            DesignerEntityModel parent = entityModel.getReferencedBy().get();
//            addEntityCode.append(parent.getId() + " root = app.getRepository().getById("+parent.getId()+".class, getAttribute(\"id\"));\n");
//            addEntityCode.append("root.add"+entityModel.getId()+"(entity);\n");
//            addEntityCode.append("app.getRepository().update(getAttribute(\"id\"), root);\n");
        }
        template.add("addEntity", addEntityCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode, BUILD_PATH_SOURCE);
        return entityClassName;
    }

    private String setupPutResourceForCompilation(ST template, DesignerApplicationModel applicationModel,
            DesignerEntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getSimpleName() + "Resource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String updateEntityCode = entityModel.getId() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";
        template.add("updateEntity", updateEntityCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode, BUILD_PATH_SOURCE);
        return entityClassName;
    }

    private String setupListResourceForCompilation(ST template, DesignerApplicationModel applicationModel,
            DesignerEntityModel entityModel) {
        final String simpleClassName = entityModel.getSimpleName() + "sResource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode, BUILD_PATH_SOURCE);
        return entityClassName;
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }
    
    private CharSequence withFirstCapital(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }


}
