package io.skysail.server.restlet;

import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.sourceconverter.ListSourceHtmlConverter;

import java.util.List;

import org.restlet.representation.Variant;

/**
 * This class converts the provided "source" object according to the logic
 * provided by the appropriate converters, and keeps a reference to the original
 * source as well.
 * 
 */
public class SourceWrapper {

    private Object originalSource;
    private Object convertedSource;
    private ResourceModel<SkysailServerResource<?>, ?> resourceModel;

    /**
     * Uses target and resource information to convert the source object.
     */
    public SourceWrapper(Object source, Variant target, ResourceModel<SkysailServerResource<?>, ?> model) {
        this.originalSource = source;
        this.resourceModel = model;
        if (source instanceof List) {
            this.convertedSource = new ListSourceHtmlConverter(source, target).convert(resourceModel);
        } else {
            this.convertedSource = source;
        }
    }

    public Object getOriginalSource() {
        return originalSource;
    }

    public Object getConvertedSource() {
        return convertedSource;
    }

}
