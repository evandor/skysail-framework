package io.skysail.server.app.designer.codegen;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.stringtemplate.v4.STGroupDir;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SkysailCompiler {

    protected DesignerApplicationModel applicationModel;
    protected STGroupDir stGroupDir;
    private JavaCompiler compiler;
    
    public SkysailCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        this.applicationModel = applicationModel;
        this.stGroupDir = stGroup;
        this.compiler = compiler;
    }

    public boolean compile(BundleContext bundleContext) {
        return compiler.compile(bundleContext);
    }

    protected String substitute(String template, Map<String, String> substitutionMap) {
        for (String key : substitutionMap.keySet()) {
            if (substitutionMap.get(key) != null) {
                template = template.replace(key, substitutionMap.get(key));
            }
        }
        return template;
    }

    protected CompiledCode collect(String className, String entityCode, String buildPathFolder) {
        CompiledCode compiledCode = null;
        try {
            compiledCode = compiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
        ProjectFileWriter.save(applicationModel, buildPathFolder, classNameToPath(className), entityCode.getBytes());
        return compiledCode;
    }

    protected CompiledCode collectSource(String className, String entityCode, String buildPathFolder) {
        CompiledCode compiledCode = null;
        compiler.collectSource(className, entityCode);
        ProjectFileWriter.save(applicationModel, buildPathFolder, classNameToPath(className), entityCode.getBytes());
        return compiledCode;
    }

    public void reset() {
        compiler.reset();
    }

    protected String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".java");
    }

    public Class<?> getClass(String className) {
        try {
            return compiler.getClass(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
