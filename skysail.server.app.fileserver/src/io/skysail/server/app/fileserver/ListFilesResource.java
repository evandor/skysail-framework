package io.skysail.server.app.fileserver;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ListFilesResource extends ListServerResource<FileDescriptor> {

    @Override
    public List<FileDescriptor> getEntity() {
        try {
            String pathId = getAttribute("id");
            String path = ((FileserverApplication)getApplication()).getPath(pathId);
            return Files.list(new File(path).toPath()).map(f -> new FileDescriptor(f)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ListFilesResource.class);
    }
}
