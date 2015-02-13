package io.skysail.server.text.asciidoc;

import io.skysail.api.text.Translation;

import java.util.Optional;

import lombok.Getter;

public class AsciiDocRendererTranslation implements Translation {

    @Getter
    private String translation;

    public AsciiDocRendererTranslation(Optional<String> text) {
        this.translation = text.orElse(null);
    }

}
