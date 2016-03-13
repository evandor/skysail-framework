package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class TargetRelationResourceTemplateCompiler extends AbstractTemplateCompiler {

    public TargetRelationResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplateName() {
        return "targetRelationResource";
    }            @Override
    public CompiledCode apply(ST template) {
        return setupTargetRelationResourceForCompilation(template, getEntityModel(), getRelation());
    }
    @Override
    public String getRoutePath() {
        return "/" + getEntityModel().getSimpleName() + "s/{id}/"+getRelation().getTargetEntityModel().getSimpleName()+"s/{targetId}";
    }
    
    private CompiledCode setupTargetRelationResourceForCompilation(ST template, DesignerEntityModel entityModel,
            EntityRelation relation) {
        final String simpleClassName = entityModel.getSimpleName() + "s"
                + relation.getTargetEntityModel().getSimpleName() + "Resource";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        template.add("relation", relation);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return getSkysailCompiler().collect(className, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }

}
