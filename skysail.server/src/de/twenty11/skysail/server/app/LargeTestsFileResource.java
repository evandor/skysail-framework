package de.twenty11.skysail.server.app;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LargeTestsFileResource extends EntityServerResource<String> {
	
	private static final Logger logger = LoggerFactory.getLogger(LargeTestsFileResource.class);

    private String pathid;

    protected void doInit() throws ResourceException {
        pathid = getAttribute("id").replace("..","");
    }

    @Override
    public String getEntity() {
        Path file = Paths.get("./", "generated", "largetests", "response", pathid);
        try {
            return Files.readAllLines(file, Charset.forName( "iso-8859-1")).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    

}
