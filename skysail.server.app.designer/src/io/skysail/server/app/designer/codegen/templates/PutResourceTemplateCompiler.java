package io.skysail.server.app.designer.codegen.templates;

import java.util.*;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.app.designer.model.*;

public class PutResourceTemplateCompiler extends AbstractTemplateCompiler {

    public PutResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplateName() {
        return "putResource";
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupPutResourceForCompilation(template, getEntityModel());
    }

    @Override
    public String getRoutePath() {
        return "/" + getEntityModel().getSimpleName() + "s/{id}/";
    }
    
    private CompiledCode setupPutResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getSimpleName() + "ResourceGen";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        String updateEntityCode = entityModel.getId() + " original = getEntity();\n";
        updateEntityCode += "copyProperties(original,entity);\n";

        Optional<DesignerFieldModel> dfm = getFieldModelFor(entityModel, FieldRole.MODIFIED_AT);
        if (dfm.isPresent()) {
            String methodName = dfm.get().getName().substring(0, 1).toUpperCase() + dfm.get().getName().substring(1);
            updateEntityCode += "original.set" + methodName + "(new Date());\n";
        }

        template.add("updateEntity", updateEntityCode);
        return getSkysailCompiler().collect(entityModel.getPackageName() + ".resources." + simpleClassName, template.render(),
                SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }
    
    private Optional<DesignerFieldModel> getFieldModelFor(DesignerEntityModel entityModel, FieldRole fieldRole) {
        return entityModel.getFieldValues().stream().map(DesignerFieldModel.class::cast)
                .filter(fieldModel -> fieldModel.getRole() != null)
                .filter(fieldModel -> fieldModel.getRole().equals(fieldRole)).findFirst();
    }

}
