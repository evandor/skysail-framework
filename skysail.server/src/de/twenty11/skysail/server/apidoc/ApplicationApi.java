package de.twenty11.skysail.server.apidoc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class ApplicationApi implements Comparable<ApplicationApi> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationApi.class);

    private String path;
    private String securedByRole;
    private Class<? extends ServerResource> target;
    private String desc;
    private List<SupportedMethod> methods = new ArrayList<SupportedMethod>();

    private EntityDescriptor entity;

    public ApplicationApi(String path, RouteBuilder routeBuilder) {
        this.path = path;
        //securedByRole = routeBuilder.getSecuredByRole();
        target = routeBuilder.getTargetClass();
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
            Get get = method.getAnnotation(Get.class);
            if (get != null) {
                supportedMethod = addGet(get);
            }
            Post post = method.getAnnotation(Post.class);
            if (post != null) {
                supportedMethod = addPost(post);
            }
            Put put = method.getAnnotation(Put.class);
            if (put != null) {
                supportedMethod = addPut(put);
            }
            Delete delete = method.getAnnotation(Delete.class);
            if (delete != null) {
                supportedMethod = addDelete(delete);
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
            //List<SupportedMethod> parents = getParents(method);
            //methodsToRemove.addAll(parents);
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
            if (methodToCheck.getMethodName().equals(method.getMethodName())
                    && methodToCheck.getValue().equals(method.getValue())) {
                if (method.getDesc() == null) {
                    continue;
                }
                if (method.getDesc().startsWith("generic API description for ")) {
                    return method;
                }
            }
        }
        return null;
    }

    private SupportedMethod addGet(Get htmlMethod) {
        SupportedMethod supportMethod = new SupportedMethod(Method.GET, htmlMethod, path);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addPost(Post htmlMethod) {
        SupportedMethod supportMethod = new SupportedMethod(Method.POST, htmlMethod);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addPut(Put htmlMethod) {
        SupportedMethod supportMethod = new SupportedMethod(Method.PUT, htmlMethod);
        methods.add(supportMethod);
        return supportMethod;
    }

    private SupportedMethod addDelete(Delete htmlMethod) {
        SupportedMethod supportMethod = new SupportedMethod(Method.DELETE, htmlMethod);
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
    public int compareTo(ApplicationApi o) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplicationApi other = (ApplicationApi) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

}
