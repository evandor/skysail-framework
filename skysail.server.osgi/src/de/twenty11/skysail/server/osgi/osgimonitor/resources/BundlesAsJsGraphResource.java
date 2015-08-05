//package de.twenty11.skysail.server.ext.osgimonitor.resources;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.restlet.data.MediaType;
//import org.restlet.representation.Representation;
//import org.restlet.representation.StringRepresentation;
//import org.restlet.resource.ClientResource;
//import org.restlet.resource.Get;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.common.graphs.EdgeProvider;
//import de.twenty11.skysail.common.graphs.NodeProvider;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource2;
//import de.twenty11.skysail.server.ext.osgimonitor.OsgiMonitorViewerApplication;
//
///**
// * 
// */
//public class BundlesAsJsGraphResource extends SkysailServerResource2 { // extends ListServerResource<NodeProvider> {
//
//    private List<Bundle> bundles;
//
//    /** deals with json objects */
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public BundlesAsJsGraphResource() {
//        setName("osgimonitor 'bundles as Javascript Graph' resource");
//        setDescription("The resource containing the list of bundles displayed as a graph");
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
//        BundleContext bundleContext = app.getBundleContext();
//        if (bundleContext == null) {
//            bundles = Collections.emptyList();
//        } else {
//            bundles = Arrays.asList(bundleContext.getBundles());
//        }
//    }
//
//    @Get
//    public Representation getJSGraph() throws JsonParseException, JsonMappingException, IOException {
//
//        List<NodeProvider> graphRepresentation = getGraphRepresentation();
//        List<String> nodes = new ArrayList<String>();
//        StringBuffer edgesHtml = new StringBuffer();
//        StringBuffer nodesHtml = new StringBuffer();
//
//        for (NodeProvider node : graphRepresentation) {
//            for (EdgeProvider edge : node.getEdges()) {
//                if (!nodes.contains(edge.getSource())) {
//                    nodes.add(edge.getSource());
//                    nodesHtml
//                            .append("  g.addNode(\"")
//                            .append(edge.getSource())
//                            .append("\", { label : \"" + getNodeLabel(graphRepresentation, edge.getSource())
//                                    + "\" });\n");
//                }
//                if (!nodes.contains(edge.getTarget())) {
//                    nodes.add(edge.getTarget());
//                    nodesHtml
//                            .append("  g.addNode(\"")
//                            .append(edge.getTarget())
//                            .append("\", { label : \"" + getNodeLabel(graphRepresentation, edge.getTarget())
//                                    + "\" });\n");
//                }
//                edgesHtml.append(" g.addEdge(\"").append(edge.getSource()).append("\", \"").append(edge.getTarget())
//                        .append("\");\n");
//            }
//        }
//        
//        // @formatter:off
//         StringBuffer sb = new StringBuffer("<!DOCTYPE html>\n")
//         .append("<html><head><title>Skysail Json Html Viewer</title>\n")
//         .append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/../static/css/default.css\">\n")
//         .append("<script type=\"text/javascript\" src=\"/../static/js/graphdracula/raphael-min.js\"></script>\n")
//         .append("<script type=\"text/javascript\" src=\"/../static/js/graphdracula/dracula_graffle.js\"></script>\n")
//         .append("<script type=\"text/javascript\" src=\"/../static/js/graphdracula/jquery-1.4.2.min.js\"></script>\n")
//         .append("<script type=\"text/javascript\" src=\"/../static/js/graphdracula/dracula_graph.js\"></script>\n")
//         .append("<script type=\"text/javascript\">\n")
//         .append("var redraw, g, renderer;\n\n")
//         .append("window.onload = function() {\n")
//         .append("  var width = $(document).width() - 20;\n")
//         .append("  var height = $(document).height() - 60;\n")
//         .append("  g = new Graph();\n")
//         .append("  ").append(nodesHtml)
//         .append("  ").append(edgesHtml)
//         //.append("  g.addNode(\"cherry\");\n")
//         //.append("  g.addEdge(\"strawberry\", \"cherry\");\n")
//         .append("  var layouter = new Graph.Layout.Spring(g);\n")
//         .append("  renderer = new Graph.Renderer.Raphael('canvas', g, width, height);\n")
//         //.append("  redraw = function() {\n")
//         //.append("    layouter.layout();\n")
//         .append("    renderer.draw();\n")
//         //.append("  };\n")
//         .append("};\n")
//         .append("</script>\n")
//         .append("</head>\n<body><div id=\"canvas\"></div>\n")
//         .append("\n</body>\n</html>");
//        // @formatter:on
//
//        Representation representation = new StringRepresentation(sb.toString());
//        representation.setMediaType(MediaType.TEXT_HTML);
//        return representation;
//    }
//
//    private List<NodeProvider> getGraphRepresentation() throws IOException, JsonParseException, JsonMappingException {
//        ClientResource columns = new ClientResource("riap://application/bundles/asGraph");
//        columns.setChallengeResponse(getChallengeResponse());
//        Representation representation = columns.get();
//        SkysailResponse<List<NodeProvider>> response = mapper.readValue(representation.getText(),
//                new TypeReference<SkysailResponse<List<NodeProvider>>>() {
//                });
//        return response.getData();
//    }
//
//    private String getNodeLabel(List<NodeProvider> payload, String nodeId) {
//        for (NodeProvider node : payload) {
//            if (node.getNodeId().equals(nodeId)) {
//                return node.getNodeLabel();
//            }
//        }
//        return "unknown";
//    }
//
// }
