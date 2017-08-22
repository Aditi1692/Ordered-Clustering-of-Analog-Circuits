package lib;

import components.Connection;
import components.Node;
import devices.Mosfet;
import edu.uci.ics.jung.graph.Graph;

public abstract class BuildingBlocks {

	public abstract <V, E> Graph<Node, Connection> createBuildingBlock();
	
	public abstract boolean isBuildingBlock(Graph<Node, Connection> g);
	
	public abstract boolean getSizingRule(Mosfet n1);

	public abstract int getPriority();
	

}
