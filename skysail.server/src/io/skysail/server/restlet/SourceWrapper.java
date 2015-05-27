package io.skysail.server.restlet;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.sourceconverter.ConverterFactory;
import io.skysail.server.restlet.sourceconverter.SourceConverter;

import org.restlet.representation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    //private List<Field> fields;

    /**
     * Uses target and resource information to convert the source object.
     * 
     * @param source
     * @param target
     * @param requestModel
     */
    public SourceWrapper(Object source, Variant target, ResourceModel<SkysailServerResource<?>, ?> requestModel) {
        this.originalSource = source;
        this.convertedSource = convertSource(source, target, requestModel);
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

    private Object convertSource(Object source, Variant target, ResourceModel<SkysailServerResource<?>, ?> requestModel) {
        SourceConverter converter = ConverterFactory.getConverter(source, target);
        logger.info("using converter '{}' for {}-Source: {}", new Object[] { converter.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        return converter.convert(requestModel.getResource(), requestModel.getFields());
    }

}
