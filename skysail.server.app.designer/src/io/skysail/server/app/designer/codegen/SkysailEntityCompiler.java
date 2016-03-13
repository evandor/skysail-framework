package io.skysail.server.app.designer.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.templates.*;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.Getter;

public class SkysailEntityCompiler extends SkysailCompiler {

    public static final String BUILD_PATH_SOURCE = "src-gen";
    public static final String ENTITY_IDENTIFIER = "entity";

    @Getter
    private List<RouteModel> routes = new ArrayList<>();

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    @Getter
    private TemplateProvider templateProvider;

    public SkysailEntityCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            JavaCompiler compiler, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.stGroupDir = stGroup;
        this.templateProvider = templateProvider;
    }

    public CompiledCode createEntity(DesignerEntityModel entityModel) {
        EntityEntityTemplateCompiler entityEntityTemplateCompiler = new EntityEntityTemplateCompiler(this, entityModel, null, new HashMap<String, CompiledCode>());
        entityEntityTemplateCompiler.process();
//        ST template = templateProvider.templateFor("javafile");
//        CompiledCode compiledCode = setupEntityForCompilation(template, entityModel);
//        entityClassName = compiledCode.getClassName();
//        entityModel.setClassName(entityClassName);
//        return compiledCode;
        return entityEntityTemplateCompiler.getCodes().values().iterator().next();
    }

    public Map<String, CompiledCode> createResources(DesignerEntityModel entityModel) {
        Map<String, CompiledCode> codes = new HashMap<>();
        new EntityResourceTemplateCompiler(this, entityModel, null, codes).process();
        createPostResource(entityModel, codes);
        new PutResourceTemplateCompiler(this, entityModel, null, codes).process();
        createListResource(entityModel, codes);

        entityModel.getRelations().stream().forEach(relation -> createRelationResources(entityModel, relation, codes));

        return codes;
    }

    private void createPostResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        PostResourceTemplateCompiler templateCompiler = new PostResourceTemplateCompiler(this, entityModel, null, codes);
        templateCompiler.setApplicationModel(applicationModel);
        templateCompiler.process();
    }

    private void createListResource(DesignerEntityModel entityModel, Map<String, CompiledCode> codes) {
        String collectionLinks = entityModel.getApplicationModel().getRootEntities().stream()
              .map(e -> "," + e.getSimpleName() + "sResourceGen.class").collect(Collectors.joining());        
        ListResourceTemplateCompiler templateCompiler = new ListResourceTemplateCompiler(this, entityModel, null, codes);
        templateCompiler.setCollectionLinks(collectionLinks);
        String listResourceClassName = templateCompiler.process();
        if (entityModel.isAggregate()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private void createRelationResources(DesignerEntityModel entityModel, EntityRelation relation, Map<String, CompiledCode> codes) {
        new RelationResourceTemplateCompiler(this, entityModel, relation, codes).process();
        new PostRelationTemplateCompiler(this, entityModel, relation, codes).process();
        new PostRelationToNewEntityTemplateCompiler(this, entityModel, relation, codes).process();
        new TargetRelationResourceTemplateCompiler(this, entityModel, relation, codes).process();
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
