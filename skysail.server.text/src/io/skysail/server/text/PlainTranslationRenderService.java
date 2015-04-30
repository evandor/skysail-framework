package io.skysail.server.text;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import aQute.bnd.annotation.component.Component;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=" + PlainTranslationRenderService.SERVICE_RANKING})
public class PlainTranslationRenderService extends AbstractTranslationRenderService implements TranslationRenderService {

    public static final String SERVICE_RANKING = "-1";

    @Override
    public String render(Translation translation, Object... substitutions) {
        return translation.getValue();
    }

    @Override
    protected Translation createTranslation(StoreAndTranslation t) {
        return new PlainTranslation(t);
    }

}
