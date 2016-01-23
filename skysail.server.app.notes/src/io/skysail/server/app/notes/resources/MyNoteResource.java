package io.skysail.server.app.notes.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.notes.NoteResource;

public class MyNoteResource extends NoteResource {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MyPutNoteResource.class);
    }

}
