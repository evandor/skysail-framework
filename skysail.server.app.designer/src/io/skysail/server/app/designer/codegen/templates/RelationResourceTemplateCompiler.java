package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class RelationResourceTemplateCompiler extends AbstractTemplateCompiler {

    public RelationResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplate() {
        if (getEntityModel().isAggregate()) {
            return "relationResource";
        } else {
            return null;// template =
                        // templateProvider.templateFor("entityResourceNonAggregate");
        }
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupRelationResourceForCompilation(template, getEntityModel(), getRelation());
    }

    @Override
    public String routePath() {
        return "/" + getEntityModel().getSimpleName() + "s/{id}/" + getRelation().getTargetEntityModel().getSimpleName() + "s";
    }
    
    private CompiledCode setupRelationResourceForCompilation(ST template, DesignerEntityModel entityModel,
            EntityRelation relation) {
        final String simpleClassName = entityModel.getSimpleName() + "s"
                + relation.getTargetEntityModel().getSimpleName() + "sResource";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        template.add("relation", relation);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return getSkysailCompiler().collect(className, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }


}
