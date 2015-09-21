package io.skysail.server.restlet.filter.helper;

import io.skysail.server.app.SkysailApplication;
import lombok.extern.slf4j.Slf4j;

import org.osgi.service.event.EventAdmin;
import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.Wrapper;

@Slf4j
public class ExceptionCatchingFilterHelper {

    public static void handleError(Exception e, SkysailApplication application, Wrapper responseWrapper, Class<?> cls) {
        log.error(e.getMessage(), e);
        Response response = responseWrapper.getResponse();
        response.setStatus(Status.SERVER_ERROR_INTERNAL);

        if (application == null) {
            return;
        }

        EventAdmin eventAdmin = application.getEventAdmin() != null ? application.getEventAdmin().get() : null;
        if (eventAdmin != null) {
            new EventHelper(eventAdmin)//
                    .channel(EventHelper.GUI_MSG)//
                    .error(cls.getSimpleName() + ".saved.failure")//
                    .fire();
        }   }

}
