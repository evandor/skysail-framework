package io.skysail.product.notes;

import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;

public class NotesRootApplication extends SkysailApplication implements ApplicationProvider {

    public NotesRootApplication() {
        super("notes", new ApiVersion(1));
    }
    
    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

    @Override
    protected void attach() {
        router.setApiVersion(null);
        router.attach(new RouteBuilder("", PublicResource.class).noAuthenticationNeeded());
    }
}

