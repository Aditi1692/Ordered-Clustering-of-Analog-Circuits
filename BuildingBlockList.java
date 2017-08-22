package lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import components.Connection;
import components.Node;
import devices.Mosfet;
import edu.uci.ics.jung.graph.Graph;
import parser.ListBB;

public class BuildingBlockList {
	private BuildingBlocks bb;
	boolean differentialPair = false;
	boolean crossCoupledPair = false;
	boolean levelShifter = false;
	boolean currentMirror = false;
	boolean cascodePair = false;
	boolean currentMirrorLoad = false;
	boolean voltRef1 = false;
	boolean voltRef2 = false;
	boolean vccs = false;

	public BuildingBlockList() {

	}

	public List<String> findBuildingBlock(Graph<Node, Connection> subGraph, Node n1, HashMap<Node, Integer> hm) {
		List<String> list = new LinkedList<>();
		bb = new DifferentialPair();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			if (!hm.containsKey(n1)) {
				list.add("Recognised Block: Differential Pair" + " " + ((Mosfet) n1).getName());
				System.out.println("Recognised Block: Differential Pair" + " " + ((Mosfet) n1).getName());
				bb.getSizingRule((Mosfet) n1);
				hm.put(n1, bb.getPriority());
			} else {
				if (hm.get(n1) < bb.getPriority()) {
					list.add("Dominance Rule applied to the following block");
					list.add("Recognised Block: Differential Pair" + " " + ((Mosfet) n1).getName());
					System.out.println("Dominance Rule applied to the following block");
					System.out.println("Recognised Block: Differential Pair" + " " + ((Mosfet) n1).getName());
					bb.getSizingRule((Mosfet) n1);
					hm.put(n1, bb.getPriority());
				}
			}
		}

