package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;

public class EntityResourceTest extends AbstractEntityResourceTest {

    private DbApplication anApplication;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        anApplication = createApplication();
        setAttributes("id", anApplication.getId());
    }
    
    @Test
    public void created_entity_can_be_retrieved() {
        String entityId = createAndAddEntity();
    
        addAttribute("eid", entityId);
        init(entityResource);
        DbEntity entity = entityResource.getEntity();
        
        assertThat(entity.getId(),is(notNullValue()));
    }

    @Test
    public void created_entity_can_be_deleted_again() {
        String entityId = createAndAddEntity();
        addAttribute("eid", entityId);
        init(entityResource);
        entityResource.eraseEntity();
        
        init(entityResource);
        assertThat(entityResource.getEntity(), is(nullValue()));
    }
    
    private String createAndAddEntity() {
        DbEntity dbEntity = new DbEntity("TheEntity");
        postEntityResource.post(dbEntity, HTML_VARIANT);
        setAttributes("id", anApplication.getId());
        init(applicationResource);
        DbApplication dbApplication = applicationResource.getEntity();
        String entityId = dbApplication.getEntities().get(0).getId();
        return entityId;
    }


}
