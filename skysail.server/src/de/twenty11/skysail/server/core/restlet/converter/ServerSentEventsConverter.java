package de.twenty11.skysail.server.core.restlet.converter;

import io.skysail.server.app.SkysailApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.OsgiConverterHelper;

@Component(immediate = true)
@Slf4j
public class ServerSentEventsConverter extends ConverterHelper implements OsgiConverterHelper {

	private static final float DEFAULT_MATCH_VALUE = 0f;
	
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<>();

	static {
		mediaTypesMatch.put(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS, 1.0F);
	}

	
	@Override
	public Representation toRepresentation(Object source, Variant target, Resource resource) throws IOException {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		StringRepresentation representation = new StringRepresentation("retry: 10000\ndata: " + sdf.format(new Date()) +"\n\n");
        representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
	}

	@Override
	public List<VariantInfo> getVariants(Class<?> source) {
		return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS));
	}

	@Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (target == null) {
            return 0.0f;
        }
        for (MediaType mediaType : mediaTypesMatch.keySet()) {
            if (target.getMediaType().equals(mediaType)) {
                log.info("converter '{}' matched '{}' with threshold {}", new Object[] {
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

}
