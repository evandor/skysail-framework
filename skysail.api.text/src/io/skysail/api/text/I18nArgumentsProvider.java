package io.skysail.api.text;

import aQute.bnd.annotation.ConsumerType;

@ConsumerType
public interface I18nArgumentsProvider {

    MessageArguments getMessageArguments();

}
