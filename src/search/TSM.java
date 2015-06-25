package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import org.apache.xerces.util.SynchronizedSymbolTable;

import common.MyNode;
import common.Parser;
import common.Printer;
import common.SearchNode;

// For each generated city
// work out the distance from the current city to it
// if it is the shortest, take this one.(a*).
// add the start city to the visited set.
// repeat
// if all cities are in the visited set then connect final city to start.
// check the Threshold
// if within threshold ,stop
// check search max
// if not outside of search max start again
// if outside of search max,return route closest to threshold.

public class TSM extends Search {

	private ArrayList<MyNode> cities;
	private Queue<MyNode> visited;
	private double threshold, routeLength, wantedLength, searchesRun,
			bestRouteLength, lastResortLength;
	private final double EARTH_RADIUS = 6371;
	private final double MAXIMUM_SEARCHES = 10;
	private boolean acceptSuitable;
	public Queue<SearchNode> bestRoute, lastResort, test;

	private int citiesAmount = 4;

	public TSM(MyNode startNode, Parser p, double wantedLength,
			double threshold, boolean fast) {
		super(startNode, null, p);
		cities = new ArrayList<MyNode>();
		cities.add(startNode);
		visited = new LinkedList<MyNode>();
		this.wantedLength = wantedLength;
		this.threshold = threshold;
		routeLength = 0;
		bestRouteLength = 0;
		lastResortLength = wantedLength * 2;
		acceptSuitable = fast;
		bestRoute = new LinkedList<SearchNode>();
		lastResort = new LinkedList<SearchNode>();

		generateCities(citiesAmount);
	}

	/**
	 * Generates amount many nodes that are 0 - n/2 distance away from the start
	 * node. Then find a node in getAllNodes() that is closes to that lat lang
	 * and adds it to cities
	 * 
	 * @param amount
	 */
	private void generateCities(int amount) {

		Random rand = new Random();

		double bearing = rand.nextDouble() * 60;
		double distance = (wantedLength / 2) * rand.nextDouble();
		for (int i = 0; i < amount; i++) {

			double cityLat = getCityLat(bearing, this.getStartNode().getLat(),
					distance);
			double cityLong = getCityLong(bearing,
					this.getStartNode().getLon(), distance, this.getStartNode()
							.getLat(), cityLat);
			Long newCityLong = this.findClosestNode(cityLat, cityLong);
			MyNode newCity = super.getAllNodes().get(newCityLong);

			cities.add(newCity);
			bearing = rand.nextDouble() * 60;
			distance = (wantedLength - 3) * rand.nextDouble();

		}

		// cities.add(getAllNodes().get(new Long("2758769717")));
		// cities.add(getAllNodes().get(new Long("59909117")));
		// cities.add(getAllNodes().get(new Long("277646817")));
	}

	/**
	 * visitCities finds gets the first city from Cities and finds which
	 * neighbour city is closest. Once it finds which is closest, it performs A*
	 * Star search to that city and adds the inital city to the visited set. We
	 * repeat this for all nodes in Cities.
	 */
	public void visitCities() {
		// while cities is >1
		while (cities.size() >= 2) {
			// pop the first node
			MyNode first = cities.get(0);
			// add it to visited.
			visited.add(first);
			// set a distance bench mark
			double distance = Search.distFrom(first.getLat(), first.getLon(),
					cities.get(1).getLat(), cities.get(1).getLon());
			// Sets the next node into visited to be peek to start with
			MyNode next = cities.get(1);

			cities.remove(0);
			// get a dist from the first to each remaining point
			for (MyNode n : cities) {
				if (Search.distFrom(first.getLat(), first.getLon(), n.getLat(),
						n.getLon()) < distance) {

					distance = Search.distFrom(first.getLat(), first.getLon(),
							n.getLat(), n.getLon());
					next = n;
				}

			}
			// whichever is smallest, add this to visited next.
			cities.remove(next);
			cities.add(0, next);
			// pop the next node and repeat.
		}
		// There will be one node left in cities- need to move this over.
		if (cities.size() == 1) {
			visited.add(cities.get(0));
			cities.clear();
		}

		visited.add(this.getStartNode());

		Search();

	}

