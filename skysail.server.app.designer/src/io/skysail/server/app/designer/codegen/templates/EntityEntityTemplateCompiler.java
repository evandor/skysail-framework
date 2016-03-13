package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.SkysailCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public class EntityEntityTemplateCompiler extends AbstractTemplateCompiler {

    public EntityEntityTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
        
    }

    @Override
    public String getTemplateName() {
        return "javafile";
    }

    @Override
    public String getRoutePath() {
        return null;
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupEntityForCompilation(template, getEntityModel());
    }

    private CompiledCode setupEntityForCompilation(ST template, DesignerEntityModel entityModel) {
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String entityName = entityModel.getId();
        return getSkysailCompiler().collect(entityName, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }
}
