package io.skysail.server.restlet;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.sourceconverter.*;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

import org.restlet.representation.Variant;
import org.slf4j.*;

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
    private List<Field> fields;

    private String indexPageName;

    /**
     * Uses target and resource information to convert the source object.
     * 
     * @param source
     * @param target
     * @param resource
     * @param indexPageName 
     */
    public SourceWrapper(Object source, Variant target, SkysailServerResource<?> resource, String indexPageName) {
        this.originalSource = source;
        this.indexPageName = indexPageName;
        fields = ReflectionUtils.getInheritedFields(resource.getParameterType());
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
        return converter.convert(resource, fields, indexPageName);
    }

}
