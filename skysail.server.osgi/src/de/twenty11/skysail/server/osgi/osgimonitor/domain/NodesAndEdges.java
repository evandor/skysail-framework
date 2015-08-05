package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

public class NodesAndEdges {

	public class Edge {

		private long id1;
		private long id2;
		private long cnt = 1;

		public Edge(long id1, long id2) {
			this.id1 = id1;
			this.id2 = id2;
		}

		public long getSource() {
			return id1;
		}

		public long getTarget() {
			return id2;
		}

		public long getValue() {
	        return cnt;
        }

		public void incrementCnt() {
			cnt++;
		}

		@Override
        public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + getOuterType().hashCode();
	        result = prime * result + (int) (id1 ^ (id1 >>> 32));
	        result = prime * result + (int) (id2 ^ (id2 >>> 32));
	        return result;
        }

		@Override
        public boolean equals(Object obj) {
	        if (this == obj)
		        return true;
	        if (obj == null)
		        return false;
	        if (getClass() != obj.getClass())
		        return false;
	        Edge other = (Edge) obj;
	        if (!getOuterType().equals(other.getOuterType()))
		        return false;
	        if (id1 != other.id1)
		        return false;
	        if (id2 != other.id2)
		        return false;
	        return true;
        }

		private NodesAndEdges getOuterType() {
	        return NodesAndEdges.this;
        }

		@Override
		public String toString() {
		    return id1 + " -> " + id2 + " #" + cnt;
		}

	}

	public class Node {

		private String symbolicName;
		private long bundleId;

		public Node(long bundleId, String symbolicName) {
			this.bundleId = bundleId;
			this.symbolicName = symbolicName;
		}

		public String getName() {
			return symbolicName;
		}

		public long getBundleId() {
			return bundleId;
		}

		public int getType() {
			return 2;
		}

	}

	private List<Node> nodes = new ArrayList<Node>();

	private List<Edge> edges = new ArrayList<Edge>();

	public void addNode(Bundle bundle) {
		nodes.add(new Node(bundle.getBundleId(), bundle.getSymbolicName()));
	}

	public void addEdge(long id1, long id2) {
		Edge edge = new Edge(id1, id2);
		if (edges.contains(edge)) {
			for (Edge e : edges) {
	            if (e.equals(edge)) {
	            	e.incrementCnt();
	            }
            }
		} else {
			edges.add(edge);
		}
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getLinks() {
		return edges;
	}

	public Integer getNodeIndex(long bundleId) {
		for (Node node : nodes) {
			if (node.getBundleId() == bundleId) {
				return nodes.indexOf(node);
			}
		}
		return null;
	}

}
