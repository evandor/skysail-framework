package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.List;

import org.junit.Test;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class PutApplicationResourceTest extends AbstractApplicationResourceTest {

    @Test
    public void empty_form_data_yields_validation_failure() {
        Application application = createValidApplication();

        form.add("name", "");
        //form.add("id", application.getId());
        setAttributes("id", application.getId());
        init(putApplicationResource);

        SkysailResponse<Application> skysailResponse = putApplicationResource.put(form, HTML_VARIANT);

        assertValidationFailure(putApplicationResource, skysailResponse);
    }

    @Test
    public void empty_json_data_yields_validation_failure() {

        init(putApplicationResource);

        Application updatedList = new Application();
        SkysailResponse<Application> skysailResponse = putApplicationResource.put(updatedList, JSON_VARIANT);

        assertValidationFailure(putApplicationResource, skysailResponse);
    }

    @Test
    public void list_can_be_updated() {
        Application app = createValidApplication();
        form.add("name", "application3a");
        form.add("id", app.getId());
        form.add("path", "../");
        form.add("packageName", "io.skysail.app.test");
        form.add("projectName", "testproj");
        putApplicationResource.getRequestAttributes().put("id", app.getId());
        setAttributes("id", app.getId());
        init(putApplicationResource);

        putApplicationResource.put(form, HTML_VARIANT);

        List<OrientVertex> vertexById2 = (List<OrientVertex>) new DesignerRepository().getVertexById(Application.class, app.getId());
        OrientVertex vertexById = vertexById2.get(0);

        //assertThat(vertexById.getProperty("modified"), is(not(nullValue())));
        //assertThat(vertexById.getProperty("created"), is(not(nullValue())));
        assertThat(vertexById.getProperty("name"), is(equalTo("application3a")));
    }


}
