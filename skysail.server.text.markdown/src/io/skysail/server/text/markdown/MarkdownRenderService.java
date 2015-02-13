package io.skysail.server.text.markdown;

import io.skysail.api.text.RenderService;
import io.skysail.api.text.Store;
import io.skysail.api.text.Translation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.markdown4j.Markdown4jProcessor;
import org.restlet.Request;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
@Slf4j
public class MarkdownRenderService implements RenderService {

    private List<Store> stores = new ArrayList<>();

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request, Store store) {
        if (stores.size() == 0) {
            return new MarkdownRendererTranslation(Optional.of(key));
        }
        Optional<String> text = stores.get(0).get(key, cl, request);
        return new MarkdownRendererTranslation(text);
    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        try {
            // TODO handle substitutions
            return new Markdown4jProcessor().process(StringEscapeUtils.unescapeHtml4(translation.getTranslation()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return translation.getTranslation();
        }
    }

    @Reference(dynamic = true, optional = false, multiple = true)
    public void addStore(Store store) {
        stores.add(store);
    }

    public void removeStore(Store store) {
        stores.remove(store);
    }

}
