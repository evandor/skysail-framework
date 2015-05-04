package io.skysail.server.text.markdown;

import io.skysail.api.text.*;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.markdown4j.Markdown4jProcessor;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=" + MarkdownTranslationRenderService.SERVICE_RANKING})
@Slf4j
public class MarkdownTranslationRenderService implements TranslationRenderService {

    public static final String PREFIX_IDENTIFIER = "renderer:markdown ";
    
    public static final String SERVICE_RANKING = "100";

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
            return new Markdown4jProcessor().process(adjustText(unformatted));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return translation.getValue();
        }
    }

    @Override
    public String adjustText(String unformatted) {
        return unformatted.trim().substring(PREFIX_IDENTIFIER.length());
    }

    @Override
    public boolean applicable(String unformattedTranslation) {
        return (unformattedTranslation.trim().startsWith(PREFIX_IDENTIFIER));
    }

    @Override
    public String addRendererInfo() {
        return PREFIX_IDENTIFIER;
    }

   

}
