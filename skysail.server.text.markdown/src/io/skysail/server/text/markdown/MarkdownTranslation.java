package io.skysail.server.text.markdown;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;

import java.util.Optional;

public class MarkdownTranslation extends Translation {

    public MarkdownTranslation(Optional<String> text) {
        super(text, Integer.valueOf(MarkdownTranslationRenderService.SERVICE_RANKING));
    }

    @Override
    public Class<? extends TranslationRenderService> getTranslatedBy() {
        return MarkdownTranslationRenderService.class;
    }

}