	public void Search() {
		Queue<SearchNode> route = new LinkedList<SearchNode>();

		while (visited.size() > 1) {
			MyNode start = visited.poll();
			MyNode goal = visited.peek();
			AStar star = new AStar(start, goal, super.getParse());

			// temp stack for working out the length
			Stack<SearchNode> temp = star.Search();

			// add all the STACK to a QUEUE here. this would be the overall
			// result

			Collections.reverse(temp);
			route.addAll(temp);
		}

		// route length is worked out here.
		Queue<SearchNode> tempRoute = new LinkedList<SearchNode>(route);
		while (!tempRoute.isEmpty()) {
			SearchNode n = tempRoute.poll();
			if (tempRoute.size() > 1) {
				routeLength = routeLength
						+ Search.distFrom(n.getLat(), n.getLon(), tempRoute
								.peek().getLat(), tempRoute.peek().getLon());
			}

		}

		if (Math.abs(wantedLength - routeLength) <= threshold
				&& Math.abs(wantedLength - routeLength) >= 0
				&& searchesRun < MAXIMUM_SEARCHES) {
			searchesRun++;
			// start again.
			// Repeat until we are within threshold or max searches has gone
			// over.
			// remember our best guess so far in case we reach the max case
			// searchesRun++;

			if (acceptSuitable) {
				bestRouteLength = routeLength;
				bestRoute.clear();

				bestRoute.addAll(route);
				route.clear();
				System.out.println("found a suitable route of length "
						+ bestRouteLength);
				// print(bestRoute);
				System.out.println(route.size());

				return;
			}

			if (routeLength == wantedLength) {
				System.out.println("found the exact right distance");
				bestRoute.clear();
				bestRoute.addAll(route);
				route.clear();

				bestRouteLength = routeLength;
				// print(bestRoute);
				return;

			} else if (Math.abs(routeLength - bestRouteLength) < Math
					.abs(wantedLength) - bestRouteLength
					&& searchesRun < MAXIMUM_SEARCHES) {
				System.out.println("new best route found");

				bestRouteLength = routeLength;
				bestRoute.clear();

				bestRoute.addAll(route);
				route.clear();

				routeLength = 0;
				generateCities(citiesAmount);
				visitCities();
			} else {
				routeLength = 0;
				route.clear();
				generateCities(citiesAmount);
				visitCities();
			}

		} else if (Math.abs(wantedLength - routeLength) >= threshold
				&& searchesRun < MAXIMUM_SEARCHES) {
			searchesRun++;
			// System.out.println("hit else if 1");

			routeLength = 0;
			route.clear();
			generateCities(citiesAmount);
			visitCities();
		}

		else if (searchesRun == MAXIMUM_SEARCHES && bestRouteLength == 0) {
			System.out.println("raised the threshold");
			if (threshold == 0.1) {
				threshold = 0.5;
			} else if (threshold == 0.5) {
				threshold = 0.75;
			} else if (threshold == 0.75) {
				threshold = 1;
			}

			searchesRun = 0;
			routeLength = 0;
			route.clear();
			generateCities(citiesAmount);
			visitCities();

		}

		else {

			System.out.println("best route found in " + searchesRun
					+ " searches");
			System.out.println("best route length is " + bestRouteLength);

			print(bestRoute);

		}

	}

	public double getCityLat(double bearing, double lat1, double distance) {
		double lat1R = Math.toRadians(lat1);
		double asRad = Math.asin(Math.sin(lat1R)
				* Math.cos(distance / EARTH_RADIUS) + Math.cos(lat1R)
				* Math.sin(distance / EARTH_RADIUS) * Math.cos(bearing));
		return Math.toDegrees(asRad);

	}

	public double getCityLong(double bearing, double long1, double distance,
			double lat1, double lat2) {
		double long1R = Math.toRadians(long1);
		double lat1R = Math.toRadians(lat1);
		double lat2R = Math.toRadians(lat2);
		double asrad = long1R
				+ Math.atan2(
						Math.sin(bearing) * Math.sin(distance / EARTH_RADIUS)
								* Math.cos(lat1R),
						Math.cos(distance / EARTH_RADIUS) - Math.sin(lat1R)
								* Math.sin(lat2R));

		return Math.toDegrees(asrad);
	}

	public double getRouteLength() {
		return bestRouteLength;
	}

	private void print(Queue<SearchNode> bestRoute) {
		while (!bestRoute.isEmpty()) {
			Printer.writeArrayForPoly(bestRoute.poll());
		}

	}

}
