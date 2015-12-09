package io.skysail.server.app.designer.it.browser;

import java.math.BigInteger;

import org.restlet.data.*;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntitiesBrowser extends ApplicationBrowser<EntitiesBrowser, DbEntity> {

    public EntitiesBrowser(MediaType mediaType, int port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
        parentEntityBrowser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, port);
    }

    protected Form createForm(DbEntity entity) {
        Form form = new Form();
        form.add("name", entity.getName());
        return form;
    }

    public DbEntity createRandomEntity() {
        DbEntity e = new DbEntity();
        e.setName("Entity_" + new BigInteger(130, random).toString(32));
        return e;
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(DbEntity entity) {
        ((ApplicationsBrowser) parentEntityBrowser).create();
        log.info("{}creating new DbEntity {}", ApplicationClient.TESTTAG, entity);
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
        log.info("{}deleting DbEntity #{}", ApplicationClient.TESTTAG, entityId);
        login();
        deleteEntity(client, appId, entityId);
    }

    public Representation getApplication(String id) {
        log.info("{}retrieving DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplication(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateApplication(DbEntity entity) {
        log.info("{}updating DbEntity #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateEntity(client, entity);
    }

    private void deleteEntity(ApplicationClient<DbEntity> client, String appId, String entityId) {
        client.gotoAppRoot()
                //
                .followLinkTitleAndRefId("list Entities", appId).followLinkTitleAndRefId("update", entityId)
                .followLink(Method.DELETE, null);
    }

    private void create(ApplicationClient<DbEntity> client, DbEntity entity) {
        navigateToPostEntity(client);
        client.post(createForm(entity));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntity(ApplicationClient<DbEntity> client) {
        client.gotoAppRoot().followLinkTitleAndRefId("Create new DbEntity", parentEntityBrowser.getId());
    }

    private void getEntities(ApplicationClient<DbEntity> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME);
    }

    private void updateEntity(ApplicationClient<DbEntity> client, DbEntity entity) {
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
