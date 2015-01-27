package io.skysail.api.validation.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationProviderResolver;
import javax.validation.spi.ValidationProvider;

import org.hibernate.validator.HibernateValidator;

public class OSGiServiceDiscoverer implements ValidationProviderResolver {

    @Override
    public List<ValidationProvider<?>> getValidationProviders() {
        List<ValidationProvider<?>> result = new ArrayList<ValidationProvider<?>>();
        HibernateValidator hibernateValidator = new HibernateValidator();
        result.add(hibernateValidator);
        return result;
    }
}
