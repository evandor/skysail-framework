package de.twenty11.skysail.server.core.restlet.converter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.SkysailConverterHelper;

@Component(immediate = true)
public class ServerSentEventsConverter extends SkysailConverterHelper {

	private static Map<MediaType, Float> mediaTypesMatch = new HashMap<>();

	static {
		mediaTypesMatch.put(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS, 1.0F);
	}

	public ServerSentEventsConverter() {
		super(mediaTypesMatch);
	}

	@Override
	public Representation toRepresentation(Object source, Variant target, Resource resource) throws IOException {
		StringRepresentation representation = new StringRepresentation("data: " + System.currentTimeMillis() +"\n\n");
        representation.setMediaType(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
	}

	@Override
	public List<VariantInfo> getVariants(Class<?> source) {
		return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS));
	}

}
