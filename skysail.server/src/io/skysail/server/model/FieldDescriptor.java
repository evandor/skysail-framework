package io.skysail.server.model;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.net.URL;
import java.util.*;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;

public class FieldDescriptor {

    private FormField f;

    public FieldDescriptor(FormField f2) {
        this.f = f2;
//        formField = f2.getAnnotation(io.skysail.api.forms.Field.class);
//        listView = f2.getAnnotation(ListView.class);
//        postfix = f2.getAnnotation(Postfix.class);
//        prefix = f2.getAnnotation(Prefix.class);
//        type = f2.getType();
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props, SkysailServerResource<?> resource) {
        return check(props, resource);
    }

    private Map<String, Object> check(Map<String, Object> props, SkysailServerResource<?> resource) {
        if (f.getListViewAnnotation() == null) {
            return props;
        }
        String newValue = null;
        if (f.getListViewAnnotation().truncate() > 3) {
            String oldValue = newValue = (String) props.get(f.getName());
            if (oldValue != null && oldValue.length() > f.getListViewAnnotation().truncate()) {
                newValue = "<span title='" + oldValue + "'>" + oldValue.substring(0, f.getListViewAnnotation().truncate() - 3)
                        + "...</span>";
            }
        }
        if (URL.class.equals(f.getType())) {
            newValue = "<a href='" + props.get(f.getName()).toString() + "' target=\"_blank\">" + newValue + "</a>";
        } else {
            Class<? extends SkysailServerResource<?>> linkedResource = f.getListViewAnnotation().link();
            if (linkedResource != null) {
                List<Link> links = resource.getLinks();
                String id = props.get("id") != null ? props.get("id").toString().replace("#", "") : null;
                if (links != null && id != null) {
                    Optional<Link> findFirst = links.stream().filter(l -> {
                        return linkedResource.equals(l.getCls()) && id.equals(l.getRefId());
                    }).findFirst();
                    if (findFirst.isPresent()) {
                        String page = CookiesUtils.getMainPageFromCookie(resource.getRequest());
                        if (page != null && page.equals("indexMobile")) {
                            // newValue =
                            // "<a href='"+findFirst.get().getUri()+"'><input type='button' class='btn btn-primary btn-lg btn-block' value='"
                            // + newValue + "' /></a>";
                            newValue = newValue;
                            props.put("_href", findFirst.get().getUri());
                        } else {
                            newValue = "<a href='" + findFirst.get().getUri() + "'>" + newValue + "</a>";
                        }
                    }
                }
            }
        }
//        Prefix prefix = f.getAnnotation(Prefix.class);
//        if (newValue != null && prefix != null) {
//            newValue = props.get(prefix.methodName()) + "&nbsp;" + newValue;
//        }
//        Postfix postfix = f.getAnnotation(Postfix.class);
//        if (newValue != null && postfix != null) {
//            newValue = newValue + "&nbsp;" + props.get(postfix.methodName());
//            props.put(f.getName() + "_postfix", props.get(postfix.methodName()));
//        }

        if (newValue != null) {
            props.put(f.getName(), newValue);
        }
        return props;
    }

    public boolean isSubmitField() {
        return f.isSubmitField();
    }

}
