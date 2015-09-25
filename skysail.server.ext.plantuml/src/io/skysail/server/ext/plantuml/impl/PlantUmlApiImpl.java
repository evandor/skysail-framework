package io.skysail.server.ext.plantuml.impl;

import io.skysail.server.ext.plantuml.PlantUmlApi;

import java.io.*;
import java.nio.charset.Charset;

import net.sourceforge.plantuml.*;

public class PlantUmlApiImpl implements PlantUmlApi {

    @Override
    public void createPng(String plantUmlSource, String filename) throws IOException {
        OutputStream png = new FileOutputStream(filename);
        SourceStringReader reader = new SourceStringReader(wrapInputWithMarker(plantUmlSource));
        String desc = reader.generateImage(png);
        throwExceptionIfError(desc);
    }


    @Override
    public String getSvg(String plantUmlSource) throws IOException {
        SourceStringReader reader = new SourceStringReader(wrapInputWithMarker(plantUmlSource));
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();
        throwExceptionIfError(desc);
        return new String(os.toByteArray(), Charset.forName("UTF-8"));
    }

    private String wrapInputWithMarker(String plantUmlSource) {
        String source = "@startuml\n";
        source += plantUmlSource + "\n";
        source += "@enduml\n";
        return source;
    }

    private void throwExceptionIfError(String desc) throws IOException {
        if (desc == null) {
            throw new IOException("could not generate the plantUml Image");
        }
    }

}
