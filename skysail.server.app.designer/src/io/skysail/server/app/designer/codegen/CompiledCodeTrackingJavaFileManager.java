package io.skysail.server.app.designer.codegen;

import java.io.IOException;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompiledCodeTrackingJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    
    static {
        if (javac == null) {
            log.error("running the app designer application requeres a JDK. It seems this installation is using a JRE");
        }
    }

    private Map<String, CompiledCode> compiledCodes;
    private DynamicClassLoader cl;

    public CompiledCodeTrackingJavaFileManager(Map<String, CompiledCode> compiledCodes) {
        super(javac.getStandardFileManager(null, null, null));
        this.compiledCodes = compiledCodes;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
            JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return compiledCodes.get(className);
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return cl;
    }

    public void add(CompiledCode compiledCode) {
        this.compiledCodes.put(compiledCode.getName(), compiledCode);
        this.cl.setCode(compiledCode);
    }

    public void setClassLoader(DynamicClassLoader dcl) {
        this.cl = dcl;
    }

    public void clearCompiledCode() {
        this.compiledCodes.clear();  
    }
}