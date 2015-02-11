package de.twenty11.skysail.server.db.orientdb;

import java.io.IOException;
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

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component
public class OrientDbJsonConverter extends ConverterHelper implements OsgiConverterHelper {

    private static final Logger logger = LoggerFactory.getLogger(OrientDbJsonConverter.class);

    private static final float DEFAULT_MATCH_VALUE = 0.5f;
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

    static {
        mediaTypesMatch.put(MediaType.APPLICATION_JSON, 0.95F);
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Collections.emptyList();
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (source == null) {
            return -1.0F;
        }
        if (!(source instanceof List)) {
            if (!(source.getClass().getName().contains("$$"))) {
                return -1.0F;
            }
        } else {
            List<?> listSource = (List<?>)source;
            if (listSource.size() == 0) {
                return -1.0F;
            }
            if (!(listSource.get(0).getClass().getName().contains("$$"))) {
                return -1.0F;
            }
        }
        return -1.0F;
        // for (MediaType mediaType : mediaTypesMatch.keySet()) {
        // if (target.getMediaType().equals(mediaType)) {
        // logger.info("converter '{}' matched '{}' with threshold {}", new
        // Object[] {
        // this.getClass().getSimpleName(), mediaTypesMatch,
        // mediaTypesMatch.get(mediaType) });
        // return mediaTypesMatch.get(mediaType);
        // }
        // }
        // return DEFAULT_MATCH_VALUE;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        return -1.0F;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) {
        throw new RuntimeException("toObject method is not implemented yet");
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) throws IOException {
        List<?> listSource = (List<?>)source;
       // listSource.
        return new StringRepresentation("Hi");
    }

}
