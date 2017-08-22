package lib;

import java.util.Collection;

import components.Connection;
import components.Node;
import devices.Mosfet;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import parser.ListBB;

public class CascodePair extends BuildingBlocks {

	private Mosfet m1 = new Mosfet();
	private Mosfet m2 = new Mosfet();
	private Connection c = new Connection();

	@Override
	public <V, E> Graph<Node, Connection> createBuildingBlock() {

		Graph<Node, Connection> target = new UndirectedSparseGraph<>();

		target.addVertex(m1);
		target.addVertex(m2);

		c.addNodes(m1);
		c.addNodes(m2);
		c.addConnection("1_" + m1.getSourceID(), "2_" + m2.getDrainID());

		target.addEdge(c, c.getNodes(), EdgeType.UNDIRECTED);

		return target;
	}

	@Override
	public boolean isBuildingBlock(Graph<Node, Connection> g) {
		Collection<Connection> c1 = g.getEdges();
		for (Connection connection : c1) {
			if (!connection.isB()) {
				if (connection.getConnection().equals(c.getConnection())) {
					connection.setB(true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean getSizingRule(Mosfet m1) {
		return false;
	}

	@Override
	public int getPriority() {
		return ListBB.getPriorityMap().get(this);
	}

}
