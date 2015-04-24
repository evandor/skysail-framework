package io.skysail.server.app.designer.it;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Representation;

@Slf4j
public class ApplicationsBrowser extends ApplicationBrowser<ApplicationsBrowser, Application> {

    public ApplicationsBrowser(MediaType mediaType) {
        super(DesignerApplication.APP_NAME, mediaType, "2014");
    }

    protected Form createForm(Application application) {
        Form form = new Form();
        form.add("name", application.getName());
        return form;
    }

    public void createApplication(String listId, Application application) {
        log.info("{}creating new Application {} in List {}", ApplicationClient.TESTTAG, application, listId);
        login();
        createApplication(client, listId, application);
    }

    public Representation getApplicationsForList(String id) {
        log.info("{}getting Applications for List #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplicationsForList(client, id);
        return client.getCurrentRepresentation();
    }

    private String createApplication(ApplicationClient<Application> client, String listId, Application Application) {
        navigateToPostApplication(client, listId);
        client.post(createForm(Application));
        return client.getLocation().getLastSegment(true);
    }

    private void navigateToPostApplication(ApplicationClient<Application> client, String listId) {
        client.gotoAppRoot().followLinkTitleAndRefId("List of Applications", listId)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getApplicationsForList(ApplicationClient<Application> client, String id) {
        client.gotoAppRoot().followLinkTitleAndRefId("List of Applications", id);
    }

}
