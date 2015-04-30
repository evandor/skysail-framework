package io.skysail.server.text.markdown;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.server.text.AbstractTranslationRenderService;
import io.skysail.server.text.StoreAndTranslation;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.markdown4j.Markdown4jProcessor;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true, properties = { "test=" + MarkdownTranslationRenderService.SERVICE_RANKING })
@Slf4j
public class MarkdownTranslationRenderService extends AbstractTranslationRenderService implements
        TranslationRenderService {

    public static final String SERVICE_RANKING = "100";

    @Override
    protected Translation createTranslation(StoreAndTranslation sat) {
        return new MarkdownTranslation(sat);
    }

    @Override
    // TODO handle substitutions
    public String render(Translation translation, Object... substitutions) {
        if (translation == null) {
            return null;
        }
        try {
            String unformatted = StringEscapeUtils.unescapeHtml4(translation.getValue());
            if (unformatted == null) {
                return null;
            }
            return new Markdown4jProcessor().process(unformatted);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return translation.getValue();
        }
    }

//    @Reference(dynamic = true, optional = false, multiple = true)
//    public void addStore(TranslationStore store, Map<String, String> props) {
//        stores.add(new TranslationStoreHolder(store, props));
//    }
//
//    public void removeStore(TranslationStore store) {
//        stores.remove(new TranslationStoreHolder(store));
//    }

}
