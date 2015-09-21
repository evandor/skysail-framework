package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class CheckUserIsLoggedInFilter<R extends SkysailServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, Wrapper responseWrapper) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return FilterResult.CONTINUE;
        }
        Response response = responseWrapper.getResponse();
        response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, "user is not logged in");
        return FilterResult.STOP;
    }

}
