package io.skysail.server.documentation;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Method;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import de.twenty11.skysail.api.responses.Link;

//@lombok.EqualsAndHashCode
public class SupportedMethod implements Comparable<SupportedMethod> {

    private Method method;
    private Get get;
    private Post post;
    private Put put;
    private Delete delete;
    private String desc;
    private String path;

    public SupportedMethod(Method method, Get get, String path) {
        this.method = method;
        this.get = get;
        this.path = path;
    }

    public SupportedMethod(Method method, Post post) {
        this.method = method;
        this.post = post;
    }

    public SupportedMethod(Method method, Put put) {
        this.method = method;
        this.put = put;
    }

    public SupportedMethod(Method method, Delete delete) {
        this.method = method;
        this.delete = delete;
    }

    public String getMethodName() {
        return method.getName();
    }

    public List<Link> getGet() {
        if (get == null) {
            return null;
        }
        List<Link> result = new ArrayList<Link>();
        String[] types = get.value().split("\\|");
        for (String type : types) {
            result.add(new Link(path + "?media=" + type, type));
        }
        return result;
    }

    public List<Link> getPostResponseTypes() {
        if (post == null) {
            return null;
        }
        List<Link> result = new ArrayList<Link>();
        String[] types = post.value().split(":");
        if (types.length == 2) {
            String[] split = types[1].split("\\|");
            for (String type : split) {
                result.add(new Link(path + "?media=" + type, type));
            }
        }
        return result;
    }
    
    public List<Link> getPostRequestTypes() {
        if (post == null) {
            return null;
        }
        List<Link> result = new ArrayList<Link>();
        String[] types = post.value().split(":");
        if (types.length == 1 || types.length == 2) {
            String[] split = types[0].split("\\|");
            for (String type : split) {
                result.add(new Link(path + "?media=" + type, type));
            }
        } else if (types.length > 2) {
            // invalid input
        }
        return result;
    }

    public String getPut() {
        return put == null ? null : put.value();
    }

    public String getDelete() {
        return delete == null ? null : delete.value();
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        if (get != null) {
            return get.value();
        } else if (post != null) {
            return post.value();
        } else if (put != null) {
            return put.value();
        } else if (delete != null) {
            return delete.value();
        } else {
            return "";
        }
    }

    public void setApi(API api) {
        this.desc = api.desc();
    }

    @Override
    public int compareTo(SupportedMethod other) {
        int compareTo = getMethodName().compareTo(other.getMethodName());
        if (compareTo != 0) {
            return compareTo;
        }
        return 0;
    }

}
