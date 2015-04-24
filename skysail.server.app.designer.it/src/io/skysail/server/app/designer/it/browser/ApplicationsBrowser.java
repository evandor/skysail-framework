package io.skysail.server.app.designer.it.browser;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Representation;

@Slf4j
public class ApplicationsBrowser extends ApplicationBrowser<ApplicationsBrowser, Application> {

    public ApplicationsBrowser(MediaType mediaType, String port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(Application application) {
        Form form = new Form();
        form.add("name", application.getName());
        return form;
    }

    public void createApplication(Application application) {
        log.info("{}creating new Application {}", ApplicationClient.TESTTAG, application);
        login();
        createApplication(client, application);
    }

    public Representation getApplications() {
        log.info("{}getting Applications", ApplicationClient.TESTTAG);
        login();
        getApplications(client);
        return client.getCurrentRepresentation();
    }

    private String createApplication(ApplicationClient<Application> client, Application Application) {
        navigateToPostApplication(client);
        client.post(createForm(Application));
        return client.getLocation().getLastSegment(true);
    }

    private void navigateToPostApplication(ApplicationClient<Application> client) {
        client.gotoAppRoot()
            //.followLinkTitleAndRefId("List of Applications", listId)
            .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getApplications(ApplicationClient<Application> client) {
        client.gotoAppRoot();//.followLinkTitleAndRefId("List of Applications", id);
    }

}
