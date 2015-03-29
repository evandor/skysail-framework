package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class PostRedirectGetFilter<R extends PostEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            Response response = responseWrapper.getResponse();
            //response.redirectSeeOther(redirectTo);
            // It seems I don't really want a 303 to a different url (usually the list-entities-resource)
            // here: It swallows the Location header, which should be used in our 201 response to indicate
            // the location of the new resource. Still, I want a browser (but not another kind of user-agent)
            // to display a new page after the POST, as it is more convenient for the user. More concrete,
            // after POSTing a new entity, I want the entity to be created, get a 201 response with the location
            // of the new resource, and then, in case of a browser user agent, I want to see the list of entities
            // right away, without having to click another button.
            // So, we set a refreh header here instead of creating a 303 redirect response.
            // TODO do this in PutEntityResource as well.
            //response.getHeaders().add("Refresh", "http://www.heise.de");
            resource.setMetaRefreshTarget("http://www.heise.de");
        }
    }
}
