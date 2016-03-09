package io.skysail.server.app.designer.codegen;

import java.util.*;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.templates.*;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.Getter;

public class SkysailEntityCompiler extends SkysailCompiler {

    public static final String BUILD_PATH_SOURCE = "src-gen";
    public static final String ENTITY_IDENTIFIER = "entity";

    private List<RouteModel> routes = new ArrayList<>();

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;
    private TemplateProvider templateProvider;

    public SkysailEntityCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            JavaCompiler compiler, TemplateProvider templateProvider) {
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
        handleUnit(new EntityResourceTemplateCompiler(this, entityModel, null, codes));
        createPostResource(entityModel, codes);
        createPutResource(entityModel, codes);
        createListResource(entityModel, codes);

        entityModel.getRelations().stream().forEach(relation -> createRelationResources(entityModel, relation, codes));

        return codes;
    }

    private void createPostResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        
        PostResourceTemplateCompiler templateCompiler = new PostResourceTemplateCompiler(this, entityModel, null, codes);
        templateCompiler.setApplicationModel(applicationModel);
        handleUnit(templateCompiler);
        
//        CompiledCode compiledCode;
//        ST postResourceTemplate;
//        if (entityModel.isAggregate()) {
//            postResourceTemplate = templateProvider.templateFor("postResource");
//        } else {
//            postResourceTemplate = templateProvider.templateFor("postResourceNonAggregate");
//        }
//        compiledCode = setupPostResourceForCompilation(postResourceTemplate, applicationModel, entityModel);
//        String postResourceClassName = compiledCode.getClassName();
//        routes.add(new RouteModel("/" + entityModel.getSimpleName() + "s/", postResourceClassName));
//        codes.put(postResourceClassName, compiledCode);
    }

    private void createPutResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        
        handleUnit(new PutResourceTemplateCompiler(this, entityModel, null, codes));
        
//        ST putResourceTemplate = templateProvider.templateFor("putResource");
//        CompiledCode compiledCode = setupPutResourceForCompilation(putResourceTemplate, entityModel);
//        String putResourceClassName = compiledCode.getClassName();
//        routes.add(new RouteModel("/" + entityModel.getSimpleName() + "s/{id}/", putResourceClassName));
//        codes.put(putResourceClassName, compiledCode);
    }

    private void createListResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        String listResourceClassName;
        CompiledCode compiledCode;
        String collectionLinks = entityModel.getApplicationModel().getRootEntities().stream()
                .map(e -> "," + e.getSimpleName() + "sResourceGen.class").collect(Collectors.joining());
        ST listResourceTemplate;
        if (entityModel.isAggregate() && !entityModel.hasSelfReference()) {
            listResourceTemplate = templateProvider.templateFor("listResource");
            listResourceTemplate.add("listLinks", "       return super.getLinks(Post" + entityModel.getSimpleName()
                    + "ResourceGen.class" + collectionLinks + ");");
        } else if (entityModel.isAggregate() && entityModel.hasSelfReference()) {
            listResourceTemplate = templateProvider.templateFor("listResourceWithSelfReference");
            listResourceTemplate.add("listLinks", "       return super.getLinks(Post" + entityModel.getSimpleName()
                    + "ResourceGen.class" + collectionLinks + ");");
        } else {
            listResourceTemplate = templateProvider.templateFor("listResourceNonAggregate");
        }
        compiledCode = setupListResourceForCompilation(listResourceTemplate, entityModel);
        listResourceClassName = compiledCode.getClassName();
        routes.add(new RouteModel("/" + entityModel.getSimpleName() + "s", listResourceClassName));
        codes.put(listResourceClassName, compiledCode);

        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private void createRelationResources(DesignerEntityModel entityModel, EntityRelation relation, Map<String, CompiledCode> codes) {
        handleUnit(new RelationResourceTemplateCompiler(this, entityModel, relation, codes));
        handleUnit(new PostRelationTemplateCompiler(this, entityModel, relation, codes));
        handleUnit(new PostRelationToNewEntityTemplateCompiler(this, entityModel, relation, codes));
        handleUnit(new TargetRelationResourceTemplateCompiler(this, entityModel, relation, codes));
    }

    private void handleUnit(AbstractTemplateCompiler f) {
        String theTemplate = f.getTemplate();
        if (theTemplate == null) {
            return;
        }
        ST template = templateProvider.templateFor(theTemplate);
        CompiledCode compiledCode = f.apply(template);
        String name = compiledCode.getClassName();
        String routePath = f.routePath();
        if (routePath != null) {
            routes.add(new RouteModel(f.routePath(), name));
        }
        f.getCodes().put(name, compiledCode);
    }

    private CompiledCode setupEntityForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityName = entityModel.getId();
        return collect(entityName, entityCode, BUILD_PATH_SOURCE);
    }

    

    private CompiledCode setupPutResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getSimpleName() + "ResourceGen";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String updateEntityCode = entityModel.getId() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";

        Optional<DesignerFieldModel> dfm = getFieldModelFor(entityModel, FieldRole.MODIFIED_AT);
        if (dfm.isPresent()) {
            String methodName = dfm.get().getName().substring(0, 1).toUpperCase() + dfm.get().getName().substring(1);
            updateEntityCode += "original.set" + methodName + "(new Date());\n";
        }

        template.add("updateEntity", updateEntityCode);
        return collect(entityModel.getPackageName() + ".resources." + simpleClassName, template.render(),
                BUILD_PATH_SOURCE);
    }

    private CompiledCode setupListResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = entityModel.getSimpleName() + "sResource";
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + ".resources." + simpleClassName + "Gen";
        return collect(className, entityCode, BUILD_PATH_SOURCE);
    }


    public List<RouteModel> getRouteModels() {
        return routes;
    }

    private Optional<DesignerFieldModel> getFieldModelFor(DesignerEntityModel entityModel, FieldRole fieldRole) {
        return entityModel.getFieldValues().stream().map(DesignerFieldModel.class::cast)
                .filter(fieldModel -> fieldModel.getRole() != null)
                .filter(fieldModel -> fieldModel.getRole().equals(fieldRole)).findFirst();
    }

}
