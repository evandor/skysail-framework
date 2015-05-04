package io.skysail.api.text;

import java.util.*;

import lombok.*;

@Getter
@ToString(of = { "value" })
public class Translation {

    protected String value;
    private TranslationStore store;
    private Set<String> stores = Collections.emptySet();

    public Translation(String text, @NonNull TranslationStore store, @NonNull Set<String> stores) {
        this.value = text;//.orElse(null);
        this.store = store;
        this.stores = stores;
    }


}
