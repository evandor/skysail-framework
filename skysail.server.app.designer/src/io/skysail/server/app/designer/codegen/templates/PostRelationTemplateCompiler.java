package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class PostRelationTemplateCompiler extends AbstractTemplateCompiler {

    public PostRelationTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplate() {
        if (!getEntityModel().hasSelfReference()) {
            return "postRelationResource";
        } else {
            return "postSelfRelationResource";
        }
    }
    @Override
    public CompiledCode apply(ST template) {
        return setupPostRelationResourceForCompilation(template, getEntityModel(), getRelation());
    }
    @Override
    public String routePath() {
        return null;
    }

    private CompiledCode setupPostRelationResourceForCompilation(ST template, DesignerEntityModel entityModel,
            EntityRelation relation) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "s"
                + relation.getTargetEntityModel().getSimpleName() + "RelationResource";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        template.add("relation", relation);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + "." + simpleClassName;
        return getSkysailCompiler().collect(className, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }


}
