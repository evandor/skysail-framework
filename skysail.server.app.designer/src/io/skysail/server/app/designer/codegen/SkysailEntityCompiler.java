package io.skysail.server.app.designer.codegen;

import java.util.*;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.DesignerFieldModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.Getter;

public class SkysailEntityCompiler extends SkysailCompiler {

    private static final String BUILD_PATH_SOURCE = "src-gen";
    private static final String ENTITY_IDENTIFIER = "entity";

    private List<RouteModel> routes = new ArrayList<>();

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;
    private TemplateProvider templateProvider;

    public SkysailEntityCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.stGroupDir = stGroup;
        this.templateProvider = templateProvider;
    }

    public CompiledCode createEntity(DesignerEntityModel entityModel) {
        ST template = templateProvider.templateFor("javafile");
        CompiledCode compiledCode = setupEntityForCompilation(template, entityModel);
        entityClassName = compiledCode.getClassName();
        entityModel.setClassName(entityClassName);
        return compiledCode;
    }

    public Map<String, CompiledCode> createResources(DesignerEntityModel entityModel) {
        Map<String, CompiledCode> codes = new HashMap<>();
        createEntityResource(entityModel, codes);
        createPostResource(entityModel, codes);
        createPutResource(entityModel, codes);
        createListResource(entityModel, codes);
        
        entityModel.getRelations().stream().forEach(relation -> createRelationResources(entityModel, relation, codes));
        
        return codes;
    }

    private void createEntityResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        CompiledCode compiledCode;
        ST template;
        if (entityModel.isAggregate()) {
            template = templateProvider.templateFor("entityResource");
        } else {
            template = templateProvider.templateFor("entityResourceNonAggregate");
        }
        compiledCode = setupEntityResourceForCompilation(template, entityModel);
        entityResourceClassName = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}", entityResourceClassName));
        codes.put(entityResourceClassName, compiledCode);
    }
    
    private void createPostResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        CompiledCode compiledCode;
        ST postResourceTemplate;
        if (entityModel.isAggregate()) {
            postResourceTemplate = templateProvider.templateFor("postResource");
        } else {
            postResourceTemplate = templateProvider.templateFor("postResourceNonAggregate");
        }
        compiledCode = setupPostResourceForCompilation(postResourceTemplate, applicationModel, entityModel);
        String postResourceClassName = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s/", postResourceClassName));
        codes.put(postResourceClassName, compiledCode);
    }

    private void createPutResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        ST putResourceTemplate = templateProvider.templateFor("putResource");
        CompiledCode compiledCode = setupPutResourceForCompilation(putResourceTemplate, entityModel);
        String putResourceClassName = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}/", putResourceClassName));
        codes.put(putResourceClassName, compiledCode);
    }

    private void createListResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        String listResourceClassName;
        CompiledCode compiledCode;
        if (entityModel.isAggregate()) {
            ST listResourceTemplate = templateProvider.templateFor("listResource");
            String collectionLinks = entityModel.getApplicationModel().getRootEntities().stream().map(e -> "," + e.getSimpleName() + "sResource.class").collect(Collectors.joining());
            listResourceTemplate.add("listLinks", "       return super.getLinks(Post"+entityModel.getSimpleName()+"Resource.class"+ collectionLinks+");");
            compiledCode = setupListResourceForCompilation(listResourceTemplate, entityModel);
        } else {
            ST listResourceTemplate = templateProvider.templateFor("listResourceNonAggregate");
            compiledCode = setupListResourceForCompilation(listResourceTemplate, entityModel);
        }
        listResourceClassName = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s", listResourceClassName));
        codes.put(listResourceClassName, compiledCode);
        
        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private void createRelationResources(DesignerEntityModel entityModel, EntityRelation relation, Map<String, CompiledCode> codes) {
        CompiledCode compiledCode;
        ST template;
        if (entityModel.isAggregate()) {
            template = templateProvider.templateFor("relationResource");
        } else {
            return;//   template = templateProvider.templateFor("entityResourceNonAggregate");
        }
        compiledCode = setupRelationResourceForCompilation(template, entityModel, relation);
        String name = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}/OEs", name));
        codes.put(name, compiledCode);
        
        
        template = templateProvider.templateFor("postRelationResource");
        compiledCode = setupPostRelationResourceForCompilation(template, entityModel, relation);
        name = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getId() + "s/{id}/OEs/", name));
        codes.put(name, compiledCode);
        
        
    }

    private CompiledCode setupEntityForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityName = entityModel.getId();
        return collect(entityName, entityCode, BUILD_PATH_SOURCE);
    }

    private CompiledCode setupEntityResourceForCompilation(ST template, DesignerEntityModel entityModel) {
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
        return collect(entityName, entityCode, BUILD_PATH_SOURCE);
    }

    private CompiledCode setupPostResourceForCompilation(ST template, DesignerApplicationModel applicationModel,
            DesignerEntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "Resource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);

        StringBuilder addEntityCode;
        addEntityCode = new StringBuilder("Subject subject = SecurityUtils.getSubject();\n");
//        addEntityCode.append(entityModel.getActionFields().stream().map(actionField -> {
//            return actionField.getCode("postEntity#addEntity").replace("$Methodname$", withFirstCapital(actionField.getName()));
//        }).collect(Collectors.joining("\n")));
        if (entityModel.isAggregate()) {
            Optional<DesignerFieldModel> uuidField = getFieldModelFor(entityModel, FieldRole.GUID);
            if (uuidField.isPresent()) {
                String methodName = uuidField.get().getName().substring(0, 1).toUpperCase() + uuidField.get().getName().substring(1);
                addEntityCode.append("entity.set"+methodName+"(java.util.UUID.randomUUID().toString());\n");
            }
            Optional<DesignerFieldModel> ownerField = getFieldModelFor(entityModel, FieldRole.OWNER);
            if (ownerField.isPresent()) {
                String methodName = ownerField.get().getName().substring(0, 1).toUpperCase() + ownerField.get().getName().substring(1);
                //addEntityCode.append("Subject subject = SecurityUtils.getSubject();\n");
                addEntityCode.append("entity.set"+methodName+"(subject.getPrincipal().toString());\n");
            }
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
        return collect(entityClassName, entityCode, BUILD_PATH_SOURCE);
    }

    private CompiledCode setupPutResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getSimpleName() + "Resource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String updateEntityCode = entityModel.getId() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";
        
        Optional<DesignerFieldModel> dfm = getFieldModelFor(entityModel, FieldRole.MODIFIED_AT);
        if (dfm.isPresent()) {
            String methodName = dfm.get().getName().substring(0, 1).toUpperCase() + dfm.get().getName().substring(1);
            updateEntityCode += "original.set"+methodName+"(new Date());\n";
        }
        
        template.add("updateEntity", updateEntityCode);
        return collect(entityModel.getPackageName() + "." + simpleClassName, template.render(), BUILD_PATH_SOURCE);
    }

    private CompiledCode setupListResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = entityModel.getSimpleName() + "sResource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return collect(className, entityCode, BUILD_PATH_SOURCE);
    }
    
    private CompiledCode setupRelationResourceForCompilation(ST template, DesignerEntityModel entityModel, EntityRelation relation) {
        final String simpleClassName = entityModel.getSimpleName() + "s"+relation.getTargetEntityModel().getSimpleName()+"sResource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        template.add("relation", relation);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return collect(className, entityCode, BUILD_PATH_SOURCE);
    }
    
    private CompiledCode setupPostRelationResourceForCompilation(ST template, DesignerEntityModel entityModel, EntityRelation relation) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "s"+relation.getTargetEntityModel().getSimpleName()+"RelationResource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        template.add("relation", relation);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return collect(className, entityCode, BUILD_PATH_SOURCE);
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }
    
    private CharSequence withFirstCapital(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    private Optional<DesignerFieldModel> getFieldModelFor(DesignerEntityModel entityModel, FieldRole fieldRole) {
        return entityModel
                .getFieldValues().stream()
                .map(DesignerFieldModel.class::cast)
                .filter(fieldModel -> fieldModel.getRole() != null)
                .filter(fieldModel -> fieldModel.getRole().equals(fieldRole)).findFirst();
    }


}
