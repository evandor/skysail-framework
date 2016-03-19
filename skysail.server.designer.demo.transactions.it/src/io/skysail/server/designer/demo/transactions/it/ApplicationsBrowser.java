package io.skysail.server.designer.demo.transactions.it;

import java.awt.PageAttributes.MediaType;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.designer.demo.transactions.TransactionsApplication;
import io.skysail.server.designer.demo.transactions.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationsBrowser extends ApplicationBrowser<ApplicationsBrowser, Transaction> {

    public ApplicationsBrowser(MediaType mediaType, int port) {
        super(TransactionsApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(Transaction transaction) {
        Form form = new Form();
        //form.add("name", transaction.getName());
        return form;
    }

    public Transaction createRandomApplication() {
        return new Transaction();//"App_" + new BigInteger(130, random).toString(32), "pkgName", "../", "projectName");
    }

    public void create() {
        create(createRandomApplication());
    }

    public void create(Transaction application) {
        log.info("{}creating new Transaction {}", ApplicationClient.TESTTAG, application);
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
        log.info("{}deleting Transaction #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteApplication(client, id);
    }

    public Representation getApplication(String id) {
        log.info("{}retrieving Transaction #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplication(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateApplication(Transaction entity) {
        log.info("{}updating Transaction #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateApplication(client, entity);
    }

    private void deleteApplication(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createApplication(ApplicationClient<Transaction> client, Transaction Application) {
        navigateToPostApplication(client);
        client.post(createForm(Application));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostApplication(ApplicationClient<Transaction> client) {
        client.gotoAppRoot()
        // .followLinkTitleAndRefId("List of Applications", listId)
        // .followLinkRelation(LinkRelation.CREATE_FORM);
                .followLinkTitle("Create new Transaction");
    }

    private void getApplications(ApplicationClient<Transaction> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(TransactionsApplication.APP_NAME);
    }

    private void updateApplication(ApplicationClient<Transaction> client, Transaction entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
