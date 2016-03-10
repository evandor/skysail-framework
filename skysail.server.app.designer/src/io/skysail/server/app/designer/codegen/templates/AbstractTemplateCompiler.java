package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import lombok.Getter;

@Getter
public abstract class AbstractTemplateCompiler implements TemplateCompiler {

    private DesignerEntityModel entityModel;
    private EntityRelation relation;
    private Map<String, CompiledCode> codes;
    private SkysailCompiler skysailCompiler;

    public AbstractTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, Map<String, CompiledCode> codes) {
        this.skysailCompiler = skysailCompiler;
        this.entityModel = entityModel;
        this.relation = relation;
        this.codes = codes;
    }
    
    @Override
    public void addAdditionalAttributes(ST template) {
        
    }

}
