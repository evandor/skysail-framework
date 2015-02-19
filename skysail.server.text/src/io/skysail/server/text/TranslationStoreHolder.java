package io.skysail.server.text;

import io.skysail.api.text.TranslationStore;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Constants;

@Getter
@Slf4j
@EqualsAndHashCode(of = { "store" })
public class TranslationStoreHolder {

    private WeakReference<TranslationStore> store;
    private WeakHashMap<String, String> props;

    public TranslationStoreHolder(TranslationStore store) {
        this.store = new WeakReference<TranslationStore>(store);
    }

    public TranslationStoreHolder(TranslationStore store, Map<String, String> props) {
        this.store = new WeakReference<TranslationStore>(store);
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
}
