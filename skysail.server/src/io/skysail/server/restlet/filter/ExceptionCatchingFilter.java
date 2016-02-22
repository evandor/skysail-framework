package io.skysail.server.restlet.filter;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.filter.helper.ExceptionCatchingFilterHelper;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public ExceptionCatchingFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, responseWrapper);
        } catch (ResourceException re) {
            throw re;
        } catch (Exception e) {
            ExceptionCatchingFilterHelper.handleError(e, application, responseWrapper, resource.getClass());
        }
        return FilterResult.CONTINUE;
    }

//    @Override
//    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
//        String redirectTo = resource.redirectTo();
//        Parameter noRedirects = resource.getQuery().getFirst(SkysailServerResource.NO_REDIRECTS);
//        if (redirectTo != null && noRedirects == null) {
//            Variant variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
//            if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
//                redirectTo = augmentWithMessageIds(redirectTo, responseWrapper.getMessageIds());
//                resource.getResponse().redirectSeeOther(redirectTo);
//            }
//        } else {
//            List<Long> messageIds = responseWrapper.getMessageIds();
//            System.out.println(messageIds);
//        }
//    }
//
//    private String augmentWithMessageIds(String redirectTo, List<Long> messageIds) {
//        if (messageIds.isEmpty()) {
//            return redirectTo;
//        }
//        String result;
//        if (redirectTo.contains("?")) {
//            result = redirectTo + "&";
//        } else {
//            result = redirectTo + "?";
//        }
//        return result + "msgIds=" + messageIds.stream().map(id -> id.toString()).collect(Collectors.joining("|"));
//    }
}
