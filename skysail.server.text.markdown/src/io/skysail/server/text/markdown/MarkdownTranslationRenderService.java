package io.skysail.server.text.markdown;

import io.skysail.api.text.*;

import java.io.IOException;
import java.text.MessageFormat;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.markdown4j.Markdown4jProcessor;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=" + MarkdownTranslationRenderService.SERVICE_RANKING})
@Slf4j
public class MarkdownTranslationRenderService implements TranslationRenderService {

    public static final String PREFIX_IDENTIFIER = "renderer:markdown ";

    public static final String SERVICE_RANKING = "100";

    private MessageFormat formatter;

    public MarkdownTranslationRenderService() {
        formatter = new MessageFormat("");
    }

    @Override
    public String render(String in, Object... substitutions) {
        String unformatted = StringEscapeUtils.unescapeHtml4(in.trim());
        try {
            return new Markdown4jProcessor().process(adjustText(unformatted));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return in;        }
    }

    @Override
    // TODO handle substitutions
    public String render(Translation translation) {
        if (translation == null) {
            return null;
        }
        String rawTranslation = translation.getValue();
        try {
            formatter.applyPattern(rawTranslation);
            String messageFormatted = formatter.format(translation.getMessageArguments().toArray(new Object[translation.getMessageArguments().size()]));
            return process(messageFormatted);
        } catch (IllegalArgumentException iae) {
            return process(rawTranslation);
        }
    }

    private String process(String inputString) {
        String unescaped = StringEscapeUtils.unescapeHtml4(inputString);
        if (unescaped == null) {
            return null;
        }
        try {
            return new Markdown4jProcessor().process(adjustText(unescaped));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return inputString;
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
