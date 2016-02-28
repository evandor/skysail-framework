package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;

public class SkysailResponseTest {

    @Test
    public void object_is_initialized_with_response_and_entity() {
        SkysailResponse<String> response = new SkysailResponse<String>(null, "entity"){};
        assertThat(response.getEntity(),is(equalTo("entity")));
        assertThat(response.isForm(),is(false));
    }
    
    @Test
    public void isForm() {
        assertThat(new SkysailResponse<String>(null, "entity").isForm(),is(false));
        assertThat(new FormResponse<String>(null, "entity", "target").isForm(),is(true));
        Set<ConstraintViolation<String>> violations = new HashSet<>();
        violations.add(new ConstraintViolation<String>() {
            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }
            @Override
            public Object[] getExecutableParameters() {
                return null;
            }
            @Override
            public Object getExecutableReturnValue() {
                return null;
            }
            @Override
            public Object getInvalidValue() {
                return null;
            }
            @Override
            public Object getLeafBean() {
                return null;
            }
            @Override
            public String getMessage() {
                return null;
            }
            @Override
            public String getMessageTemplate() {
                return null;
            }
            @Override
            public Path getPropertyPath() {
                return new Path(){
                    @Override
                    public Iterator<Node> iterator() {
                        return null;
                    }};
            }
            @Override
            public String getRootBean() {
                return null;
            }
            @Override
            public Class<String> getRootBeanClass() {
                return null;
            }
            @Override
            public <U> U unwrap(Class<U> arg0) {
                return null;
            }
        });
        Response response = Mockito.mock(Response.class);
        Request request = Mockito.mock(Request.class);
        Mockito.when(response.getRequest()).thenReturn(request);
        Reference reference = Mockito.mock(Reference.class);
        Mockito.when(request.getOriginalRef()).thenReturn(reference);
        assertThat(new ConstraintViolationsResponse<String>(response, "entity", violations).isForm(),is(true));
    }
}