		bb = new LevelShifter();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			if (!hm.containsKey(n1)) {
				list.add("Recognised Block: Level Shifter" + " " + ((Mosfet) n1).getName());
				System.out.println("Recognised Block: Level Shifter" + " " + ((Mosfet) n1).getName());
				bb.getSizingRule((Mosfet) n1);
				hm.put(n1, bb.getPriority());
			} else {
				if (hm.get(n1) < bb.getPriority()) {
					list.add("Dominance Rule applied to the following block");
					list.add("Recognised Block: Level Shifter" + " " + ((Mosfet) n1).getName());
					System.out.println("Dominance Rule applied to the following block");
					System.out.println("Recognised Block: Level Shifter" + " " + ((Mosfet) n1).getName());
					bb.getSizingRule((Mosfet) n1);
					hm.put(n1, bb.getPriority());
				}
			}
		}

		bb = new SimpleCurrentMirror();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			bb.getSizingRule((Mosfet) n1);
			if (!hm.containsKey(n1)) {
				list.add("Recognised Block: Current Mirror" + " " + ((Mosfet) n1).getName());
				System.out.println("Recognised Block: Current Mirror" + " " + ((Mosfet) n1).getName());
				bb.getSizingRule((Mosfet) n1);
				hm.put(n1, bb.getPriority());
			} else {
				if (hm.get(n1) < bb.getPriority()) {
					System.out.println("Dominance Rule applied to the following block");
					list.add("Recognised Block: Current Mirror" + " " + ((Mosfet) n1).getName());
					System.out.println("Recognised Block: Current Mirror" + " " + ((Mosfet) n1).getName());
					bb.getSizingRule((Mosfet) n1);
					hm.put(n1, bb.getPriority());
				}
			}
		}

		bb = new CascodePair();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			if (!hm.containsKey(n1)) {
				list.add("Recognised Block: Cascode Pair" + " " + ((Mosfet) n1).getName());
				System.out.println("Recognised Block: Cascode Pair" + " " + ((Mosfet) n1).getName());
				bb.getSizingRule((Mosfet) n1);
				hm.put(n1, bb.getPriority());
			} else {
				if (hm.get(n1) < bb.getPriority()) {
					list.add("Dominance Rule applied to the following block");
					list.add("Recognised Block: Cascode Pair" + " " + ((Mosfet) n1).getName());
					System.out.println("Dominance Rule applied to the following block");
					System.out.println("Recognised Block: Cascode Pair" + " " + ((Mosfet) n1).getName());
					bb.getSizingRule((Mosfet) n1);
					hm.put(n1, bb.getPriority());
				}
			}
		}

		bb = new CrossCoupledPair();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			if (!hm.containsKey(n1)) {
				list.add("Recognised Block: Cross Coupled Pair" + " " + ((Mosfet) n1).getName());
				System.out.println("Recognised Block: Cross Coupled Pair" + " " + ((Mosfet) n1).getName());
				bb.getSizingRule((Mosfet) n1);
				hm.put(n1, bb.getPriority());
			} else {
				if (hm.get(n1) < bb.getPriority()) {
					list.add("Dominance Rule applied to the following block");
					list.add("Recognised Block: Cross Coupled Pair" + " " + ((Mosfet) n1).getName());
					System.out.println("Dominance Rule applied to the following block");
					System.out.println("Recognised Block: Cross Coupled Pair" + " " + ((Mosfet) n1).getName());
					bb.getSizingRule((Mosfet) n1);
					hm.put(n1, bb.getPriority());
				}
			}
		}
		return list;
	}

	public List<String> findBB_Level3(Graph<Node, Connection> g) {
		List<String> list = new LinkedList<>();
		HashMap<Integer, Mosfet> map1 = new HashMap<>();
		HashMap<Node, Integer> map2 = ListBB.getHm();
		for (Map.Entry<Node, Integer> entry : map2.entrySet()) {
			if (entry.getValue() == 1) {
				map1.put(1, (Mosfet) entry.getKey());
				differentialPair = true;
			} else if (entry.getValue() == 2) {
				map1.put(2, (Mosfet) entry.getKey());
				crossCoupledPair = true;
			} else if (entry.getValue() == 3) {
				map1.put(3, (Mosfet) entry.getKey());
				levelShifter = true;
			} else if (entry.getValue() == 4) {
				map1.put(4, (Mosfet) entry.getKey());
				currentMirror = true;
			} else if (entry.getValue() == 5) {
				map1.put(5, (Mosfet) entry.getKey());
				cascodePair = true;
			} else if (entry.getValue() == 6) {
				map1.put(6, (Mosfet) entry.getKey());
				currentMirrorLoad = true;
			} else if (entry.getValue() == 7) {
				map1.put(7, (Mosfet) entry.getKey());
				voltRef2 = true;
			} else if (entry.getValue() == 8) {
				map1.put(8, (Mosfet) entry.getKey());
				voltRef1 = true;
			} else if (entry.getValue() == 9) {
				map1.put(9, (Mosfet) entry.getKey());
				vccs = true;
			}
			// Cascode current mirror
			if (levelShifter == true && currentMirror == true) {
				Mosfet m1 = (Mosfet) map1.get(3);
				Mosfet m2 = (Mosfet) map1.get(4);
				Collection<Node> neighborNodes = g.getNeighbors(m1);
				if (neighborNodes != null) {
					Iterator<Node> iNode = neighborNodes.iterator();
					while (iNode.hasNext()) {
						if (m2.equals(iNode.next())) {
							list.add("Recognised block: Cascode current mirror" + " "
									+ ((Mosfet) m1).getName() + " "+ ((Mosfet) m2).getName());
							System.out.println("Recognised block: Cascode current mirror" + " "
									+ ((Mosfet) m1).getName() + " "+ ((Mosfet) m2).getName());
						
						}
					}
				}
			}

			// improved wilson current mirror
			if (levelShifter == true && currentMirror == true) {
				Mosfet m1 = (Mosfet) map1.get(3);
				Mosfet m2 = (Mosfet) map1.get(4);
				Collection<Node> neighborNodes = g.getNeighbors(m1);
				if (neighborNodes != null) {
					Iterator<Node> iNode = neighborNodes.iterator();
					while (iNode.hasNext()) {
						if (m2.equals(iNode.next())) {
							list.add("Recognised block: Improved wilson current mirror" + " " + ((Mosfet) m1).getName()
									+ ((Mosfet) m2).getName());
							System.out.println("Recognised block: Improved wilson current mirror" + " " + ((Mosfet) m1).getName()
									+ ((Mosfet) m2).getName());
						}
					}
				}
			}

			// wilson current mirror
			if (voltRef1 == true && currentMirror == true) {
				Mosfet m1 = (Mosfet) map1.get(8);
				Mosfet m2 = (Mosfet) map1.get(4);
				Collection<Node> neighborNodes = g.getNeighbors(m1);
				Iterator<Node> iNode = neighborNodes.iterator();
				while (iNode.hasNext()) {
					if (m2.equals(iNode.next())) {
						list.add("Recognised block: Wilson current mirror" + " " + ((Mosfet) m1).getName() + ((Mosfet) m2).getName());
						System.out.println("Recognised block: Wilson current mirror" + " " + ((Mosfet) m1).getName() + ((Mosfet) m2).getName());
						
					}
				}

			}

			// transistor current mirror
			if (currentMirror == true && voltRef1 == true) {
				Mosfet m1 = (Mosfet) map1.get(3);
				Mosfet m2 = (Mosfet) map1.get(4);
				Collection<Node> neighborNodes = g.getNeighbors(m1);
				Iterator<Node> iNode = neighborNodes.iterator();
				while (iNode.hasNext()) {
					if (m2.equals(iNode.next())) {
						list.add("Recognised block: Transistor current mirror" + " " + ((Mosfet) m1).getName() + ((Mosfet) m2).getName());
						System.out.println("Recognised block: Transistor current mirror" + " " + ((Mosfet) m1).getName() + ((Mosfet) m2).getName());
					}
				}
			}
		}
		return list;
	}

	public void compareWilsonCurrentMirror(Graph<Node, Connection> subGraph, Mosfet n1, HashMap<Node, Integer> hm) {
		bb = new WilsonCurrentMirror();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			hm.put(n1, bb.getPriority());
		}

	}

	public void compareCascodeCurrentMirror(Graph<Node, Connection> subGraph, Mosfet n1, HashMap<Node, Integer> hm) {
		bb = new CascodeCurrentMirror();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			hm.put(n1, bb.getPriority());
		}

	}

	public void comparefourTransistorCurrentMirror(Graph<Node, Connection> subGraph, Mosfet n1,
			HashMap<Node, Integer> hm) {
		bb = new TransistorCurrentMirror();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			hm.put(n1, bb.getPriority());
		}

	}

	public void compareImprovedWilsonCurrentMirror(Graph<Node, Connection> subGraph, Mosfet n1,
			HashMap<Node, Integer> hm) {
		bb = new TransistorCurrentMirror();
		bb.createBuildingBlock();
		if (bb.isBuildingBlock(subGraph)) {
			hm.put(n1, bb.getPriority());
		}

	}

}
