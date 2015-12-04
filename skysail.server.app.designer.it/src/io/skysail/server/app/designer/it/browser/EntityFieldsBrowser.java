package io.skysail.server.app.designer.it.browser;

import java.math.BigInteger;

import org.restlet.data.*;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.fields.EntityField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityFieldsBrowser extends ApplicationBrowser<EntityFieldsBrowser, EntityField> {

    public EntityFieldsBrowser(MediaType mediaType, int port) {
        super(DesignerApplication.APP_NAME, mediaType, port);
        parentEntityBrowser = new EntitiesBrowser(MediaType.APPLICATION_JSON, port);
    }

    protected Form createForm(EntityField entity) {
        Form form = new Form();
        form.add("name", entity.getName());
        return form;
    }
    
    public EntityField createRandomField() {
        EntityField e = new EntityField();
        e.setName("EntityField_" + new BigInteger(130, random).toString(32));
        return e;
    }

    public void create(EntityField field) {
        ((EntitiesBrowser)parentEntityBrowser).create();
        log.info("{}creating new Field {}", ApplicationClient.TESTTAG, field);
        login();
        create(client, field);
    }

    public Representation getApplication(String appId) {
        return ((EntitiesBrowser)parentEntityBrowser).getApplication(appId);
    }
    
    

//    public Representation getEntities() {
//        log.info("{}getting Applications", ApplicationClient.TESTTAG);
//        login();
//        getEntities(client);
//        return client.getCurrentRepresentation();
//    }
//
//    public void deleteEntity(String appId, String entityId) {
//        log.info("{}deleting Entity #{}", ApplicationClient.TESTTAG, entityId);
//        login();
//        deleteEntity(client, appId, entityId);
//    }
//
//    public Representation getApplication(String id) {
//        log.info("{}retrieving Application #{}", ApplicationClient.TESTTAG, id);
//        login();
//        getApplication(client, id);
//        return client.getCurrentRepresentation();
//    }
//
//    public void updateApplication(Entity entity) {
//        log.info("{}updating Entity #{}", ApplicationClient.TESTTAG, entity.getId());
//        login();
//        updateEntity(client, entity);
//    }
//
//    private void deleteEntity(ApplicationClient<Entity>client, String appId, String entityId) {
//        client.gotoAppRoot() //
//                .followLinkTitleAndRefId("list Entities", appId)
//                .followLinkTitleAndRefId("update", entityId)
//                .followLink(Method.DELETE, null);
//    }
//
    private void create(ApplicationClient<EntityField> client, EntityField field) {
        navigateToPostEntity(client);
        client.post(createForm(field));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntity(ApplicationClient<EntityField> client) {
        String appId = parentEntityBrowser.getParentEntityBrowser().getId();
        String entityId = parentEntityBrowser.getId();
        client.gotoAppRoot()
            .followLinkTitleAndRefId("list Entities", appId)
            .followLinkTitleAndRefId("create new EntityField", entityId);
                //.followLinkRelation(LinkRelation.CREATE_FORM);
    }

//    private void getEntities(ApplicationClient<Entity> client) {
//        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
//                             // id);
//    }
//
//    private void getApplication(ApplicationClient<?> client, String id) {
//        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME);
//    }
//
//    private void updateEntity(ApplicationClient<Entity> client, Entity entity) {
//        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
//    }
//

}
