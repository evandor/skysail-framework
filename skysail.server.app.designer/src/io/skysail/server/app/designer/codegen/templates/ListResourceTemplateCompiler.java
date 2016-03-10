package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import lombok.Setter;

public class ListResourceTemplateCompiler extends AbstractTemplateCompiler {

    @Setter
    private String collectionLinks;

    public ListResourceTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        super(skysailCompiler, entityModel, relation, codes);
    }

    @Override
    public String getTemplate() {
        if (getEntityModel().isAggregate() && !getEntityModel().hasSelfReference()) {
            return "listResource";
        } else if (getEntityModel().isAggregate() && getEntityModel().hasSelfReference()) {
            return "listResourceWithSelfReference";
        } else {
            return "listResourceNonAggregate";
        }
    }

    @Override
    public CompiledCode apply(ST template) {
        return setupListResourceForCompilation(template, getEntityModel());
    }

    @Override
    public String routePath() {
        return "/" + getEntityModel().getSimpleName() + "s";
    }

    @Override
    public void addAdditionalAttributes(ST template) {
        if (getEntityModel().isAggregate() && !getEntityModel().hasSelfReference()) {
            template.add("listLinks", "       return super.getLinks(Post" + getEntityModel().getSimpleName()
                    + "ResourceGen.class" + collectionLinks + ");");
        } else if (getEntityModel().isAggregate() && getEntityModel().hasSelfReference()) {
            template.add("listLinks", "       return super.getLinks(Post" + getEntityModel().getSimpleName()
                    + "ResourceGen.class" + collectionLinks + ");");
        }
    }
    
    private CompiledCode setupListResourceForCompilation(ST template, DesignerEntityModel entityModel) {
        final String simpleClassName = entityModel.getSimpleName() + "sResource";
        template.remove(SkysailEntityCompiler.ENTITY_IDENTIFIER);
        template.add(SkysailEntityCompiler.ENTITY_IDENTIFIER, entityModel);
        String entityCode = template.render();
        String className = entityModel.getPackageName() + ".resources." + simpleClassName + "Gen";
        return getSkysailCompiler().collect(className, entityCode, SkysailEntityCompiler.BUILD_PATH_SOURCE);
    }


}
