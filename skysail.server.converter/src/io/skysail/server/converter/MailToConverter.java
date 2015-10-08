package io.skysail.server.converter;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.*;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.services.OsgiConverterHelper;
import etm.core.configuration.EtmManager;
import etm.core.monitor.*;

//@Component(immediate = true)
@Slf4j
public class MailToConverter extends ConverterHelper implements OsgiConverterHelper {

    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private static final float DEFAULT_MATCH_VALUE = 0.0f;

    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

    static {
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_MAILTO_MEDIATYPE, 1.0F);
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM), new VariantInfo(
                SkysailApplication.SKYSAIL_MAILTO_MEDIATYPE));
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

    @Override
    public Representation toRepresentation(Object originalSource, Variant target, Resource resource) {
        EtmPoint point = etmMonitor.createPoint(this.getClass().getSimpleName() + ":toRepresentation");

        SkysailServerResource<?> ssr = (SkysailServerResource<?>) resource;
        List<Field> fields = ReflectionUtils.getInheritedFields(ssr.getParameterizedType());

        String subject = Reference.encode("Exporting " + resource.getClass().getSimpleName());

        String body = fields.stream().map(field -> {
            String value = "???";
            try {
                field.setAccessible(true);
                Object object = field.get(ssr.getCurrentEntity());
                value = object != null ? object.toString() : "";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return field.getName().toString() + ": " + value;
        }).collect(Collectors.joining("\n"));

        body = Reference.encode(body);

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("  <head>");
        sb.append("    <meta http-equiv='refresh' content='0; url=mailto:?Subject=").append(subject).append("&body=")
                .append(body).append("'>");
        sb.append("  </head>");
        sb.append("</html>");
        StringRepresentation rep = new StringRepresentation(sb.toString(), MediaType.TEXT_HTML);
        point.collect();
        return rep;
    }
}
