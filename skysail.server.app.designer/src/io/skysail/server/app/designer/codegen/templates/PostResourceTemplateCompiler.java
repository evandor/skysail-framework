package io.skysail.server.app.designer.codegen.templates;

import java.util.*;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.app.designer.model.*;
import lombok.Setter;

public class PostResourceTemplateCompiler extends AbstractTemplateCompiler {
    
    @Setter
    private DesignerApplicationModel applicationModel;

    public PostResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplateName() {
        if (getEntityModel().isAggregate()) {
            return "postResource";
        } else {
            return "postResourceNonAggregate";
        }
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupPostResourceForCompilation(template, applicationModel, getEntityModel());
    }

    @Override
    public String getRoutePath() {
        return "/" + getEntityModel().getSimpleName() + "s/";
    }
    
    private CompiledCode setupPostResourceForCompilation(ST template, DesignerApplicationModel applicationModel,
            DesignerEntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getSimpleName() + "Resource";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);

        StringBuilder addEntityCode;
        addEntityCode = new StringBuilder("Subject subject = SecurityUtils.getSubject();\n");
        // addEntityCode.append(entityModel.getActionFields().stream().map(actionField
        // -> {
        // return
        // actionField.getCode("postEntity#addEntity").replace("$Methodname$",
        // withFirstCapital(actionField.getName()));
        // }).collect(Collectors.joining("\n")));
        if (entityModel.isAggregate()) {
            Optional<DesignerFieldModel> uuidField = getFieldModelFor(entityModel, FieldRole.GUID);
            if (uuidField.isPresent()) {
                String methodName = uuidField.get().getName().substring(0, 1).toUpperCase()
                        + uuidField.get().getName().substring(1);
                addEntityCode.append("entity.set" + methodName + "(java.util.UUID.randomUUID().toString());\n");
            }
            Optional<DesignerFieldModel> ownerField = getFieldModelFor(entityModel, FieldRole.OWNER);
            if (ownerField.isPresent()) {
                String methodName = ownerField.get().getName().substring(0, 1).toUpperCase()
                        + ownerField.get().getName().substring(1);
                // addEntityCode.append("Subject subject =
                // SecurityUtils.getSubject();\n");
                addEntityCode.append("entity.set" + methodName + "(subject.getPrincipal().toString());\n");
            }
            addEntityCode.append("String id = app.getRepository(" + entityModel.getId()
                    + ".class).save(entity, app.getApplicationModel()).toString();\n");
            addEntityCode.append("entity.setId(id);\n");
        } else {
            // DesignerEntityModel parent = entityModel.getReferencedBy().get();
            // addEntityCode.append(parent.getId() + " root =
            // app.getRepository().getById("+parent.getId()+".class,
            // getAttribute(\"id\"));\n");
            // addEntityCode.append("root.add"+entityModel.getId()+"(entity);\n");
            // addEntityCode.append("app.getRepository().update(getAttribute(\"id\"),
            // root);\n");
        }
        template.add("addEntity", addEntityCode);
        String entityCode = template.render();
        String entityClassName = entityModel.getPackageName() + ".resources." + simpleClassName + "Gen";
        return getSkysailCompiler().collect(entityClassName, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }
    
    private Optional<DesignerFieldModel> getFieldModelFor(DesignerEntityModel entityModel, FieldRole fieldRole) {
        return entityModel.getFieldValues().stream().map(DesignerFieldModel.class::cast)
                .filter(fieldModel -> fieldModel.getRole() != null)
                .filter(fieldModel -> fieldModel.getRole().equals(fieldRole)).findFirst();
    }

    


}
