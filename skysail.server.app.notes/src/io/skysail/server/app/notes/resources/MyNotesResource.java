package io.skysail.server.app.notes.resources;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.app.notes.NotesResource;

public class MyNotesResource extends NotesResource {
    
    public MyNotesResource() {
        super(MyNoteResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Notes");
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(MyPostNoteResource.class  );
    }

}
