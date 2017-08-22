package lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import components.Connection;
import components.Node;
import devices.Mosfet;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import parser.Constraint;
import parser.ListBB;

public class DifferentialPair extends BuildingBlocks {

	private Mosfet m1 = new Mosfet();
	private Mosfet m2 = new Mosfet();
	private final Connection c = new Connection();

	public DifferentialPair() {

	}

	@Override
	public <V, E> Graph<Node, Connection> createBuildingBlock() {
		Graph<Node, Connection> target = new UndirectedSparseGraph<>();
		target.addVertex(m1);
		c.addNodes(m1);
		c.addNodes(m2);
		c.addConnection("1_" + m1.getSourceID(), "2_" + m2.getSourceID());

		target.addEdge(c, c.getNodes(), EdgeType.UNDIRECTED);
		return target;
	}

	@Override
	public boolean isBuildingBlock(Graph<Node, Connection> g) {
		boolean b1 = true;
		Collection<Connection> c1 = g.getEdges();
		for (Connection connection : c1) {
			if (!connection.getConnection().equals(c.getConnection())){
				b1 = false;
				break;
			}
		}
		return b1;
	}

	@Override
	public boolean getSizingRule(Mosfet m1) {
		List<String> list = new ArrayList<>();
		list.add("l1 = l2");
		list.add("w1 = w2");
		list.add("| vds2 - vds1 | <= Vdsmax(dp)");
		list.add("|vgs2 - vgs1 | <= Vgsmax");
		Constraint c = new Constraint();
		c.writeRules(list, m1);
		return false;
	}

	public int getPriority() {
		return ListBB.getPriorityMap().get("Differential Pair");
	}

}
