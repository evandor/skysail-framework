package io.skysail.server.app.notes;

import java.util.*;

import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.notes.resources.*;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.services.*;
import io.skysail.server.uikit.webresource.RequireUiKitWebResource;
import lombok.Getter;

@Component(immediate = true)
@RequireUiKitWebResource
public class NotesApplication extends NotesApplicationGen implements ApplicationProvider, MenuItemProvider, MessageQueueProvider {
    
    public static final int TITLE_MAX_LENGTH = 30;
    
    @Reference(cardinality = ReferenceCardinality.MULTIPLE)
    @Getter
    public volatile List<MessageQueueHandler> messageQueueHandler;

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