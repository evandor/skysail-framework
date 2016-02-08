package io.skysail.product.designer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.*;

@Component(immediate = true)
public class DesignerRootApplication extends SkysailApplication implements ApplicationProvider {

    public DesignerRootApplication() {
        super("designer", new ApiVersion(1));
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
