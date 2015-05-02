package io.skysail.server.text;

import io.skysail.api.text.TranslationStore;

import java.lang.ref.WeakReference;
import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Constants;

@Getter
@Slf4j
@EqualsAndHashCode(of = { "name" })
public class TranslationStoreHolder {

    private WeakReference<TranslationStore> store;
    private WeakHashMap<String, String> props;
    private String name;

    public TranslationStoreHolder(TranslationStore store) {
        this.store = new WeakReference<TranslationStore>(store);
        this.name = store.getClass().getName();
    }

    public TranslationStoreHolder(TranslationStore store, Map<String, String> props) {
        this(store);
        this.props = new WeakHashMap<>(props);
    }

    public Integer getServiceRanking() {
        String serviceRanking = props.get(Constants.SERVICE_RANKING);
        if (StringUtils.isEmpty(serviceRanking)) {
            return 0;
        }
        try {
            return Integer.valueOf(serviceRanking);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("TranslationStoreHolder for ").append(store.get()).toString();
    }

}
