package io.skysail.server.app.designer.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.it.browser.*;

import java.math.BigInteger;

import org.junit.*;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting Entities.
 *
 */
@Ignore
public class FieldsCrudIntegrationTests extends IntegrationTests<EntityFieldsBrowser, EntityField> {

    private EntityField field;

    private ApplicationsBrowser appBrowser;


    @Before
    public void setUp() {
        String port = determinePort();
        browser = new EntityFieldsBrowser(MediaType.APPLICATION_JSON, port);
        browser.setUser("admin");
        field = browser.createRandomField();
    }

    @Test
    public void creating_field_for_entity_persists_them() throws Exception {
        browser.create(field);
        String appId = browser.getParentEntityBrowser().getParentEntityBrowser().getId();
        String rep = browser.getApplication(appId).getText();
        System.out.println(rep);
        assertThat(rep, containsString(field.getName()));
    }

//    @Test
//    // delete
//    public void new_entity_can_be_deleted() throws Exception {
//        String appId = appBrowser.create(createRandomApplication());
//        Entity theEntity = createRandomEntity();
//        browser.create(appId, theEntity);
//
//        Representation application = appBrowser.getApplication(appId);
//        String strRep = application.getText();
//        JsonNode node = mapper.readValue(strRep, JsonNode.class);
//        JsonNode firstEntityId = node.get(0).get("entities").get(0).get("id");
//       // browser.from(application).get("entities").get("id").first();
//        String entityId = firstEntityId.textValue().replace("#", "");
//         browser.deleteEntity(appId, entityId);
//         
//        // assertThat(browser.getEntities().getText(),
//        // not(containsString(field.getName())));
//    }

    // @Test // update
    // public void altering_application_updates_it_in_DB() throws Exception {
    // String id = browser.create(field);
    // assertThat(browser.getApplication(id).getText(),
    // containsString(field.getName()));
    //
    // field.setId(id);
    // //field.setDesc("description changed");
    // field.setName(field.getName() + "_changed");
    // browser.updateApplication(field);
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
    // browser.create(field);
    // String html = browser.getEntities().getText();
    // assertThat(html, containsString(field.getName()));
    // }

    private Application createRandomApplication() {
        Application e = new Application();
        e.setName("Application_" + new BigInteger(130, random).toString(32));
        return e;
    }

    
}
