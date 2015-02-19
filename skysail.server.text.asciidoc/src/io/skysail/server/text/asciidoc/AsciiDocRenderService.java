package io.skysail.server.text.asciidoc;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.api.text.TranslationStore;
import io.skysail.server.text.AbstractTranslationRenderService;
import io.skysail.server.text.TranslationStoreHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.asciidoctor.Asciidoctor;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true, properties = { "test=" + AsciiDocRenderService.SERVICE_RANKING })
@Slf4j
public class AsciiDocRenderService extends AbstractTranslationRenderService implements TranslationRenderService {

    public static final String SERVICE_RANKING = "200";

    private volatile Asciidoctor asciidoctor;

    @Activate
    public void activate() {
        asciidoctor = org.asciidoctor.Asciidoctor.Factory.create(Arrays
                .asList("/Users/carsten/git/skysail-framework/skysail.server.text.asciidoc/resources"));
        // asciidoctor =
        // org.asciidoctor.Asciidoctor.Factory.create(ClassLoader.getSystemClassLoader());
    }

    @Deactivate
    public void deactivate() {
        asciidoctor = null;
    }

    protected Translation createTranslation(Optional<String> t) {
        return new AsciiDocRendererTranslation(t);
    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        return asciidoctor.convert(translation.getValue(), new HashMap<String, Object>());
    }

    @Reference(dynamic = true, optional = false, multiple = true)
    public void addStore(TranslationStore store, Map<String, String> props) {
        stores.add(new TranslationStoreHolder(store, props));
    }

    public void removeStore(TranslationStore store) {
        stores.remove(new TranslationStoreHolder(store));
    }

}
