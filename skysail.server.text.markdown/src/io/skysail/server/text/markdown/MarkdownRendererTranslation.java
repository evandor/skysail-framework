package io.skysail.server.text.markdown;

import io.skysail.api.text.Translation;

import java.util.Optional;

import lombok.Getter;

public class MarkdownRendererTranslation implements Translation {

    @Getter
    private String translation;

    public MarkdownRendererTranslation(Optional<String> text) {
        this.translation = text.orElse(null);
    }

}
