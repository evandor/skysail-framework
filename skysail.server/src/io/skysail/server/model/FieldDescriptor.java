//package io.skysail.server.model;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.utils.RequestUtils;
//
//import java.net.URL;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.restlet.Request;
//
//import de.twenty11.skysail.server.core.FormField;
//import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
//
//public class FieldDescriptor {
//
//    private FormField f;
//
//    public FieldDescriptor(FormField f2) {
//        this.f = f2;
//    }
//
//    public Map<String, Object> dataFromMap(Map<String, Object> props, SkysailServerResource<?> resource) {
//        return check(props, resource);
//    }
//
//    private Map<String, Object> check(Map<String, Object> props, SkysailServerResource<?> resource) {
//        if (f.getListViewAnnotation() == null) {
//            return props;
//        }
//        String newValue = null;
//        if (f.getListViewAnnotation().truncate() > 3) {
//            String oldValue = newValue = (String) props.get(f.getName());
//            if (oldValue != null && oldValue.length() > f.getListViewAnnotation().truncate()) {
//                newValue = "<span title='" + oldValue + "'>" + oldValue.substring(0, f.getListViewAnnotation().truncate() - 3)
//                        + "...</span>";
//            }
//        }
//        if (URL.class.equals(f.getType())) {
//            newValue = "<a href='" + props.get(f.getName()).toString() + "' target=\"_blank\">" + newValue + "</a>";
//        } else {
//            Class<? extends SkysailServerResource<?>> linkedResource = f.getListViewAnnotation().link();
//            if (linkedResource != null) {
//                List<Link> links = resource.getLinks();
//                String id = props.get("id") != null ? props.get("id").toString().replace("#", "") : null;
//                if (links != null && id != null) {
//                    Optional<Link> findFirst = links.stream().filter(l -> {
//                        return linkedResource.equals(l.getCls()) && id.equals(l.getRefId());
//                    }).findFirst();
//                    if (findFirst.isPresent()) {
//                        if (showMobilePage(resource.getRequest())) {
//                            // newValue =
//                            // "<a href='"+findFirst.get().getUri()+"'><input type='button' class='btn btn-primary btn-lg btn-block' value='"
//                            // + newValue + "' /></a>";
//                            newValue = newValue;
//                            props.put("_href", findFirst.get().getUri());
//                        } else {
//                            newValue = "<a href='" + findFirst.get().getUri() + "'>" + newValue + "</a>";
//                        }
//                    }
//                }
//            }
//        }
//
//        if (newValue != null) {
//            props.put(f.getName(), newValue);
//        }
//        return props;
//    }
//
//    private boolean showMobilePage(Request request) {
//        if (RequestUtils.isMobile(request)) {
//            return true;
//        }
//        String page = CookiesUtils.getMainPageFromCookie(request);
//        return page != null && page.equals("indexMobile");
//    }
//
//    public boolean isSubmitField() {
//        return f.isSubmitField();
//    }
//
//}
