package io.skysail.server.app.designer.codegen;

import org.stringtemplate.v4.ST;

public interface TemplateCompiler {

    String getTemplate();

    CompiledCode apply(ST template);

    String routePath();
    
    void addAdditionalAttributes(ST template);

}
