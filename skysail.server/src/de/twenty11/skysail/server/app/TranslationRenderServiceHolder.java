package de.twenty11.skysail.server.app;

import io.skysail.api.text.TranslationRenderService;

import java.lang.ref.WeakReference;
import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Constants;

/**
 * Holds a weak reference to a translationRenderService and its service
 * properties.
 *
 */
@Getter
@Slf4j
@EqualsAndHashCode(of = {"name"})// NO_UCD
@ToString(of = {"name"})
public class TranslationRenderServiceHolder {

    private WeakReference<TranslationRenderService> service;
    private WeakHashMap<String, String> props;
    private String name;

    public TranslationRenderServiceHolder(TranslationRenderService service, Map<String, String> props) {
        this.service = new WeakReference<TranslationRenderService>(service);
        this.props = new WeakHashMap<>(props);
        this.name = service.getClass().getName();
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
