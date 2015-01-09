package de.twenty11.skysail.server.app;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.restlet.representation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.enhancement.OObjectProxyMethodHandler;

import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public abstract class AbstractSourceConverter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSourceConverter.class);

    final protected ObjectMapper mapper = new ObjectMapper();

    private Object source;
    private Variant target;

    public void init(Object source, Variant target) {
        this.source = source;
        this.target = target;
    }

    protected Comparator<String> getComparator(SkysailServerResource<?> resource) {
        return null;
    }

    protected Optional<Method> findHandlerMethod(Method[] methods) {
        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
                .findFirst();
        return getHandlerMethod;
    }

    protected SortedMap<String, Object> mapSource(Object source, Optional<Method> getHandlerMethod,
            SkysailServerResource<?> resource) {
        SortedMap<String, Object> objectMap = new TreeMap<>(getComparator(resource));
        try {
            OObjectProxyMethodHandler handler = (OObjectProxyMethodHandler) getHandlerMethod.get().invoke(source);
            ODocument doc = handler.getDoc();
            Map<String, Object> readValue = mapper.readValue(doc.toJSON(), new TypeReference<Map<String, Object>>() {
            });

            Map<String, Object> collect = readValue.entrySet().stream().filter(entry -> {
                return !entry.getKey().startsWith("@") || entry.getKey().equals("@rid");
            }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue() == null ? "&lt;null&gt;" : p.getValue()));

            objectMap.putAll(collect);

            return objectMap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return objectMap;
    }

    public Object getSource() {
        return source;
    }

    public Variant getTarget() {
        return target;
    }

}
