package io.skysail.server.documentation;

import io.skysail.api.documentation.API;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;

import org.restlet.data.Method;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.utils.ReflectionUtils;

/**
 * A resourceApi connects a path with a target {@link ServerResource} and
 * analyzes its associated entity and methods.
 * 
 * resourceApis defines a natural order based on its path.
 *
 */
@EqualsAndHashCode(of = { "path" })
public class ResourceApi implements Comparable<ResourceApi> {

    private static final Logger logger = LoggerFactory.getLogger(ResourceApi.class);

    private String path;
    private String securedByRole;
    private Class<? extends ServerResource> target;
    private String desc;
    private List<SupportedMethod> methods = new ArrayList<SupportedMethod>();

    private EntityDescriptor entity;

    public ResourceApi() {
        // needed by converter
    }

    /**
     * Creates a new instance by analyzing the provided route Builder.
     * 
     * @param path
     *            the path (relative to the applications path)
     * @param routeBuilder
     *            the route builder used in the application.
     */
    public ResourceApi(String path, RouteBuilder routeBuilder) {
        this.path = path; // e.g. "clipboard", "clipboard/clips/{id}"
        target = routeBuilder.getTargetClass(); // e.g. ClipsResource
        setUpEntity(target);
        handleMethodAnnotations();
        removeDuplicatedAnnotations();
        sortMethods();
    }

    public String getPath() {
        return path;
    }

    public String getTargetClassName() {
        return target.getName();
    }

    public String getSecuredByRole() {
        return securedByRole;
    }

    public String getDesc() {
        return desc;
    }

    public List<SupportedMethod> getMethods() {
        return methods;
    }

    public EntityDescriptor getEntity() {
        return entity;
    }

    private void handleMethodAnnotations() {
        try {
            ServerResource instance = target.newInstance();
            if (instance instanceof SkysailServerResource) {
                SkysailServerResource<?> ssr = ((SkysailServerResource<?>) instance);
                desc = ssr.getDescription();
            }
        } catch (Exception e) {
            logger.warn("could not create instance from {}", target == null ? "unknown target" : target.getName());
        }
        List<java.lang.reflect.Method> methods = ReflectionUtils.getInheritedMethods(target);
        for (java.lang.reflect.Method method : methods) {
            SupportedMethod supportedMethod = null;
            Get getAnnotation = method.getAnnotation(Get.class);
            if (getAnnotation != null) {
                supportedMethod = addGet(getAnnotation, method);
            }
            Post postAnnotation = method.getAnnotation(Post.class);
            if (postAnnotation != null) {
                supportedMethod = addPost(postAnnotation, method);
            }
            Put putAnnotation = method.getAnnotation(Put.class);
            if (putAnnotation != null) {
                supportedMethod = addPut(putAnnotation, method);
            }
            Delete deleteAnnotation = method.getAnnotation(Delete.class);
            if (deleteAnnotation != null) {
                supportedMethod = addDelete(deleteAnnotation, method);
            }
            API api = method.getAnnotation(API.class);
            if (api != null) {
                addApi(api, supportedMethod);
            }
        }
    }

    private void removeDuplicatedAnnotations() {
        List<SupportedMethod> methodsToRemove = new ArrayList<SupportedMethod>();
        for (SupportedMethod method : methods) {
            SupportedMethod duplicate = getDuplicate(method);
            if (duplicate != null) {
                methodsToRemove.add(duplicate);
            }
            // List<SupportedMethod> parents = getParents(method);
            // methodsToRemove.addAll(parents);
        }
        for (SupportedMethod methodToRemove : methodsToRemove) {
            methods.remove(methodToRemove);
        }
    }

    private SupportedMethod getDuplicate(SupportedMethod methodToCheck) {
        for (SupportedMethod method : methods) {
            if (methodToCheck.equals(method)) {
                continue;
            }
            // if
            // (methodToCheck.getHttpVerb().getName().equals(method.getHttpVerb().getName())
            // && methodToCheck.getValue().equals(method.getValue())) {
            // // if (method.getDesc() == null) {
            // // continue;
            // // }
            // // if
            // // (method.getDesc().startsWith("generic API description for "))
            // // {
            // // return method;
            // // }
            // }
        }
        return null;
    }

    private SupportedMethod addGet(Get annotation, java.lang.reflect.Method method) {
        SupportedMethod supportMethod = new SupportedMethod(Method.GET, annotation, method, path);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addPost(Post annotation, java.lang.reflect.Method method) {
        SupportedMethod supportMethod = new SupportedMethod(Method.POST, annotation, method, path);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addPut(Put annotation, java.lang.reflect.Method method) {
        SupportedMethod supportMethod = new SupportedMethod(Method.PUT, annotation, method, path);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addDelete(Delete annotation, java.lang.reflect.Method method) {
        SupportedMethod supportMethod = new SupportedMethod(Method.DELETE, annotation, method, path);
        methods.add(supportMethod);
        return supportMethod;
    }

    private void addApi(API api, SupportedMethod supportedMethod) {
        if (api == null || supportedMethod == null) {
            return;
        }
        supportedMethod.setApi(api);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(path).append(": ");
        sb.append(desc == null ? "" : desc).append(" (").append(target.getSimpleName()).append(")");
        return sb.toString();
    }

    @Override
    public int compareTo(ResourceApi o) {
        return path.compareTo(o.getPath());
    }

    private void sortMethods() {
        Collections.sort(methods);
    }

    private void setUpEntity(Class<? extends ServerResource> target) {
        if (target == null) {
            return;
        }
        Type genericSuperclass = target.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (actualTypeArguments == null || actualTypeArguments.length == 0) {
                return;
            }
            if (actualTypeArguments[0] instanceof Class) {
                Class<?> cls = (Class<?>) actualTypeArguments[0];
                analyze(cls, rawType);
            }
        }
    }

    private void analyze(Class<?> entityClass, Class<?> rawType) {
        entity = new EntityDescriptor(entityClass, rawType);
    }

}
