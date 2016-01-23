package io.skysail.server.app.notes.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.notes.NotesResource;

public class MyNotesResource extends NotesResource {
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(MyPostNoteResource.class,MyNotesResource.class);
    }

}
