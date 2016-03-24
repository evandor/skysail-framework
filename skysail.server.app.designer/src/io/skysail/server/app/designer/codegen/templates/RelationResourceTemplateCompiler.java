package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.SkysailCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class RelationResourceTemplateCompiler extends AbstractTemplateCompiler {

    public RelationResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplateName() {
        if (getEntityModel().isAggregate()) {
            return "relationResource";
        }
        return null;
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupRelationResourceForCompilation(template, getEntityModel(), getRelation());
    }

    @Override
    public String getRoutePath() {
        return "/" + getEntityModel().getSimpleName() + "s/{id}/" + getRelation().getTargetEntityModel().getSimpleName()
                + "s";
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
