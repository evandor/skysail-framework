package de.twenty11.skysail.server.app;

import org.restlet.representation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.app.sourceconverter.ConverterFactory;
import de.twenty11.skysail.server.app.sourceconverter.SourceConverter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

/**
 * This class converts the provided "source" object according to the logic
 * provided by the appropriate converters, and keeps a reference to the original
 * source as well.
 *
 */
public class SourceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(SourceWrapper.class);

    private Object originalSource;
    private Variant target;
    private Object convertedSource;

    public SourceWrapper(Object source, Variant target, SkysailServerResource<?> resource) {
        this.originalSource = source;
        this.convertedSource = convertSource(source, target, resource);
    }

    public Object getOriginalSource() {
        return originalSource;
    }

    public Object getConvertedSource() {
        return convertedSource;
    }

    public Variant getTarget() {
        return target;
    }

    private Object convertSource(Object source, Variant target, SkysailServerResource<?> resource) {
        SourceConverter converter = ConverterFactory.getConverter(source, target);
        logger.info("using converter '{}' for {}-Source: {}", new Object[] { converter.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        return converter.convert(resource);
    }

}
