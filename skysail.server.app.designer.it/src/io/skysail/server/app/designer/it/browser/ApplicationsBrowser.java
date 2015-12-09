package io.skysail.server.app.designer.it.browser;

import java.math.BigInteger;

import org.restlet.data.*;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationsBrowser extends ApplicationBrowser<ApplicationsBrowser, DbApplication> {

    public ApplicationsBrowser(MediaType mediaType, int port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(DbApplication application) {
        Form form = new Form();
        form.add("name", application.getName());
        return form;
    }

    public DbApplication createRandomApplication() {
        return new DbApplication("App_" + new BigInteger(130, random).toString(32), "pkgName", "../", "projectName");
    }

    public void create() {
        create(createRandomApplication());
    }

    public void create(DbApplication application) {
        log.info("{}creating new DbApplication {}", ApplicationClient.TESTTAG, application);
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
        log.info("{}deleting DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteApplication(client, id);
    }

    public Representation getApplication(String id) {
        log.info("{}retrieving DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplication(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateApplication(DbApplication entity) {
        log.info("{}updating DbApplication #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateApplication(client, entity);
    }

    private void deleteApplication(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createApplication(ApplicationClient<DbApplication> client, DbApplication Application) {
        navigateToPostApplication(client);
        client.post(createForm(Application));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostApplication(ApplicationClient<DbApplication> client) {
        client.gotoAppRoot()
        // .followLinkTitleAndRefId("List of Applications", listId)
        // .followLinkRelation(LinkRelation.CREATE_FORM);
                .followLinkTitle("Create new DbApplication");
    }

    private void getApplications(ApplicationClient<DbApplication> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME);
    }

    private void updateApplication(ApplicationClient<DbApplication> client, DbApplication entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
