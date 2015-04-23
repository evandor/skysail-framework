package io.skysail.server.app.designer.codegen;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SourceCode extends SimpleJavaFileObject {
    
    private String contents = null;

    public SourceCode(String className, String contents) throws Exception {
        super(URI.create(calcUri(className)), Kind.SOURCE);
        log.debug("creating new SourceCode at {}", calcUri(className));  
        this.contents = contents;
    }

    private static String calcUri(String className) {
        return "string:///" + className.replace('.', '/') + Kind.SOURCE.extension;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
}