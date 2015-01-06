package de.twenty11.skysail.server.events;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.service.ConverterService;
import org.slf4j.Logger;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.api.config.ConfigurationProvider;
import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.core.osgi.internal.EventHelper;
import de.twenty11.skysail.server.services.RestletServicesProvider;

//@Component(immediate = true, properties = { "event.topics=couchdb/*" })
public class CouchDbFeeder implements EventHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CouchDbFeeder.class);

    private volatile RestletServicesProvider restletServicesProvider;

    private volatile ConfigurationProvider configurationProvider;

    @Override
    public void handleEvent(Event event) {
        String coubdDbUrl = configurationProvider.getConfigForKey(Constants.COUCHDB_URL);
        if (coubdDbUrl == null) {
            log.debug("NO COUCHDB configured!");
            return;
        }
        // String path = (String) event.getProperty(EventHelper.EVENT_PROPERTY_PATH);
        Object entity = event.getProperty(EventHelper.EVENT_PROPERTY_ENTITY);
        ConverterService converterService = restletServicesProvider.getConverterSerivce();

        Representation result = converterService.toRepresentation(entity, new Variant(
                org.restlet.data.MediaType.APPLICATION_JSON), null);
        try {
            Request.Post(coubdDbUrl).bodyString(result.getText(), ContentType.APPLICATION_JSON).execute()
                    .returnContent();
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    @Reference
    public void setRestletServicesProvider(RestletServicesProvider rsp) {
        this.restletServicesProvider = rsp;
    }

    @Reference
    public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

}
