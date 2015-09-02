//package de.twenty11.skysail.server.core.restlet;
//
//import java.util.Map;
//
//import org.restlet.data.Header;
//import org.restlet.data.Method;
//import org.restlet.representation.Representation;
//import org.restlet.util.Series;
//
//public class LinkNode {
//
//    private String text;
//
//    /**
//     * Constructor.
//     *
//     * @param responseMap
//     * @param head
//     */
//    public LinkNode(Map<String, Object> responseMap, Representation head) {
//        if (responseMap != null) {
//            @SuppressWarnings("unchecked")
//            Series<Header> headers = (Series<Header>) responseMap.get("org.restlet.http.headers");
//            String linkheaders = headers.getFirstValue("Link");
//            text = linkheaders;
//            // Link.valueOf(linkheader)
//        } else {
//            text = head.toString();
//        }
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void getEdges(Method... verbs) {
//
//    }
//
//}
