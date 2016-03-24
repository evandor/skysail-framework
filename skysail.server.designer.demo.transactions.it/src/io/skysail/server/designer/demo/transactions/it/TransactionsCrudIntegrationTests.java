package io.skysail.server.designer.demo.transactions.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.designer.demo.transactions.transaction.Transaction;

public class TransactionsCrudIntegrationTests extends BrowserTests<TransactionsBrowser, Transaction> {

    private Transaction entity;

    @Before
    public void setUp() {
        browser = new TransactionsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entity = browser.createRandomTransaction();
    }

    @Test  // create and read
    public void creating_new_application_will_persist_it() throws IOException {
        browser.create(entity);
        String html = browser.getTransactions().getText();
        assertThat(html, containsString(entity.getDescription()));
    }

    @Test // delete
    public void new_application_can_be_deleted() throws IOException {
        browser.create(entity);
        browser.deleteTransaction(browser.getId());
        assertThat(browser.getTransactions().getText(), not(containsString(entity.getDescription())));
    }

}
