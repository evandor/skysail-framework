package de.twenty11.skysail.server.app;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class LargeTestsResource extends ListServerResource<String> {

    private static final Logger logger = LoggerFactory.getLogger(LargeTestsResource.class);

    // @Override
    // public List<String> getData() {
    // Path dir = Paths.get("./", "generated", "largetests","response");
    // return fileList(dir).stream().map(d ->
    // d.getFileName().toString()).collect(Collectors.toList());
    // }

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
        // TODO Auto-generated method stub
        return null;
    }
}
