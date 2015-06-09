package io.skysail.server.app.designer.it.browser;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Representation;

@Slf4j
public class EntitiesBrowser extends ApplicationBrowser<EntitiesBrowser, Entity> {

    public EntitiesBrowser(MediaType mediaType, String port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
        parentEntityBrowser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, port);
    }

    protected Form createForm(Entity entity) {
        Form form = new Form();
        form.add("name", entity.getName());
        return form;
    }

    public Entity createRandomEntity() {
        Entity e = new Entity();
        e.setName("Entity_" + new BigInteger(130, random).toString(32));
        return e;
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(Entity entity) {
        ((ApplicationsBrowser) parentEntityBrowser).create();
        log.info("{}creating new Entity {}", ApplicationClient.TESTTAG, entity);
        login();
        create(client, entity);
    }

    public Representation getEntities() {
        log.info("{}getting Applications", ApplicationClient.TESTTAG);
        login();
        getEntities(client);
        return client.getCurrentRepresentation();
    }

    public void deleteEntity(String appId, String entityId) {
        log.info("{}deleting Entity #{}", ApplicationClient.TESTTAG, entityId);
        login();
        deleteEntity(client, appId, entityId);
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

    private void deleteEntity(ApplicationClient<Entity> client, String appId, String entityId) {
        client.gotoAppRoot()
                //
                .followLinkTitleAndRefId("list Entities", appId).followLinkTitleAndRefId("update", entityId)
                .followLink(Method.DELETE, null);
    }

    private void create(ApplicationClient<Entity> client, Entity entity) {
        navigateToPostEntity(client);
        client.post(createForm(entity));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntity(ApplicationClient<Entity> client) {
        client.gotoAppRoot().followLinkTitleAndRefId("Create new Entity", parentEntityBrowser.getId());
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

    // public EntitiesBrowser from(Representation representation) {
    // try {
    // String strRep = representation.getText();
    // node = mapper.readValue(strRep, JsonNode.class);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return this;
    // }
    //
    // public Object get(String identifier) {
    // Iterable<JsonNode> iterable = () -> node.iterator();
    // Stream<JsonNode> stream = StreamSupport.stream(iterable.spliterator(),
    // false);
    // stream.filter(node -> {
    // return node.get(identifier) != null;
    // }).map
    // return null;
    // }

}
