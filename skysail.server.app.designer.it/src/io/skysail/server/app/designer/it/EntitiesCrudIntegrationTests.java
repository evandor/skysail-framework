package io.skysail.server.app.designer.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import org.junit.*;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import com.fasterxml.jackson.databind.*;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.it.browser.*;

/**
 * Integration tests for creating, reading, updating, and deleting Entities.
 *
 */
@Ignore

public class EntitiesCrudIntegrationTests extends BrowserTests<EntitiesBrowser, Entity> {

    private Entity entity;

    private ApplicationsBrowser appBrowser;

    private ObjectMapper mapper = new ObjectMapper();
//    private JsonNode node;

    @Before
    public void setUp() {
        int port = determinePort();
        browser = new EntitiesBrowser(MediaType.APPLICATION_JSON, port);
        browser.setUser("admin");

        appBrowser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, port);
        appBrowser.setUser("admin");

        // entity = createRandomEntity();
    }

    @Test
    public void creating_entity_for_application_persists_them() throws Exception {
       // String appId = appBrowser.create(new Application("PropMan"));
        browser.create(new Entity("Campaign"));
        Representation rep = browser.getApplication(browser.getParentEntityBrowser().getId());
        assertThat(rep.getText(), containsString("Campaign"));
    }

    @Test
    // delete
    public void new_entity_can_be_deleted() throws Exception {
//        String appId = appBrowser.create(createRandomApplication());
        Entity theEntity = browser.createRandomEntity();
        browser.create(theEntity);

        Representation application = appBrowser.getApplication(browser.getParentEntityBrowser().getId());
        String strRep = application.getText();
        JsonNode node = mapper.readValue(strRep, JsonNode.class);
        JsonNode firstEntityId = node.get(0).get("entities").get(0).get("id");
       // browser.from(application).get("entities").get("id").first();
        String entityId = firstEntityId.textValue().replace("#", "");
         browser.deleteEntity(browser.getParentEntityBrowser().getId(), entityId);
         
        // assertThat(browser.getEntities().getText(),
        // not(containsString(entity.getName())));
    }

    // @Test // update
    // public void altering_application_updates_it_in_DB() throws Exception {
    // String id = browser.create(entity);
    // assertThat(browser.getApplication(id).getText(),
    // containsString(entity.getName()));
    //
    // entity.setId(id);
    // //entity.setDesc("description changed");
    // entity.setName(entity.getName() + "_changed");
    // browser.updateApplication(entity);
    //
    // String updatedText = browser.getApplication(id).getText();
    // assertThat(updatedText, containsString("_changed"));
    // }
    //
    // @Test
    // @Ignore // not working yet...
    // public void
    // stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg()
    // throws IOException, BundleException {
    // stopAndStartBundle(SkysailServerResource.class);
    // createListAndCheckAssertions();
    // }

    // @Test
    // @Ignore
    // public void posting_new_application_with_name_and_path_persists_it()
    // throws Exception {
    // Application application = new Application("app1");
    // // application.setPath(".");
    // browser.create(application);
    // Representation applications = browser.getEntities();
    // assertThat(applications.getText(), containsString("app1"));
    // }

    // @Test
    // @Ignore
    // public void
    // posting_new_application_with_name_containing_specialChar_yields_validation_violation()
    // throws Exception {
    // thrown.expect(ResourceException.class);
    // thrown.expectMessage("Bad Request");
    // browser.createApplication(new Application("app1!"));
    // }

    // private void createListAndCheckAssertions() throws IOException {
    // browser.create(entity);
    // String html = browser.getEntities().getText();
    // assertThat(html, containsString(entity.getName()));
    // }

    private Application createRandomApplication() {
        Application e = new Application();
        e.setName("Application_" + new BigInteger(130, random).toString(32));
        return e;
    }

    
}
