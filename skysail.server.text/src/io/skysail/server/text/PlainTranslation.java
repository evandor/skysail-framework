package io.skysail.server.text;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;

public class PlainTranslation extends Translation {

    public PlainTranslation(StoreAndTranslation sat) {
        super(sat.getTranslation(), sat.getStore(), Integer.valueOf(PlainTranslationRenderService.SERVICE_RANKING));
    }

    @Override
    public Class<? extends TranslationRenderService> getTranslatedBy() {
       return PlainTranslationRenderService.class;
    }

}
