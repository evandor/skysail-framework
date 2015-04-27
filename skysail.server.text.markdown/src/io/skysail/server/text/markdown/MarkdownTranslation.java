package io.skysail.server.text.markdown;

import io.skysail.api.text.*;
import io.skysail.server.text.StoreAndTranslation;

public class MarkdownTranslation extends Translation {

    public MarkdownTranslation(StoreAndTranslation sat) {
        super(sat.getTranslation(), sat.getStore(), Integer.valueOf(MarkdownTranslationRenderService.SERVICE_RANKING));
    }

    @Override
    public Class<? extends TranslationRenderService> getTranslatedBy() {
        return MarkdownTranslationRenderService.class;
    }

}
