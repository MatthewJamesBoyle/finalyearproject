package search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import common.MyNode;
import common.Parser;
import common.Printer;
import common.SearchNode;

public class SquaredCycle extends Search {
	double runLength;
	double sqaureRadius;
	private final double EARTH_RADIUS = 6371;
	private MyNode centreNode;

	public SquaredCycle(MyNode startNode, Parser p, double runLength) {
		super(startNode, startNode, p);
		this.runLength = runLength;
		sqaureRadius = runLength / 8;
		centreNode = getAllNodes().get(getPoint(startNode, 45));
	}

	private long getPoint(MyNode start, double bearing) {
		// The id of the closest node we have on record to the centre
		long centre;
		// /This chunk gets the latitude of the point radius away from the
		// centre at the bearing given.
		double lat1R = Math.toRadians(start.getLat());
		double asRad = Math.asin(Math.sin(lat1R)
				* Math.cos(sqaureRadius / EARTH_RADIUS) + Math.cos(lat1R)
				* Math.sin(sqaureRadius / EARTH_RADIUS)
				* Math.cos(Math.toRadians(bearing)));
		double centreLat = Math.toDegrees(asRad);

		// /This chunk gets the longitude of the point radius away from the
		// centre at the bearing given. Note how we must find the lat first as
		// this is dependant.

		double long1R = Math.toRadians(start.getLon());
		double lat1R2 = Math.toRadians(start.getLat());
		double lat2R = Math.toRadians(centreLat);
		double asrad = long1R
				+ Math.atan2(
						Math.sin(Math.toRadians(bearing))
								* Math.sin(sqaureRadius / EARTH_RADIUS)
								* Math.cos(lat1R),
						Math.cos(sqaureRadius / EARTH_RADIUS)
								- Math.sin(lat1R2) * Math.sin(lat2R));

		double centreLon = Math.toDegrees(asrad);
		// System.out.println("centre is " + centreLat + "," + centreLon);
		// Printer.writeCircleTest(centreLat, centreLon);

		centre = super.findClosestNode(centreLat, centreLon);

		return centre;

	}

	public void getSquareCities() {
		Queue<MyNode> toSearch = new LinkedList<MyNode>();
		Queue<SearchNode> route = new LinkedList<SearchNode>();

		toSearch.add(getStartNode());
		toSearch.add(super.getAllNodes().get(getPoint(centreNode, 135)));
		toSearch.add(super.getAllNodes().get(getPoint(centreNode, 90)));
		toSearch.add(super.getAllNodes().get(getPoint(centreNode, 315)));
		toSearch.add(this.getStartNode());

			 while (toSearch.size() > 1) {
				MyNode start = toSearch.poll();
				MyNode end = toSearch.peek();
				Printer.writeCircleTest(start);
				AStar star = new AStar(start, end, super.getParse());
				Stack<SearchNode> temp = star.Search();
				Collections.reverse(temp);
				route.addAll(temp);
				System.out.println("searching");
			
		}
		System.out.println("done searching");

		while (!route.isEmpty()) {
			Printer.writeArrayForPoly(route.poll());
		}

		System.out.println("done");
	}

}
