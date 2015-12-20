package io.skysail.api.validation;

import javax.validation.Validator;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ValidatorService {

    Validator getValidator();

}
