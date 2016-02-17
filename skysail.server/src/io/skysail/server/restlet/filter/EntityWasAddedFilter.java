package io.skysail.server.restlet.filter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T extends Identifiable>
        extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;
    
    private static ObjectMapper mapper = new ObjectMapper();

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".saved.success";
        responseWrapper.addInfo(infoMessage);

        if (application instanceof MessageQueueProvider) {
            MessageQueueHandler messageQueueHandler = ((MessageQueueProvider) application)
                    .getMessageQueueHandler();
            if (messageQueueHandler != null) {
                Object currentEntity = resource.getCurrentEntity();
                try {
                    String serialized = mapper.writeValueAsString(currentEntity);
                    messageQueueHandler.send("topic://entity." + currentEntity.getClass().getName().replace(".", "_") + ".post", serialized);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // if (application.getEventAdmin() != null) {
        // new EventHelper(application.getEventAdmin())//
        // .channel(EventHelper.GUI_MSG)//
        // .info(infoMessage)//
        // .fire();
        // }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
