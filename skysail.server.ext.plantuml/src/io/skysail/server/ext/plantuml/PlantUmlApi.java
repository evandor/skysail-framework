package io.skysail.server.ext.plantuml;

import java.io.IOException;

public interface PlantUmlApi {

    void createPng(String plantUmlSource, String filename) throws IOException;

    String getSvg(String plantUmlSource) throws IOException;

}
