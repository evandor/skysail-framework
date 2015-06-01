package io.skysail.api.text;

import java.util.Collections;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(of = { "value" })
public class Translation {

    protected String value;
    private TranslationStore store;
    private Set<String> stores = Collections.emptySet();

    public Translation(String text, @NonNull TranslationStore store, @NonNull Set<String> stores) {
        this.value = text;
        this.store = store;
        this.stores = stores;
    }


}
