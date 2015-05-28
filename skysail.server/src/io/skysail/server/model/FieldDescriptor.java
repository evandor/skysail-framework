package io.skysail.server.model;

import io.skysail.api.forms.Postfix;
import io.skysail.api.forms.Prefix;
import io.skysail.api.links.Link;
import io.skysail.server.forms.ListView;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;

public class FieldDescriptor {

    private io.skysail.api.forms.Field formField;
    private ListView listView;
    private Postfix postfix;
    private Prefix prefix;

    private Class<?> type;
    private Field f;

    public FieldDescriptor(Field field) {
        this.f = field;
        formField = field.getAnnotation(io.skysail.api.forms.Field.class);
        listView = field.getAnnotation(ListView.class);
        postfix = field.getAnnotation(Postfix.class);
        prefix = field.getAnnotation(Prefix.class);
        type = field.getType();
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props, SkysailServerResource<?> resource) {
        return check(props, resource);
    }

    private Map<String, Object> check(Map<String, Object> props, SkysailServerResource<?> resource) {
        if (listView == null) {
            return props;
        }
        String newValue = null;
        if (listView.truncate() > 3) {
            String oldValue = newValue = (String) props.get(f.getName());
            if (oldValue != null && oldValue.length() > listView.truncate()) {
                newValue = "<span title='" + oldValue + "'>" + oldValue.substring(0, listView.truncate() - 3)
                        + "...</span>";
            }
        }
        if (URL.class.equals(f.getType())) {
            newValue = "<a href='" + props.get(f.getName()).toString() + "' target=\"_blank\">" + newValue + "</a>";
        } else {
            Class<? extends SkysailServerResource<?>> linkedResource = listView.link();
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
        Prefix prefix = f.getAnnotation(Prefix.class);
        if (newValue != null && prefix != null) {
            newValue = props.get(prefix.methodName()) + "&nbsp;" + newValue;
        }
        Postfix postfix = f.getAnnotation(Postfix.class);
        if (newValue != null && postfix != null) {
            newValue = newValue + "&nbsp;" + props.get(postfix.methodName());
            props.put(f.getName() + "_postfix", props.get(postfix.methodName()));
        }

        if (newValue != null) {
            props.put(f.getName(), newValue);
        }
        return props;
    }

}
