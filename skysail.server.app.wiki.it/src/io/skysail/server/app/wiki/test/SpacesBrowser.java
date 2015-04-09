package io.skysail.server.app.wiki.test;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;

import org.restlet.data.Form;
import org.restlet.data.MediaType;

public class SpacesBrowser extends ApplicationBrowser<SpacesBrowser, Space> {

    public SpacesBrowser(MediaType mediaType) {
        super(WikiApplication.APP_NAME, mediaType);
    }
    
    @Override
    protected Form createForm(Space entity) {
        Form form = new Form();
        form.add("name", "entity.getName()");
        return form;
    }

}
