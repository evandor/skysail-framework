package de.twenty11.skysail.server.core.restlet.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class CheckUserIsLoggedInFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return FilterResult.CONTINUE;
        }
        Response response = responseWrapper.getResponse();
        response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, "user is not logged in");
        return FilterResult.STOP;
    }

}
