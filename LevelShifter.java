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

public class LevelShifter extends BuildingBlocks {

	private Mosfet m1 = new Mosfet();
	private Mosfet m2 = new Mosfet();
	private final Connection c = new Connection();

	public LevelShifter() {

	}

	
	@Override
	public <V, E> Graph<Node, Connection> createBuildingBlock() {
		Graph<Node, Connection> target = new UndirectedSparseGraph<>();

		target.addVertex(m1);

		Connection c = new Connection();
		c.addNodes(m1);
		c.addNodes(m2);
		c.addConnection("1_" + m1.getDrainID(), "2_" + m2.getGateID());
		c.addConnection("1_" + m1.getGateID(), "2_" + m2.getGateID());
		c.addConnection("1_" + m1.getDrainID(), "2_" + m1.getGateID());

		target.addEdge(c, c.getNodes(), EdgeType.UNDIRECTED);

		return target;
	}

	@Override
	public boolean isBuildingBlock(Graph<Node, Connection> g) {
		boolean b1 = true;
		Collection<Connection> c1 = g.getEdges();
		for (Connection connection : c1) {
			if (!connection.getConnection().equals(c.getConnection())) {
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
		list.add("vgs1,2 - Vth1,2 >= Vgsmin");
		Constraint c = new Constraint();
		c.writeRules(list, m1);
		return false;

	}

	public int getPriority() {
		return ListBB.getPriorityMap().get("Level Shifter");
	}

	public Mosfet getM1() {
		return m1;
	}

	public Mosfet getM2() {
		return m2;
	}
}
