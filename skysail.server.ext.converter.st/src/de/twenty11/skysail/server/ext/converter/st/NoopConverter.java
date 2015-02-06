package de.twenty11.skysail.server.ext.converter.st;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

/**
 * A component providing converting functionality via the StringTemplate
 * library.
 *
 */
@Component(immediate = true)
public class NoopConverter extends ConverterHelper implements OsgiConverterHelper {

    private static final Logger logger = LoggerFactory.getLogger(NoopConverter.class);

    private static final float DEFAULT_MATCH_VALUE = 0.5f;

    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

    static {
        mediaTypesMatch.put(MediaType.TEXT_PLAIN, 0.99F);
    }

    @Activate
    public void acitvate() {
        logger.info("{} activated", this.getClass().getName());
    }

    @Deactivate
    public void deacitvate() {
        logger.info("{} deactivated", this.getClass().getName());
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Collections.emptyList();// Arrays.asList(new
                                       // VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM));
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        for (MediaType mediaType : mediaTypesMatch.keySet()) {
            if (target != null && target.getMediaType().equals(mediaType)) {
                logger.info("converter '{}' matched '{}' with threshold {}", new Object[] {
                        this.getClass().getSimpleName(), mediaTypesMatch, mediaTypesMatch.get(mediaType) });
                return mediaTypesMatch.get(mediaType);
            }
        }
        return DEFAULT_MATCH_VALUE;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        return -1.0F;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) {
        throw new RuntimeException("toObject method is not implemented yet");
    }

    /**
     * source: List of entities variant: e.g. [text/html] resource: ? extends
     * SkysailServerResource
     */
    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof SkysailResponse) {
            StringRepresentation rep = new StringRepresentation((String) ((SkysailResponse<?>) source).getEntity());
            rep.setMediaType(MediaType.TEXT_HTML);
            return rep;
        }
        throw new IllegalStateException("toRepresentation in NoopConverter expected SkysailResponse with String Entity");
    }

}
