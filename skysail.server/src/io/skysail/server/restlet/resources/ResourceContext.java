package io.skysail.server.restlet.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.WebComponentCall.WebComponentCallBuilder;
import io.skysail.server.utils.LinkUtils;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString(of = "application")
@Slf4j
public class ResourceContext {

    private List<WebComponentCall> navItems = new ArrayList<>();
    private SkysailApplication application;
    private SkysailServerResource<?> skysailServerResource;

    public ResourceContext(SkysailApplication application, SkysailServerResource<?> skysailServerResource) {
        this.application = application;
        this.skysailServerResource = skysailServerResource;
    }

    public void addAjaxNavigation(String id, String title, Class<? extends SkysailServerResource<?>> cls, Class<? extends SkysailServerResource<?>> targetClass, String identifier) {
        if (application == null) {
            log.warn("no application available for ResourceContext#addAjaxNavigation");
            return;
        }

        Link ajaxTarget = LinkUtils.fromResource(application, cls);
        Link linkTarget = LinkUtils.fromResource(application, targetClass);

        WebComponentCall call = WebComponentCall.builder()
                .type("sky-ajax-get")
                .id(id)
                .title(title)
                .identifier(identifier)
                .requestUrl(skysailServerResource.getOriginalRef().getPath().toString())
                .url(ajaxTarget.getUri())
                .linkTo(linkTarget.getUri())
                .build();
        navItems.add(call);
    }

    public void addDisabledAjaxNavigation(String title, Class<? extends SkysailServerResource<?>> cls) {
        if (application == null) {
            log.warn("no application available for ResourceContext#addAjaxNavigation");
            return;
        }

        Link ajaxTarget = LinkUtils.fromResource(application, cls);

        WebComponentCall call = WebComponentCall.builder()
                .type("sky-ajax-get")
                .title(title)
                .requestUrl(skysailServerResource.getOriginalRef().getPath().toString())
                .url(ajaxTarget.getUri())
                .disabled(true)
                .build();
        navItems.add(call);
    }

    public WebComponentCallBuilder getAjaxBuilder(String title, Class<? extends SkysailServerResource<?>> cls, Class<? extends SkysailServerResource<?>> targetClass) {
        if (application == null) {
            log.warn("no application available for ResourceContext#addAjaxNavigation");
            return null;
        }

        Link ajaxTarget = LinkUtils.fromResource(application, cls);
        Link linkTarget = LinkUtils.fromResource(application, targetClass);

        return WebComponentCall.builder()
                .type("sky-ajax-get")
                .url(ajaxTarget.getUri())
                .linkTo(linkTarget.getUri())
                .glyphicon("th-list")
                .title(title)
                .requestUrl(skysailServerResource.getOriginalRef().getPath().toString());
    }

    public void addAjaxNavigation(WebComponentCall call) {
        navItems.add(call);
    }
}
