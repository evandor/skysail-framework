package io.skysail.api.validation;

import io.skysail.api.validation.hibernate.OSGiServiceDiscoverer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import aQute.bnd.annotation.component.Component;

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
