package io.skysail.server.restlet;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.sourceconverter.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.representation.Variant;

/**
 * This class converts the provided "source" object according to the logic
 * provided by the appropriate converters, and keeps a reference to the original
 * source as well.
 * 
 */
@Slf4j
public class SourceWrapper {

    private Object originalSource;
    private Variant target;
    private Object convertedSource;
    private ResourceModel<SkysailServerResource<?>, ?> requestModel;

    /**
     * Uses target and resource information to convert the source object.
     * 
     * @param source
     * @param target
     * @param requestModel
     */
    public SourceWrapper(Object source, Variant target, ResourceModel<SkysailServerResource<?>, ?> requestModel, String indexPageName) {
        this.originalSource = source;
        this.requestModel = requestModel;
        this.convertedSource = convertSource(source, target, indexPageName);
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

    private Object convertSource(Object source, Variant target, String indexPageName) {
        SourceConverter converter = ConverterFactory.getConverter(source, target);
        log.info("using converter '{}' for {}-Source: {}", new Object[] { converter.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        return converter.convert(requestModel.getResource(), requestModel, indexPageName);
    }

}
