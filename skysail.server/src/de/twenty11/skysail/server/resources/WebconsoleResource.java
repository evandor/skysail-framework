package de.twenty11.skysail.server.resources;

import java.io.IOException;
import java.io.InputStream;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.utils.IOUtils;

public class WebconsoleResource extends ServerResource {

    private static final String WEBCONSOLE_TEMPLATE = "webconsole.template";
    private String rootTemplate;

    public WebconsoleResource() {
        InputStream bootstrapTemplateResource = this.getClass().getResourceAsStream(WEBCONSOLE_TEMPLATE);
        rootTemplate = IOUtils.convertStreamToString(bootstrapTemplateResource);
        try {
            bootstrapTemplateResource.close();
        } catch (IOException e) {
            rootTemplate = "Exception parsing template '" + WEBCONSOLE_TEMPLATE + "':" + e.getMessage();
        }
    }

    @Get("html")
    public StringRepresentation get() {
        StringRepresentation representation = new StringRepresentation("");
        representation.setMediaType(MediaType.TEXT_HTML);

        SkysailApplication app = (SkysailApplication) getApplication();
        String webConsolePath = "";//app.getConfigForKey(Constants.SKYSAIL_WEBCONSOLE_PATH);
        if (webConsolePath == null) {
            representation.setText("'" + Constants.SKYSAIL_WEBCONSOLE_PATH + "' is not defined in configuration.");
        } else {
            String html = rootTemplate;
            html = html.replace("${webconsolePath}", webConsolePath);
            representation.setText(html);
        }
        return representation;
    }
}
