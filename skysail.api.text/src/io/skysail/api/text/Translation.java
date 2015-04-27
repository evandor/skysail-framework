package io.skysail.api.text;

import java.util.Optional;

import lombok.*;

@Getter
@ToString(of = { "value" })
public abstract class Translation {

    protected String value;
    private Integer serviceRanking;
    private TranslationStore store;

    public Translation(Optional<String> text, TranslationStore store, Integer serviceRanking) {
        this.serviceRanking = serviceRanking;
        this.value = text.orElse(null);
        this.store = store;
    }

    public abstract Class<? extends TranslationRenderService> getTranslatedBy();

}
