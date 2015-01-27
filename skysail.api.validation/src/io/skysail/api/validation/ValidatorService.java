package io.skysail.api.validation;

import javax.validation.Validator;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface ValidatorService {

    Validator getValidator();

}
