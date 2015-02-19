package io.skysail.api.text;

import java.util.Optional;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = { "value" })
public abstract class Translation {

    protected String value;
    private Integer serviceRanking;

    public Translation(Optional<String> text, Integer serviceRanking) {
        this.serviceRanking = serviceRanking;
        this.value = text.orElse(null);
    }

    public abstract Class<? extends TranslationRenderService> getTranslatedBy();

}
