package io.skysail.server.text.asciidoc;

import io.skysail.api.text.RenderService;
import io.skysail.api.text.Store;
import io.skysail.api.text.Translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.asciidoctor.Asciidoctor;
import org.restlet.Request;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component
@Slf4j
public class AsciiDocRenderService implements RenderService {

    private List<Store> stores = new ArrayList<>();

    private volatile Asciidoctor asciidoctor;

    @Activate
    public void activate() {
        asciidoctor = org.asciidoctor.Asciidoctor.Factory.create();
    }

    @Deactivate
    public void deactivate() {
        asciidoctor = null;
    }

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request, Store store) {
        if (stores.size() == 0) {
            return new AsciiDocRendererTranslation(Optional.of(key));
        }
        Optional<String> text = stores.get(0).get(key, cl, request);
        return new AsciiDocRendererTranslation(text);
    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        return asciidoctor.convert(translation.getTranslation(), new HashMap<String, Object>());
    }

    @Reference(dynamic = true, optional = false, multiple = true)
    public void addStore(Store store) {
        stores.add(store);
    }

    public void removeStore(Store store) {
        stores.remove(store);
    }

}
