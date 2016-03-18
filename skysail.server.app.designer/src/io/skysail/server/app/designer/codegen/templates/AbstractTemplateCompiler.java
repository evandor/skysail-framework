package io.skysail.server.app.designer.codegen.templates;

import java.util.Map;

import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.SkysailCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.codegen.TemplateCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.RouteModel;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractTemplateCompiler implements TemplateCompiler {

    private DesignerEntityModel entityModel;
    private EntityRelation relation;
    private Map<String, CompiledCode> codes;
    private SkysailCompiler skysailCompiler;

    public AbstractTemplateCompiler(SkysailCompiler skysailCompiler, DesignerEntityModel entityModel,
            EntityRelation relation, @NonNull Map<String, CompiledCode> codes) {
        this.skysailCompiler = skysailCompiler;
        this.entityModel = entityModel;
        this.relation = relation;
        this.codes = codes;
    }
    
    @Override
    public void addAdditionalAttributes(ST template) {
        // default behavior, do nothing
    }

    @Override
    public final String process() {
        String templateName = getTemplateName();
        if (templateName == null) {
            return null;
        }
        SkysailEntityCompiler sec = (SkysailEntityCompiler) getSkysailCompiler();
        ST template = sec.getTemplateProvider().templateFor(templateName);
        addAdditionalAttributes(template);
        CompiledCode compiledCode = apply(template);
        String name = compiledCode.getClassName();
        String routePath = getRoutePath();
        if (routePath != null) {
            sec.getRoutes().add(new RouteModel(getRoutePath(), compiledCode.getSimpleName()));
        }
        codes.put(name, compiledCode);
        return name;
    }
}
