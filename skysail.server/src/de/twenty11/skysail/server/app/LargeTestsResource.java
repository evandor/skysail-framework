package de.twenty11.skysail.server.app;

import io.skysail.server.restlet.resources.ListServerResource;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.slf4j.*;

class LargeTestsResource extends ListServerResource<String> {

    private static final Logger logger = LoggerFactory.getLogger(LargeTestsResource.class);

    public static List<Path> fileList(Path directory) {
        List<Path> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path path : directoryStream) {
                fileNames.add(path);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return fileNames;
    }

    @Override
    public List<String> getEntity() {
        return null;
    }
}
