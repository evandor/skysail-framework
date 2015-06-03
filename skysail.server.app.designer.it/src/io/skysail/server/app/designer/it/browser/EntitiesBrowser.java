package io.skysail.server.app.designer.it.browser;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

@Slf4j
public class EntitiesBrowser extends ApplicationBrowser<EntitiesBrowser, Entity> {

    public EntitiesBrowser(MediaType mediaType, String port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(Entity entity) {
        Form form = new Form();
        form.add("name", entity.getName());
        return form;
    }

    public String create(String appId, Entity entity) {
        log.info("{}creating new Entity {}", ApplicationClient.TESTTAG, entity);
        login();
        return create(client, appId, entity);
    }

    public Representation getEntities() {
        log.info("{}getting Applications", ApplicationClient.TESTTAG);
        login();
        getEntities(client);
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

    public void updateApplication(Entity entity) {
        log.info("{}updating Entity #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateEntity(client, entity);
    }

    private void deleteApplication(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private String create(ApplicationClient<Entity> client, String appId, Entity entity) {
        navigateToPostEntity(client, appId);
        client.post(createForm(entity));
        return null;//client.getLocation().getLastSegment(true);
    }

    private void navigateToPostEntity(ApplicationClient<Entity> client, String appId) {
        client.gotoAppRoot().followLinkTitleAndRefId("Create new Entity", appId);
                //.followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getEntities(ApplicationClient<Entity> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME);
    }

    private void updateEntity(ApplicationClient<Entity> client, Entity entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
