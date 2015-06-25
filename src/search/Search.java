package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import redundant.Edge;

import common.MyNode;
import common.MyWay;
import common.Parser;

public class Search {

	private MyNode startNode, goalNode;
	// private final static File file = new File("testFileB.xml");
	// private final static File file = new File("map.osm.xml");
	private final static File file = new File("allSelly.xml");
	// private final static File file = new File("overpassturbodata.xml");
	// private final static File file = new File("LA.xml");

	private ArrayList<MyWay> allWays;
	private HashMap<Long, MyNode> allNodes;
	private ArrayList<Edge> edgeList;
	private double distance;
	private static Parser parse;

	public Parser getParse() {
		return parse;
	}

	public Search(MyNode startNode, MyNode goalNode, Parser p) {
		if (parse == null) {
			parse = new Parser(file);
		}
		this.startNode = startNode;
		this.goalNode = goalNode;
		allNodes = p.getAllNodes();
		allWays = p.getAllWays();
		edgeList = p.getEdgeList();

	}

	protected ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	protected void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public Search(File file) {
		if (parse == null) {
			parse = new Parser(file);
		}
		allNodes = parse.getAllNodes();
		allWays = parse.getAllWays();
		edgeList = parse.getEdgeList();

	}

	public static void main(String[] args) {

		double lat = 52.4447400;
		double lon = -1.9292630;
//		 try {
//		 Search.generateData(lat, lon);
//		 System.out.println("Searched");
//		 } catch (IOException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
		Search search = new Search(new File("route.xml"));
		long start = search.findClosestNode(lat, lon);
		System.out.println(start);
		MyNode startNode = search.getParse().getAllNodes().get(start);

		search.setStartNode(startNode);

		// TSM route = new TSM(startNode, search.getParse(), 6, 0.1, false);
		// route.visitCities();

		BoyleHawesCycle test = new BoyleHawesCycle(startNode,
				search.getParse(),20, 90);
		test.getCircularPoints();
		System.out.println("route is of length " + test.getRouteLength());

	}

	protected MyNode getStartNode() {
		return startNode;
	}

	protected void setStartNode(MyNode startNode) {
		this.startNode = startNode;
	}

	protected MyNode getGoalNode() {
		return goalNode;
	}

	protected void setGoalNode(MyNode goalNode) {
		this.goalNode = goalNode;
	}

	protected ArrayList<MyWay> getAllWays() {
		return allWays;
	}

	protected void setAllWays(ArrayList<MyWay> allWays) {
		this.allWays = allWays;
	}

	protected HashMap<Long, MyNode> getAllNodes() {
		return allNodes;
	}

	protected void setAllNodes(HashMap<Long, MyNode> allNodes) {
		this.allNodes = allNodes;
	}

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371; // kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	public long findClosestNode(double lat, double lon) {
		long toReturn = 0;
		double value = 1000;
		double distance = 0;
		for (MyNode node : allNodes.values()) {
			distance = Search.distFrom(node.getLat(), node.getLon(), lat, lon);
			if (distance < value) {
				toReturn = node.getId();
				value = distance;
			}

		}

		return toReturn;
	}

	// unfinished
	public static void generateData(double lat, double lon) throws IOException {
		double southLat = lat - 0.02;
		double northLat = lat + 0.03;
		double westLon = lon - 0.1;
		double eastLon = lon + 0.1;

		System.out.println(southLat);
		System.out.println(northLat);
		System.out.println(westLon);
		System.out.println(eastLon);

		String urlParameters = "<osm-script output=\"xml\" timeout=\"25\">"
				+ " <!-- gather results -->"
				+ "<union>"
				+ " <query type=\"node\">"
				+ " <has-kv k=\"highway\" regv=\"(trunk|primary|secondary|tertiary|trunk|residential)\"/>"
				+

				"  <bbox-query s=\""
				+ southLat
				+ "\" w=\""
				+ westLon
				+ "\" n=\""
				+ northLat
				+ "\" e=\""
				+ eastLon
				+ "\"/>"
				+ "</query>"
				+ " <query type=\"way\">"
				+ "<has-kv k=\"highway\" regv=\"(trunk|primary|secondary|tertiary|trunk|residential)\"/>"
				+

				"  <bbox-query s=\""
				+ southLat
				+ "\" w=\""
				+ westLon
				+ "\" n=\""
				+ northLat
				+ "/"
				+ "\" e=\""
				+ eastLon
				+ "\"/>"
				+ "  </query>"
				+ "  </union>"
				+ "  <union>"
				+ "   <item/>"
				+ "  <recurse type=\"down\"/>"
				+ "  </union>"
				+ " <print mode=\"meta\" order=\"quadtile\"/>" +

				"</osm-script>";
		URL url = new URL("http://overpass-api.de/api/interpreter");
		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream());

		writer.write(urlParameters);
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		File file = new File("route.xml");
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		while ((line = reader.readLine()) != null) {
			output.write(line);
			output.newLine();
			// System.out.println(line);
		}
		writer.close();
		reader.close();
		output.close();
	}
}
