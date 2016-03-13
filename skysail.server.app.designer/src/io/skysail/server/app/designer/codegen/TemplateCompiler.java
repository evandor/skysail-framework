package io.skysail.server.app.designer.codegen;

import org.stringtemplate.v4.ST;

/**
 * Implementors define the information needed to create java code from
 * templates.
 */
public interface TemplateCompiler {

    /**
     * @return the name of the template (under resources/code) to be used.
     */
    String getTemplateName();

    /**
     * @return the url the created resource will be available at.
     */
    String getRoutePath();

    /**
     * a hook to add additional attributes to the template.
     */
    void addAdditionalAttributes(ST template);

    /**
     * apply the template to generate code.
     * 
     * @param template
     * @return java code
     */
    CompiledCode apply(ST template);
    
    String process();

}
