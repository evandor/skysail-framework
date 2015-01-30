package io.skysail.server.documentation;

import io.skysail.api.documentation.API;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.restlet.data.Method;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import de.twenty11.skysail.api.responses.Link;

@EqualsAndHashCode(of = { "method", "path" })
@Getter
public class SupportedMethod implements Comparable<SupportedMethod> {

    private Method httpVerb;
    private Annotation annotation;
    private String desc;
    private String path;
    private java.lang.reflect.Method method;

    /**
     * @param httpVerb
     * @param annotation
     * @param method
     * @param path
     */
    public SupportedMethod(Method httpVerb, Annotation annotation, java.lang.reflect.Method method, String path) {
        // GET, POST, ....
        this.httpVerb = httpVerb;
        // e.g. @org.restlet.resource.Get(value=json)
        this.annotation = annotation;
        this.method = method;
        this.path = path;
    }

    public List<Link> getGet() {
        if (!(annotation instanceof Get)) {
            return Collections.emptyList();
        }
        return getLinks((Get) annotation);
    }

    public List<Link> getPostResponseTypes() {
        if (!(annotation instanceof Post)) {
            return Collections.emptyList();
        }
        List<Link> result = new ArrayList<Link>();
        String[] types = ((Post) annotation).value().split(":");
        if (types.length == 2) {
            String[] split = types[1].split("\\|");
            for (String type : split) {
                result.add(new Link(path + "?media=" + type, type));
            }
        }
        return result;
    }

    // public List<Link> getPostRequestTypes() {
    // if (annotation == null) {
    // return Collections.emptyList();
    // }
    // List<Link> result = new ArrayList<Link>();
    // String[] types = annotation.value().split(":");
    // if (types.length == 1 || types.length == 2) {
    // String[] split = types[0].split("\\|");
    // for (String type : split) {
    // result.add(new Link(path + "?media=" + type, type));
    // }
    // } else if (types.length > 2) {
    // // invalid input
    // }
    // return result;
    // }

    // public String getPut() {
    // return put == null ? null : put.value();
    // }
    //
    // public String getDelete() {
    // return delete == null ? null : delete.value();
    // }

    public String getValue() {
        if (annotation == null) {
            return "";
        }
        if (annotation instanceof Get) {
            return ((Get) annotation).value();
        } else if (annotation instanceof Post) {
            return ((Post) annotation).value();
        } else if (annotation instanceof Put) {
            return ((Put) annotation).value();
        } else if (annotation instanceof Delete) {
            return ((Delete) annotation).value();
        } else {
            return "";
        }
    }

    public void setApi(API api) {
        this.desc = api.desc();
    }

    @Override
    public int compareTo(SupportedMethod other) {
        int compareTo = getHttpVerb().getName().compareTo(other.getHttpVerb().getName());
        if (compareTo != 0) {
            return compareTo;
        }
        return annotation.toString().compareTo(other.annotation.toString());
    }

    private List<Link> getLinks(Get annotation2) {
        List<Link> result = new ArrayList<Link>();
        String[] types = ((Get) annotation).value().split("\\|");
        for (String type : types) {
            result.add(new Link(path + "?media=" + type, type));
        }
        return result;

    }
}
