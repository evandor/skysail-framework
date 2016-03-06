package io.skysail.server.app.notes;

import java.io.IOException;
import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.restlet.Request;
import org.restlet.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.notes.resources.MyNoteResource;
import io.skysail.server.app.notes.resources.MyNotesResource;
import io.skysail.server.app.notes.resources.MyPostNoteResource;
import io.skysail.server.app.notes.resources.MyPutNoteResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.filter.FilterResult;
import io.skysail.server.restlet.filter.UpdateEntityFilter;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.server.restlet.response.ResponseWrapper;
import io.skysail.server.services.MessageQueueHandler;
import io.skysail.server.services.MessageQueueProvider;
import io.skysail.server.services.SkysailMessageListener;

@Component(immediate = true)
//@RequireUiKitWebResource
public class NotesApplication extends NotesApplicationGen
        implements ApplicationProvider, MenuItemProvider, MessageQueueProvider {

    private static ObjectMapper mapper = new ObjectMapper();

    public static final int TITLE_MAX_LENGTH = 30;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public volatile MessageQueueHandler messageQueueHandler;

    public NotesApplication() {
        super("Notes", new ApiVersion(1), Arrays.asList());
        // addToAppContext(ApplicationContextId.IMG,
        // "/static/img/silk/page_link.png");
    }

    @Activate
    public void activate(ComponentContext componentContext) throws ConfigurationException {
        super.activate(componentContext);
        if (messageQueueHandler == null) {
            return;
        }
        messageQueueHandler.addMessageListener("topic://entity.io_skysail_server_app_notes_Note.post", new SkysailMessageListener() {
            @Override
            public void processBody(String text) {
                try {
                    Note noteFromOtherInstallation = mapper.readValue(text, Note.class);
                    noteFromOtherInstallation.setId(null);
                    System.out.println(noteFromOtherInstallation);
                    UpdateEntityFilter<PutEntityServerResource<Note>, Note> updateEntityFilter = new UpdateEntityFilter<>();
                    PutEntityServerResource<Note> resource = new MyPutNoteResource();
                    resource.setApplication(NotesApplication.this);
                    Response response = null;
                    Wrapper<Note> responseWrapper = new ResponseWrapper<>(response);
                    responseWrapper.setEntity(noteFromOtherInstallation);
                    Request request = new Request();
                    resource.init(getContext(), request, response);
                    FilterResult result = updateEntityFilter.doHandle(resource, responseWrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Deactivate
    public void deactivate() {

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

    @Override
    public MessageQueueHandler getMessageQueueHandler() {
        return messageQueueHandler;
    }

}