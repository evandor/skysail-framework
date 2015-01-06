package de.twenty11.skysail.server.core.restlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SkysailConverterHelper extends ConverterHelper {

	private static final Logger logger = LoggerFactory.getLogger(SkysailConverterHelper.class);

	protected static final float NO_MATCH_VALUE = 0.0F;
	private static final float DEFAULT_MATCH_VALUE = 0.5F;

	protected static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_JSON);

	private Map<MediaType, Float> mediaTypesMatch;

	public SkysailConverterHelper(Map<MediaType, Float> mediaTypesMatch) {
		this.mediaTypesMatch = mediaTypesMatch;
		logger.info("initializing new converter '{}' with typesMatch '{}'", this.getClass().getSimpleName(),
		        mediaTypesMatch);
	}

	@Override
	public float score(Object source, Variant target, Resource resource) {
//		if (!(source instanceof SkysailResponse)) {
//			return NO_MATCH_VALUE;
//		}
		for (MediaType mediaType : mediaTypesMatch.keySet()) {
			if (target.getMediaType().equals(mediaType)) {
				logger.info("converter '{}' matched '{}' with threshold {}",
						new Object[] {this.getClass().getSimpleName(),
				        mediaTypesMatch, mediaTypesMatch.get(mediaType) });
				return mediaTypesMatch.get(mediaType);
			}
		}
		return DEFAULT_MATCH_VALUE;
	}

	@Override
	public <T> float score(Representation source, Class<T> target, Resource resource) {
		float result = -1.0F;

		if (source instanceof JacksonRepresentation<?>) {
			result = 1.0F;
		} else if ((target != null) && JacksonRepresentation.class.isAssignableFrom(target)) {
			result = 1.0F;
		} else if (VARIANT_JSON.isCompatible(source)) {
			result = 0.8F;
		}

		return result;
	}

	public abstract Representation toRepresentation(Object source, Variant target, Resource resource) throws IOException;

	@Override
	public <T> T toObject(Representation source, Class<T> target, Resource resource) throws IOException {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<Class<?>> getObjectClasses(Variant source) {
		throw new NotImplementedException("getObjectClasses method is not implemented yet");
	}

}
