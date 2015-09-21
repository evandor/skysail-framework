//package io.skysail.server.app.designer;
//
//import io.skysail.server.restlet.resources.ListServerResource;
//
//import java.util.List;
//import java.util.Map;
//
//public class DynamicListResource extends ListServerResource<Map<String,Object>> {
//
//    @Override
//    public List<Map<String,Object>> getEntity() {
//         return ((DesignerApplication) getApplication()).getRepository().findAll("select from ");
//    }
//}
//
