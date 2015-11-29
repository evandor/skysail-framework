package io.skysail.api.validation;

import javax.validation.*;
import javax.validation.bootstrap.GenericBootstrap;

import org.osgi.service.component.annotations.Component;

import io.skysail.api.validation.hibernate.OSGiServiceDiscoverer;

@Component(immediate = true)
public class DefaultValidationImpl implements ValidatorService {

    @Override
    public Validator getValidator() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();
        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        return factory.getValidator();
    }

}
