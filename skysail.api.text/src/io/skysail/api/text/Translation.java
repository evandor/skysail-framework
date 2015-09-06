package io.skysail.api.text;

import java.util.*;

import lombok.*;

@Getter
@ToString(of = { "value" })
public class Translation {

    protected String value;
    private TranslationStore store;
    private Set<String> stores = Collections.emptySet();
    private Collection<Object> messageArguments = Collections.emptyList();
    private String translated;
    private String renderer;


    public Translation(String text, TranslationStore store, @NonNull Set<String> stores) {
        this.value = text;
        this.store = store;
        this.stores = stores;
    }

    public Translation(String text, TranslationStore store, Collection<Object> messageArguments, @NonNull Set<String> stores) {
        this.value = text;
        this.store = store;
        this.stores = stores;
        if (messageArguments != null) {
            this.messageArguments = messageArguments;
        }
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    public String getStoreName() {
        return store != null ? store.getClass().getSimpleName() : "-";
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

}
