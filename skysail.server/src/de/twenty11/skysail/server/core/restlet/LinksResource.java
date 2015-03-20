//package de.twenty11.skysail.server.core.restlet;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.restlet.Application;
//import org.restlet.data.Cookie;
//import org.restlet.data.CookieSetting;
//import org.restlet.data.Header;
//import org.restlet.data.Method;
//import org.restlet.resource.ClientResource;
//import org.restlet.util.Series;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import io.skysail.api.responses.Linkheader;
//import io.skysail.api.responses.SkysailResponse;
//
//public class LinksResource extends EntityServerResource<Node> {
//
//    private static final Logger logger = LoggerFactory.getLogger(LinksResource.class);
//
//    private Application app;
//
//    private Set<Node> nodes = new HashSet<>();
//
//    public LinksResource() {
//        app = getApplication();
//    }
//
//    @Override
//    public Node getEntity() {
//
//        String host = getRequest().getHostRef().toString();
//
//        Series<CookieSetting> cookieSettings = getResponse().getCookieSettings();
//        CookieSetting credentialCookie = cookieSettings.getFirst("Credentials");
//        Series<Cookie> cookies = new Series<>(Cookie.class);
//        cookies.add(credentialCookie);
//
//        Node rootNode = new Node("/" + app.getName());
//        process(rootNode, host, cookies);
//
//        return rootNode;
//    }
//
//    private void process(Node from, String host, Series<Cookie> cookies) {
//        if (from == null) {
//            return;
//        }
//        if (nodes.contains(from)) {
//            return;
//        }
//        nodes.add(from);
//        addEdges(from, host, cookies);
//        Set<Node> newNodes = addNodes(from);
//        from.getLinks().stream().forEach(link -> process(link.getTo(), host, cookies));
//        nodes.addAll(newNodes);
//    }
//
//    private void addEdges(Node node, String host, Series<Cookie> cookies) {
//        String uri = host + node.getPath();
//        logger.debug("checking path '{}'", uri);
//        ClientResource cr = new ClientResource(uri);
//        cr.setCookies(cookies);
//        node.addLinks(determineLinks(cr, node));
//    }
//
//    private Set<Node> addNodes(Node rootNode) {
//        return rootNode.getLinks().stream().map(link -> createNode(link)).collect(Collectors.toSet());
//    }
//
//    private Node createNode(Edge edge) {
//        if (edge.getTo() != null) {
//            return null;
//        }
//        Link link = edge.getLinkheader();
//        if (link.getVerbs().contains(Method.GET)) {
//            final Node node = new Node(link.getUri());
//            if (nodes.contains(node)) {
//                edge.setTo(nodes.stream().filter(n -> n.getPath().equals(node.getPath())).collect(Collectors.toList())
//                        .get(0));
//            } else {
//                // nodes.add(node);
//                edge.setTo(node);
//                return node;
//            }
//        }
//        return null;
//    }
//
//    private List<Edge> determineLinks(ClientResource cr, Node node) {
//        cr.head();
//        Map<String, Object> responseMap = cr.getResponseAttributes();
//        if (responseMap == null) {
//            return Collections.emptyList();
//        }
//        @SuppressWarnings("unchecked")
//        Series<Header> headers = (Series<Header>) responseMap.get("org.restlet.http.headers");
//        String linkheaders = headers.getFirstValue("Link");
//        if (linkheaders == null) {
//            return Collections.emptyList();
//        }
//        return Arrays.asList(linkheaders.split(",")).stream().map(link -> new Edge(node, Link.valueOf(link)))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public String getId() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
// }
