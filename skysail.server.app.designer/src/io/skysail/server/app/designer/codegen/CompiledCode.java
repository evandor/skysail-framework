package io.skysail.server.app.designer.codegen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

import lombok.Getter;

public class CompiledCode extends SimpleJavaFileObject {
    
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    @Getter
    private String className;

    public CompiledCode(String className) throws URISyntaxException {
        super(new URI(className), Kind.CLASS);
        this.className = className;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return baos;
    }

    public byte[] getByteCode() {
        return baos.toByteArray();
    }
}