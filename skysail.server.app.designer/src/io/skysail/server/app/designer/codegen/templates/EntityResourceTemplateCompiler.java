package io.skysail.server.app.designer.codegen.templates;

import java.util.*;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class EntityResourceTemplateCompiler extends AbstractTemplateCompiler {

    public EntityResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplate() {
        if (getEntityModel().isAggregate()) {
            return "entityResource";
        } 
        return "entityResourceNonAggregate";
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupEntityResourceForCompilation(template, getEntityModel());
    }

    @Override
    public String routePath() {
        return "/" + getEntityModel().getSimpleName() + "s/{id}";
    }
    
    private CompiledCode setupEntityResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        List<String> linkedClasses = new ArrayList<>();
        linkedClasses.add("Put" + entityModel.getSimpleName() + "ResourceGen.class");
        entityModel.getRelations().stream().forEach(relation -> {
            String targetName = relation.getTargetEntityModel().getSimpleName();
            linkedClasses.add("Post" + targetName + "ResourceGen.class");
            linkedClasses.add(targetName + "sResourceGen.class");
        });

        entityModel.getReferences()
                .forEach(r -> linkedClasses.add("Post" + r.getReferencedEntityName() + "ResourceGen.class"));

        String getLinksCode = "return super.getLinks(" + linkedClasses.stream().collect(Collectors.joining(",")) + ");";
        template.add("links", getLinksCode);
        String entityCode = template.render();
        String entityName = entityModel.getPackageName() + ".resources." + entityModel.getSimpleName() + "ResourceGen";
        return getSkysailCompiler().collect(entityName, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }

}
