package io.skysail.server.app.notes;

import java.util.List;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;

public class NotesApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Notes";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public NotesApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/{id}", io.skysail.server.app.notes.NoteResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/", io.skysail.server.app.notes.PostNoteResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/{id}/", io.skysail.server.app.notes.PutNoteResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes", io.skysail.server.app.notes.NotesResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.notes.NotesResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}