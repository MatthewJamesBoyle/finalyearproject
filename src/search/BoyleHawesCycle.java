package search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import common.MyNode;
import common.Parser;
import common.Printer;
import common.SearchNode;

public class BoyleHawesCycle extends Search {

	double runLength;
	double radius;
	double diameter;
	private final double EARTH_RADIUS = 6371;
	private MyNode centreNode;
	private Queue<SearchNode> forRoute;
	double routeLength = 0;
	double divisor = 1;
	double angle;
	public Queue<SearchNode> route;
	private int cycle = 0;
	private double bestThresh = 1;
	private double currentBest = 0;
	private double bestRouteLength, routeLengthToCheck = 100;

	public BoyleHawesCycle(MyNode startNode, Parser p, double runLength,
			int angle) {
		super(startNode, startNode, p);
		this.runLength = runLength;
		if (!getStartNode().isJunction) {
			System.out.println("set it as a junction");
			getStartNode().setJunction(true);
		} else {
			System.out.println("was already a junction");
		}

		// initally 2, but may have to rerun the search if no route of
		// reasonable length is found
		diameter = (runLength / divisor) / Math.PI;
		radius = diameter / 2;
		this.angle = angle;
		centreNode = getAllNodes().get(getPoint(startNode, angle));
	}

	private long getPoint(MyNode start, double bearing) {
		// The id of the closest node we have on record to the centre
		long centre;
		// /This chunk gets the latitude of the point radius away from the
		// centre at the bearing given.
		double lat1R = Math.toRadians(start.getLat());
		double asRad = Math.asin(Math.sin(lat1R)
				* Math.cos(radius / EARTH_RADIUS) + Math.cos(lat1R)
				* Math.sin(radius / EARTH_RADIUS)
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
								* Math.sin(radius / EARTH_RADIUS)
								* Math.cos(lat1R),
						Math.cos(radius / EARTH_RADIUS) - Math.sin(lat1R2)
								* Math.sin(lat2R));

		double centreLon = Math.toDegrees(asrad);
		// System.out.println("centre is " + centreLat + "," + centreLon);
		// Printer.writeCircleTest(centreLat, centreLon);

		centre = super.findClosestNode(centreLat, centreLon);

		return centre;

	}

	public void getCircularPoints() {
		MyNode start = null, end = null;
		MyNode tempStart = null;
		route = new LinkedList<SearchNode>();
		Queue<MyNode> toSearch = new LinkedList<MyNode>();
		boolean first = true;

		for (int i = 0; i < 360; i++) {
			if ((angle==90 && i==270)||(angle==0&&i==180)||angle==270&&i==90) {
				toSearch.add(this.getStartNode());
				System.out.println("ADDED THE STARTNODE");
			}
			if (getAllNodes().get(getPoint(centreNode, i)).isJunction == false) {
				continue;
			} else {
				if (first) {
					tempStart = (getAllNodes().get(getPoint(centreNode, i)));
					first = false;

				}
				toSearch.add(getAllNodes().get(getPoint(centreNode, i)));

				if (toSearch.size() > 1) {
					start = toSearch.poll();
					end = toSearch.peek();

					AStar star = new AStar(start, end, super.getParse());
					Stack<SearchNode> temp = star.Search();
					Collections.reverse(temp);
					route.addAll(temp);
				}

			}
		}

		// last search to join it up
		start = toSearch.poll();

		end = tempStart;
		AStar star = new AStar(start, end, super.getParse());
		Stack<SearchNode> temp = star.Search();
		Collections.reverse(temp);
		route.addAll(temp);
		System.out.println("Cycle created with " + route.size()
				+ " nodes in it");
		forRoute = new LinkedList<SearchNode>(route);

		while (!forRoute.isEmpty()) {
			SearchNode n = forRoute.poll();

			if (forRoute.size() > 1) {

				routeLength = routeLength
						+ Search.distFrom(n.getLat(), n.getLon(), forRoute
								.peek().getLat(), forRoute.peek().getLon());

			}
		}
		if (cycle <= 4) {
			checkRouteLength();
		}

	}

	// This needs to be smarter
	private void checkRouteLength() {
		cycle++;

		if (cycle == 5) {
			System.out.println("--------referring to best ---------------");
			forRoute.clear();
			routeLength = 0;

			diameter = (runLength / bestThresh) / Math.PI;

			System.out.println("the diameter is " + diameter);
			radius = diameter / 2;
			System.out.println("the radius is  " + radius);
			// centreNode = getAllNodes().get(getPoint(getStartNode(), angle));

			getCircularPoints();

			return;
		}

		System.out.println("-----------------------CYCLE " + cycle
				+ "---------------------------");
		routeLengthToCheck = Math.abs(routeLength - runLength);

		currentBest = Math.abs(bestRouteLength - runLength);
		System.out.println("best diameter = " + bestThresh);

		System.out.println("best thresh:" + bestThresh);
		System.out.println("best routeLength: " + bestRouteLength);
		System.out.println("routeLength to check: " + routeLengthToCheck);
		System.out.println("currentBest : " + currentBest);

		if (routeLengthToCheck < currentBest) {
			System.out.println("was better");
			bestThresh = divisor;
			bestRouteLength = routeLength;

		}

		if (routeLength < (runLength - 1)) {

			System.out.println("route was too small(" + routeLength + ")");
			divisor = divisor - 0.1;
			// can't divide by 0
			if (divisor <= 0) {
				System.out.println("about to return");
				return;
			}
			routeLength = 0;
			forRoute.clear();
			diameter = (runLength / divisor) / Math.PI;

			radius = diameter / 2;
			// centreNode = getAllNodes().get(getPoint(getStartNode(), angle));

			getCircularPoints();

		} else if (routeLength > (runLength + 1)) {
			System.out.println("route was too big (" + routeLength + ")");
			routeLength = 0;
			divisor = divisor + 0.5;
			forRoute.clear();

			diameter = (runLength / divisor) / Math.PI;
			radius = diameter / 2;
			// centreNode = getAllNodes().get(getPoint(getStartNode(), angle));

			getCircularPoints();

		}
	}

	public double getRouteLength() {
		System.out.println("in here");
		while (!route.isEmpty()) {

			Printer.writeArrayForPoly(route.poll());

		}
		while (!forRoute.isEmpty()) {
			SearchNode n = forRoute.poll();

			if (forRoute.size() > 1) {

				routeLength = routeLength
						+ Search.distFrom(n.getLat(), n.getLon(), forRoute
								.peek().getLat(), forRoute.peek().getLon());

			}
		}

		return routeLength;
	}
}
