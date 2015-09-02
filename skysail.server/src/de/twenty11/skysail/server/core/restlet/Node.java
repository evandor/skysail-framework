//package de.twenty11.skysail.server.core.restlet;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Node {
//
//    private String path;
//    private List<Edge> links = new ArrayList<>();
//
//    public Node(String path) {
//        this.path = path;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void addLinks(List<Edge> list) {
//        links = list;
//    }
//
//    public List<Edge> getLinks() {
//        return links;
//    }
//
//    @Override
//    public String toString() {
//        return new StringBuilder("'").append(path).append("' with ").append(links.size()).append(" links").toString();
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((path == null) ? 0 : path.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        Node other = (Node) obj;
//        if (path == null) {
//            if (other.path != null)
//                return false;
//        } else if (!path.equals(other.path))
//            return false;
//        return true;
//    }
//
//
//
//}
