package parser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import components.Connection;
import components.Node;
import devices.BJT;
import devices.Mosfet;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import lib.BuildingBlockList;

public class GraphCircuit {

	Graph<Node, Connection> g = new UndirectedSparseGraph<>();
	public static HashMap<Integer, Node> hm = new HashMap<>();
	ArrayList<Integer> t = new ArrayList<>();
	private static HashMap<Node, Integer> blockList = new HashMap<>();

	public Graph<Node, Connection> createGraph(List<Node> list) throws FileNotFoundException {
		ListBB.getInstance().priorityList();
		Constraint c = new Constraint();
		for (Node n : list) {
			if (n instanceof Mosfet) {
				Mosfet m = (Mosfet) n;
				g.addVertex(m);
				this.connectMosfetNode(m, m.getDrain(), m.getDrainID());
				this.connectMosfetNode(m, m.getSource(), m.getSourceID());
				this.connectMosfetNode(m, m.getGate(), m.getGateID());
			} else if (n instanceof BJT) {
				BJT b = (BJT) n;
				g.addVertex(b);
				this.connectBJTNode(b, b.getBase(), b.getBaseID());
				this.connectBJTNode(b, b.getEmitter(), b.getEmitterID());
				this.connectBJTNode(b, b.getCollector(), b.getCollectorID());
			}
		}
		PriorityList p = new PriorityList();
		HashMap<Node, Integer> hm = this.compareCircuit(g);
		p.getBuildingBlocks(hm);
		BuildingBlockList bb = new BuildingBlockList();
		List<String> list1 = bb.findBB_Level3(g);
		c.writeRules(list1);
		c.toFile(c.getRuleBook());
		return g;

	}

	public void connectBJTNode(BJT n, int i, int id) {
		if (hm.containsKey(i)) {
			Node n1 = hm.get(i);
			if (n1 instanceof BJT) {
				Connection c = new Connection();
				BJT m1 = (BJT) n1;
				if (m1.getEmitter() == i) {
					if (m1.getName().equals(n.getName())) {
						c.addConnection("1_" + id, "1_" + m1.getEmitterID());
					} else if (i == n.getEmitter()) {
						c.addConnection("1_" + id, "2_" + m1.getEmitterID());
					} else {
						c.addConnection("1_" + m1.getEmitterID(), "2_" + id);
					}
				} else if (m1.getCollector() == i) {
					if (m1.getName().equals(n.getName())) {
						if (m1.getName().equals(n.getName())) {
							c.addConnection("1_" + id, "1_" + m1.getCollectorID());
						} else {
							c.addConnection("1_" + id, "2_" + m1.getCollectorID());
						}
					}
				} else {
					if (m1.getName().equals(n.getName())) {
						c.addConnection("1_" + m1.getBaseID(), "1_" + id);
					} else {
						c.addConnection("1_" + m1.getBaseID(), "2_" + id);
					}
				}
				if (m1 != n) {
					c.addNodes(n1);
					c.addNodes(n);
				} else {
					c.addNodes(n1);
				}

				g.addEdge(c, c.getNodes(), EdgeType.UNDIRECTED);
			}
		} else {
			hm.put(i, n);
		}
	}

	public void connectMosfetNode(Mosfet n, int i, int id) {
		if (hm.containsKey(i)) {
			Node n1 = hm.get(i);
			if (n1 instanceof Mosfet) {
				Connection c = new Connection();
				Mosfet m1 = (Mosfet) n1;
				if (m1.getSource() == i) {
					if (m1.getName().equals(n.getName())) {
						c.addConnection("1_" + id, "1_" + m1.getSourceID());
					} else if (i == n.getDrain()) {
						c.addConnection("1_" + id, "2_" + m1.getSourceID());
					} else {
						c.addConnection("1_" + m1.getSourceID(), "2_" + id);
					}
				} else if (m1.getGate() == i) {
					if (m1.getName().equals(n.getName())) {
						if (m1.getName().equals(n.getName())) {
							c.addConnection("1_" + id, "1_" + m1.getGateID());
						} else {
							c.addConnection("1_" + id, "2_" + m1.getGateID());
						}
					}
				} else {
					if (m1.getName().equals(n.getName())) {
						c.addConnection("1_" + m1.getDrainID(), "1_" + id);
					} else {
						c.addConnection("1_" + m1.getDrainID(), "2_" + id);
					}
				}
				if (m1 != n) {
					c.addNodes(n1);
					c.addNodes(n);
				} else {
					c.addNodes(n1);
				}

				g.addEdge(c, c.getNodes(), EdgeType.UNDIRECTED);
			}
		} else {
			hm.put(i, n);
		}
	}

	public HashMap<Node, Integer> compareCircuit(Graph<Node, Connection> g) throws FileNotFoundException {
		BuildingBlockList bb = new BuildingBlockList();
		Collection<Node> c1 = g.getVertices();
		Iterator<Node> i = c1.iterator();
		while (i.hasNext()) {
			Node n1 = i.next();
			if (n1 instanceof Mosfet) {
				Collection<Connection> connect1 = g.getInEdges(n1);
				Iterator<Connection> iter1 = connect1.iterator();
				while (iter1.hasNext()) {
					Graph<Node, Connection> subGraph = new UndirectedSparseGraph<Node, Connection>();
					subGraph.addVertex(n1);
					Connection connection1 = iter1.next();
					subGraph.addEdge(connection1, connection1.getNodes(), EdgeType.UNDIRECTED);
					Constraint c = new Constraint();
					List<String> list1 = new LinkedList<>();
					list1 = bb.findBuildingBlock(subGraph, n1, blockList);
					c.writeRules(list1);
					c.toFile(c.getRuleBook());

				}

			}
		}
		return blockList;
	}

	

}
