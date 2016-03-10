package io.skysail.server.app.designer.codegen;

import java.util.*;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.templates.*;
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
        handleUnit(new PutResourceTemplateCompiler(this, entityModel, null, codes));
        createListResource(entityModel, codes);

        entityModel.getRelations().stream().forEach(relation -> createRelationResources(entityModel, relation, codes));

        return codes;
    }

    private void createPostResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        PostResourceTemplateCompiler templateCompiler = new PostResourceTemplateCompiler(this, entityModel, null, codes);
        templateCompiler.setApplicationModel(applicationModel);
        handleUnit(templateCompiler);
    }

    private void createListResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        String collectionLinks = entityModel.getApplicationModel().getRootEntities().stream()
              .map(e -> "," + e.getSimpleName() + "sResourceGen.class").collect(Collectors.joining());        
        ListResourceTemplateCompiler templateCompiler = new ListResourceTemplateCompiler(this, entityModel, null, codes);
        templateCompiler.setCollectionLinks(collectionLinks);
        String listResourceClassName = handleUnit(templateCompiler);
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

    private String handleUnit(AbstractTemplateCompiler f) {
        String templateName = f.getTemplate();
        if (templateName == null) {
            return null;
        }
        ST template = templateProvider.templateFor(templateName);
        f.addAdditionalAttributes(template);
        CompiledCode compiledCode = f.apply(template);
        String name = compiledCode.getClassName();
        String routePath = f.routePath();
        if (routePath != null) {
            routes.add(new RouteModel(f.routePath(), name));
        }
        f.getCodes().put(name, compiledCode);
        return name;
    }

    private CompiledCode setupEntityForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(ENTITY_IDENTIFIER);
        template.add(ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityName = entityModel.getId();
        return collect(entityName, entityCode, BUILD_PATH_SOURCE);
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }

   

}
