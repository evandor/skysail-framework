package io.skysail.server.app.designer.it.browser;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;

import java.math.BigInteger;

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

    public Application createRandomApplication() {
        return new Application("App_" + new BigInteger(130, random).toString(32), "pkgName", "../", "projectName");
    }

    public void create() {
        create(createRandomApplication());
    }

    public void create(Application application) {
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

    public void deleteApplication(String id) {
        log.info("{}deleting Application #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteApplication(client, id);
    }

    public Representation getApplication(String id) {
        log.info("{}retrieving Application #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplication(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateApplication(Application entity) {
        log.info("{}updating Application #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateApplication(client, entity);
    }

    private void deleteApplication(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createApplication(ApplicationClient<Application> client, Application Application) {
        navigateToPostApplication(client);
        client.post(createForm(Application));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostApplication(ApplicationClient<Application> client) {
        client.gotoAppRoot()
        // .followLinkTitleAndRefId("List of Applications", listId)
        // .followLinkRelation(LinkRelation.CREATE_FORM);
                .followLinkTitle("Create new Application");
    }

    private void getApplications(ApplicationClient<Application> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME);
    }

    private void updateApplication(ApplicationClient<Application> client, Application entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}