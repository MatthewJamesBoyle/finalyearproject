package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import common.MyNode;
import common.Parser;
import common.SearchNode;


//DONT FORGET YOU CHANGED YOUR COMPARITOR METHOD IN NODE FOR A*+. YOU NEED TO CHANGE IT TO RUN THIS ONE!!!!
public class AStar extends Search {

	private ArrayList<MyNode> openList; // This is basically our frontier
	private ArrayList<MyNode> closedList;// This is the visited set.
	private Map<Long, SearchNode> allSearchNodes;

	public AStar(MyNode startNode, MyNode goalNode, Parser p) {
		super(startNode, goalNode, p);
		openList = new ArrayList<MyNode>();
		closedList = new ArrayList<MyNode>();
		allSearchNodes = new HashMap<Long, SearchNode>();

	}

	public Stack<SearchNode> Search() {
		Stack<SearchNode> toReturn = new Stack<SearchNode>();
		openList.add(this.getStartNode());
		allSearchNodes.put(this.getStartNode().getId(), new SearchNode(this
				.getStartNode().getId(), getStartNode().getLon(),
				getStartNode().getLat(), null));

		while (!openList.isEmpty()) {

			Collections.sort(openList);
			MyNode q = openList.remove(0);
			openList.remove(q);

			// generate q's 8 successors and set their parents to q
			// if successor is the goal, stop the search
			if (isGoal(q)) {
//				System.out.println("search completed- goal found");
				// printRoute();
				SearchNode i = allSearchNodes.get(q.getId());
				while (i.getParent() != null) {
					// System.out.println(i.getId());
					toReturn.add(i);
					// Printer.writeArrayForPoly(i);
					i = i.getParent();
				}

				SearchNode start = new SearchNode(this.getStartNode().getId(),
						getStartNode().getLon(), getStartNode().getLat(), null);
				toReturn.add(start);
				// Printer.writeArrayForPoly(start);

				break;

			}

			closedList.add(q);
			ArrayList<MyNode> successors = q.getSuccessors();

			for (MyNode node : successors) {
				if (closedList.contains(node)) {
					continue;
				}

				// node.setParent(q);
				allSearchNodes.put(
						node.getId(),
						new SearchNode(node.getId(), node.getLon(), node
								.getLat(), allSearchNodes.get(q.getId())));

				/*
				 * successor.g = q.g + distance between successor and q
				 * successor.h = distance from goal to successor
				 * successor.f=successor.g + successor.h
				 */

				double g = q.getG()
						+ Search.distFrom(q.getLat(), q.getLon(),
								node.getLat(), node.getLon());

				double h = Search.distFrom(this.getGoalNode().getLat(), this
						.getGoalNode().getLon(), q.getLat(), q.getLon());

				node.setG(g);
				node.setH(h);

				node.setF(g + h);

				// if a node with the same position as successor is in the OPEN
				// list
				// has a lower f than successor, skip this successor

				int openIndex = openList.indexOf(node);
				int closedIndex = closedList.indexOf(node);

				if (openIndex > -1) {

					if (openList.get(openIndex).compareTo(node) == -1)
						continue;

				}

				// if a node with the same position as successor is in the
				// CLOSED list
				// which has a lower f than successor, skip this successor
				if (closedIndex > -1) {
					if (closedList.get(closedIndex).compareTo(node) == -1)
						continue;

				}

				if (openIndex > -1)
					openList.remove(openIndex);
				Collections.sort(openList);

				if (closedIndex > -1)
					closedList.remove(closedIndex);

				openList.add(node);
				Collections.sort(openList);

			}
			closedList.add(0, q);

		}
		return toReturn;
	}

	public ArrayList<MyNode> getOpenList() {
		return openList;
	}

	public void setOpenList(ArrayList<MyNode> openList) {
		this.openList = openList;
	}

	public ArrayList<MyNode> getClosedList() {
		return closedList;
	}

	public void setClosedList(ArrayList<MyNode> closedList) {
		this.closedList = closedList;
	}

	// checks the goal and returns a boolean.
	private Boolean isGoal(MyNode node) {
		boolean toReturn = false;

		if (node.equals(this.getGoalNode())) {
			toReturn = true;

		}

		return toReturn;
	}

	public double printRouteLength(Stack<SearchNode> toGetResult) {

		double toReturn = 0;
		while (toGetResult.size() > 1) {
			SearchNode next = toGetResult.pop();
			toReturn += Search.distFrom(next.getLat(), next.getLon(),
					toGetResult.peek().getLat(), toGetResult.peek().getLon());

		}

		return toReturn;

	}
}