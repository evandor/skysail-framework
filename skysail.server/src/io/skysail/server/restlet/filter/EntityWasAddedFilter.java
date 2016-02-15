package io.skysail.server.restlet.filter;

import java.io.*;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".saved.success";
        responseWrapper.addInfo(infoMessage);
        
        if (application instanceof MessageQueueProvider) {
            List<MessageQueueHandler> messageQueueHandler = ((MessageQueueProvider) application)
                    .getMessageQueueHandler();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(baos);
                out.writeObject(resource.getCurrentEntity());
                out.close();
                baos.close();
                messageQueueHandler.stream().forEach(mqh -> mqh.send("/topic/event", baos.toString()));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }

//        if (application.getEventAdmin() != null) {
//            new EventHelper(application.getEventAdmin())//
//                    .channel(EventHelper.GUI_MSG)//
//                    .info(infoMessage)//
//                    .fire();
//        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
