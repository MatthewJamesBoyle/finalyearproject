package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

import redundant.*;
import search.*;

public class Parser {

	// The input file.
	File file;
	// This hashmap contains all of the nodes in the xml file. The key is their
	// id.
	private HashMap<Long, MyNode> allNodes;
	private HashSet<MyNode> unique;
	private Collection<String> interestedTags;

	// This contains a list of all ways. A way then contains all the nodes that
	// make it up.

	private ArrayList<MyWay> allWays;

	// This was a list of edges that I was going to use. I didn't in the end.
	// You can find the Edge class in the redundant package. Might need it again
	private ArrayList<Edge> edgeList;

	// Constructor for parser.
	public Parser(File file) {
		this.file = file;
		allNodes = new HashMap<Long, MyNode>();
		allWays = new ArrayList<MyWay>();
		edgeList = new ArrayList<Edge>();
		unique = new HashSet<MyNode>();
		interestedTags = new ArrayList<String>();
		interestedTags.add("highway");
		try {
			parse();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HashMap<Long, MyNode> getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(HashMap<Long, MyNode> allNodes) {
		this.allNodes = allNodes;
	}

	public ArrayList<MyWay> getAllWays() {
		return allWays;
	}

	public void setAllWays(ArrayList<MyWay> allWays) {
		this.allWays = allWays;
	}

	public void parse() throws FileNotFoundException {
		SinkImplementation sinkImplementation = new SinkImplementation(this);

		// final Sink sinkImplementation = new Sink() {
		// public void process(EntityContainer entityContainer) {
		// Entity entity = entityContainer.getEntity();
		// if (entity instanceof Node) {
		// // Create a new node...
		// MyNode node = new MyNode(entity.getId(),
		// ((Node) entity).getLatitude(),
		// ((Node) entity).getLongitude(), entity.getTags());
		//
		// // add it to the list...simple.
		//
		// allNodes.put(node.getId(), node);
		//
		// } else if (entity instanceof Way) {
		//
		// // If it finds a way, create a new way
		// MyWay way = new MyWay(entity.getId(), entity.getTags(),
		// ((Way) entity).getWayNodes());
		//
		// // Now cycle through all the waypoints it has. The waypoints
		// // are only a reference to nodes, so I must search their ID
		// // against the complete list of nodes and add those that are
		// // necessary to the "complete road" list in the way.
		// for (int i = 0; i < way.getWayNodes().size(); i++) {
		// way.getCompleteRoads().add(
		// allNodes.get(way.getWayNodes().get(i)
		// .getNodeId()));
		//
		// }
		//
		// // for (MyNode node : way.getCompleteRoads()) {
		// // System.out.println(node);
		// // }
		//
		// // Add the way to complete list of ways.
		// allWays.add(way);
		//
		// } else if (entity instanceof Relation) {
		// // Not interested at this moment in time- will be useful in
		// // future
		//
		// }
		// }
		//
		// public void release() {
		// }
		//
		// public void complete() {
		//
		// // generateEdges();
		// generateSuccessors();
		// }
		//
		// @Override
		// public void initialize(Map<String, Object> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// };

		CompressionMethod compression = CompressionMethod.None;

		RunnableSource reader = new XmlReader(file, false, compression);

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		readerThread.start();

		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				/* do nothing */
			}
		}
	}

	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public HashSet<MyNode> getUnique() {
		return unique;
	}

	public void generateSuccessors() {

		for (MyWay way : getAllWays()) {
			if (way.getCompleteRoads().size() == 2) {
				MyNode toGenerateFor = way.getCompleteRoads().get(0);

				handleAddToArray(toGenerateFor, way.getCompleteRoads().get(1));
			}

			if (way.getCompleteRoads().size() > 2) {

				int startIndex = 0;
				int endIndex = 1;
				while (endIndex < way.getCompleteRoads().size()) {
					MyNode toGenerateFor = way.getCompleteRoads().get(
							startIndex);
					MyNode successor = way.getCompleteRoads().get(endIndex);
					// toGenerateFor.getSuccessors().add(successor);
					handleAddToArray(toGenerateFor, successor);

					// successor.getSuccessors().add(toGenerateFor);
					handleAddToArray(successor, toGenerateFor);

					startIndex++;
					endIndex++;

				}
			}

		}
		setJunctions();


	}
	
	
	private void setJunctions() {
		for (MyNode node : allNodes.values()) {
			if (node.getSuccessors().size() > 2) {
				node.isJunction = true;
			}
		}
	}

	private void handleAddToArray(MyNode toGenerateFor, MyNode successor) {
		if (toGenerateFor.getSuccessors().size() < 1) {
			toGenerateFor.getSuccessors().add(successor);
		}

		else if (toGenerateFor.getSuccessors().size() > 0) {
			if (successor.getId() < toGenerateFor.getSuccessors().get(0)
					.getId()) {
				toGenerateFor.getSuccessors().add(0, successor);
			} else {
				toGenerateFor.getSuccessors().add(successor);
			}
		}

	}

	public void generateEdges() {

		long id = 0;

		for (MyWay way : getAllWays()) {

			if (way.getCompleteRoads().size() == 2) {
				MyNode start = way.getCompleteRoads().get(0);
				int j = 1;
				if (way.getCompleteRoads().get(0).getId() == way
						.getCompleteRoads()
						.get(way.getCompleteRoads().size() - 1).getId()) {
					j = 2;
				}

				// -1 leads to "closed ways- need to explore why this is
				// exactly. http://wiki.openstreetmap.org/wiki/Elements
				MyNode end = way.getCompleteRoads().get(
						way.getCompleteRoads().size() - j);

				double size = Search.distFrom(start.getLat(), start.getLon(),
						end.getLat(), end.getLon());
				Edge edge = new Edge(id, start, end, size);
				edgeList.add(edge);

				id++;
			}
			if (way.getCompleteRoads().size() > 2) {

				int startIndex = 0;
				int endIndex = 1;
				while (endIndex < way.getCompleteRoads().size()) {
					MyNode start = way.getCompleteRoads().get(startIndex);
					MyNode end = way.getCompleteRoads().get(endIndex);
					double size = Search.distFrom(start.getLat(),
							start.getLon(), end.getLat(), end.getLon());
					Edge edge = new Edge(id, start, end, size);

					edgeList.add(edge);
					startIndex++;
					endIndex++;
					id++;

				}

			}

		}

	}

	private void generateEdgeSuccessors() {
		ArrayList<Edge> edgeListCopy = edgeList;

		for (Edge edgeRoot : edgeList) {
			for (Edge edgeSuccess : edgeListCopy) {
				if (edgeRoot.getEndNode().getId() == edgeSuccess.getStartNode()
						.getId()) {
					edgeRoot.getSuccessor().add(edgeSuccess);
					edgeSuccess.setParent(edgeRoot);
				}

				if (edgeRoot.getStartNode().getId() == edgeSuccess.getEndNode()
						.getId()) {
					edgeRoot.getSuccessor().add(edgeSuccess);
					edgeSuccess.setParent(edgeRoot);

				}

			}

		}

	}

	public Edge getEdge(MyNode start, MyNode end) {
		Edge toReturn = null;
		for (Edge edge : edgeList) {
			if (edge.getStartNode() == start) {
				if (edge.getEndNode() == end) {
					toReturn = edge;
					break;
				}

			}

		}
		return toReturn;
	}

}
