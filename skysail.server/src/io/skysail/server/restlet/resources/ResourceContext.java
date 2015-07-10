package io.skysail.server.restlet.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.LinkUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "application")
public class ResourceContext {

    private List<WebComponentCall> navItems = new ArrayList<>();
    private SkysailApplication application;
    private SkysailServerResource<?> skysailServerResource;
    
    public ResourceContext(SkysailApplication application, SkysailServerResource<?> skysailServerResource) {
        this.application = application;
        this.skysailServerResource = skysailServerResource;
    }

    public void addAjaxNavigation(String title, Class<? extends SkysailServerResource<?>> cls, Class<? extends SkysailServerResource<?>> targetClass, String identifier) {
        Link ajaxTarget = LinkUtils.fromResource(application, cls);
        Link linkTarget = LinkUtils.fromResource(application, targetClass);
        
        WebComponentCall call = WebComponentCall.builder()
                .type("sky-ajax-get")
                .title(title)
                .identifier(identifier)
                .requestUrl(skysailServerResource.getOriginalRef().getPath().toString())
                .url(ajaxTarget.getUri())
                .linkTo(linkTarget.getUri())
                .build();
        navItems.add(call);
    }

    public void addDisabledAjaxNavigation(String title, Class<? extends SkysailServerResource<?>> cls) {
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
}
