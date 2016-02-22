package io.skysail.server.restlet.filter.helper;

import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.server.app.SkysailApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilterHelper {
    
    private ExceptionCatchingFilterHelper() {
    }

    public static void handleError(Exception e, SkysailApplication application, Wrapper<?> responseWrapper, Class<?> cls) {
        log.error(e.getMessage(), e);

        String genericErrorMessageForGui = cls.getSimpleName() + ".saved.failure";
        responseWrapper.addError(genericErrorMessageForGui);
        
        Response response = responseWrapper.getResponse();
        response.setStatus(Status.SERVER_ERROR_INTERNAL);

        responseWrapper.addInfo(e.getMessage());
        
        if (application == null) {
            return;
        }

//        EventAdmin eventAdmin = application.getEventAdmin() != null ? application.getEventAdmin() : null;
//        if (eventAdmin != null) {
//            new EventHelper(eventAdmin)//
//                    .channel(EventHelper.GUI_MSG)//
//                    .error(genericErrorMessageForGui)//
//                    .fire();
//        }
    }

}
