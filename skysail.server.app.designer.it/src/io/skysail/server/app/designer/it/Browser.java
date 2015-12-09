package io.skysail.server.app.designer.it;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

@Slf4j
public class Browser {

    private ApplicationClient client;
    
    public Browser(String url) {
        this(url, MediaType.TEXT_HTML);
    }
    
    public Browser(String url, MediaType mediaType) {
        log.info("{}creating new browser client with url '{}' and mediaType '{}'", ApplicationClient.TESTTAG, url, MediaType.TEXT_HTML);
        client = new ApplicationClient(url, DesignerApplication.APP_NAME, mediaType);
    }
    
    public Browser asUser(String username) {
    	log.info("{}logging in as user '{}'", ApplicationClient.TESTTAG, username);
		client.loginAs(username, "skysail");
		return this;
	}
    
    public Representation getApplications() {
    	log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
        getApplications(client);
        return client.getCurrentRepresentation();
    }
//
//    public Representation getTodoList(String id) {
//        log.info("{}retrieving TodoList #{}", ApplicationClient.TESTTAG, id);
//        getTodoList(client, id);
//        return client.getCurrentRepresentation();
//    }
    
    public Reference createApplication(DbApplication application) {
        navigateToPostApplicationAs(client);
        client.post(createForm(application));
        return client.getLocation();
    }

    private Form createForm(DbApplication application) {
        Form form = new Form();
        form.add("name", application.getName());
        return form;
    }

    private void getApplications(ApplicationClient client) {
    	client.gotoRoot();
        client.followLinkTitle(DesignerApplication.APP_NAME);
    }
//    
//    private void getTodoList(ApplicationClient client, String id) {
//        client.gotoRoot();
//        client.followLinkTitle(TodoApplication.APP_NAME);
//    }
//
    private void navigateToPostApplicationAs(ApplicationClient client) {
    	client.gotoRoot();
        client.followLinkTitle(DesignerApplication.APP_NAME)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }


    public Status getStatus() {
        return client.getResponse().getStatus();
    }

    public void deleteApplication(String id) {
        client.gotoRoot().followLinkTitle(DesignerApplication.APP_NAME).followLinkTitle("update");
        
    }

   


	

}
