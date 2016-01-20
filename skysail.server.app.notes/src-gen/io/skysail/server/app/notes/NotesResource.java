package io.skysail.server.app.notes;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class NotesResource extends ListServerResource<io.skysail.server.app.notes.Note> {

    private NotesApplication app;
    private NoteRepository repository;

    public NotesResource() {
        super(NoteResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Notes");
    }

    @Override
    protected void doInit() {
        app = (NotesApplication) getApplication();
        repository = (NoteRepository) app.getRepository(io.skysail.server.app.notes.Note.class);
    }

    /*@Override
    public Set<String> getRestrictedToMediaTypes() {
        return super.getRestrictedToMediaTypes("standalone/*");
    }*/

    @Override
    public List<io.skysail.server.app.notes.Note> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
              return super.getLinks(PostNoteResource.class,NotesResource.class);
    }
}