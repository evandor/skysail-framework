package io.skysail.api.validation.hibernate.it;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import io.skysail.api.validation.ValidatorService;

import java.util.Collection;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ValidationIntegrationTests {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    @Test
    public void validatorServiceIsAvailable() {
        Collection<ServiceReference<ValidatorService>> validatorServiceReferences;
        try {
            validatorServiceReferences = context.getServiceReferences(ValidatorService.class, null);
            assertThat(validatorServiceReferences.size(), is(1));
        } catch (InvalidSyntaxException e) {
            fail();
        }
    }

}
