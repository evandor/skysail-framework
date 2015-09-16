package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.spaces.Space;

import org.junit.Test;


public class PutSpaceResourceTest extends AbstractSpaceResourceTest {

    @Test
    public void empty_form_data_yields_validation_failure() {
        Space aSpace = createSpace();

        form.add("name", "");
        form.add("id", aSpace.getId());
        setAttributes("id", aSpace.getId());
        init(putSpaceResource);

        SkysailResponse<Space> skysailResponse = putSpaceResource.put(form, HTML_VARIANT);

        assertSingleValidationFailure(putSpaceResource, skysailResponse,  "name", "size must be between");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {

        init(putSpaceResource);

        Space updatedSpace = new Space();
        SkysailResponse<Space> skysailResponse = putSpaceResource.putEntity(updatedSpace, JSON_VARIANT);

        assertSingleValidationFailure(putSpaceResource, skysailResponse,  "name", "may not be null");
    }

    @Test
    public void entities_editable_fields_can_be_updated_via_htmlform() {
        Space aSpace = createSpace();

        form.add("name", "updated_space");
        form.add("owner", "readonly");
        setAttributes("id", aSpace.getId());
        init(putSpaceResource);

        SkysailResponse<Space> response = putSpaceResource.put(form, HTML_VARIANT);

        assertThat(response.getEntity().getName(),is("updated_space"));
        assertThat(response.getEntity().getOwner(),is("admin"));
    }

    @Test
    public void entities_editable_fields_can_be_updated_via_json() {
        Space aSpace = createSpace();

        Space entityToPut = new Space("updated_space_json");
        entityToPut.setId(aSpace.getId());
        setAttributes("id", aSpace.getId());
        init(putSpaceResource);

        SkysailResponse<Space> response = putSpaceResource.putEntity(entityToPut, JSON_VARIANT);

        assertThat(response.getEntity().getName(),is("updated_space_json"));
        assertThat(response.getEntity().getOwner(),is("admin"));
    }

}
