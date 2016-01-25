package io.skysail.server.app.notes;

import java.util.Arrays;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.notes.resources.MyNoteResource;
import io.skysail.server.app.notes.resources.MyNotesResource;
import io.skysail.server.app.notes.resources.MyPostNoteResource;
import io.skysail.server.app.notes.resources.MyPutNoteResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.uikit.webresource.RequireUiKitWebResource;

@Component(immediate = true)
@RequireUiKitWebResource
public class NotesApplication extends NotesApplicationGen implements ApplicationProvider, MenuItemProvider {
    
    public static final int TITLE_MAX_LENGTH = 30;

    public  NotesApplication() {
        super("Notes", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes", MyNotesResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/", MyPostNoteResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/{id}", MyNoteResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.notes.Notes/{id}/", MyPutNoteResource.class));
        router.attach(new RouteBuilder("", MyNotesResource.class));
    }

}