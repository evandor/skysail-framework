package io.skysail.server.app.fileserver;

import io.skysail.server.restlet.resources.ListServerResource;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ListFilesResource extends ListServerResource<FileDescriptor> {

    @Override
    public List<FileDescriptor> getEntity() {
        try {
            return Files.list(new File(".").toPath()).map(f -> new FileDescriptor(f)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
