package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import common.MyNode;
import common.Parser;
import common.Printer;
import common.SearchNode;



public class DFS extends Search {

	Stack<MyNode> frontier;
	LinkedList<MyNode> visited;
	ArrayList<MyNode> route;
	Parser p;
	boolean found = false;
	private int toWriteInt = 0;
	private Map<Long, SearchNode> allSearchNodes;

	public DFS(MyNode startNode, MyNode goalNode, Parser p) {
		super(startNode, goalNode, p);
		frontier = new Stack<MyNode>();
		visited = new LinkedList<MyNode>();
		this.p = p;
		route = new ArrayList<MyNode>();
		allSearchNodes = new HashMap<Long, SearchNode>();

	}

	public void Search() {

		visited.add(this.getStartNode());
		allSearchNodes.put(this.getStartNode().getId(), new SearchNode(this
				.getStartNode().getId(), getStartNode().getLon(),
				getStartNode().getLat(), null));

		if (isGoal(this.getStartNode())) {
			System.out.println("goal found at start");
			goalFound();
		}
		ArrayList<MyNode> successors = this.getStartNode().getSuccessors();

		for (int i = 0; i < successors.size(); i++) {
			allSearchNodes.put(successors.get(i).getId(),
					new SearchNode(successors.get(i).getId(), successors.get(i)
							.getLon(), successors.get(i).getLat(),
							allSearchNodes.get(this.getStartNode().getId())));
			if (!(visited.contains(successors.get(i)))
					&& !(frontier.contains(successors.get(i)))) {
				if (isGoal(successors.get(i))) {
					visited.add(successors.get(i));
					System.out.println("goal found at start successor");

					goalFound();
					break;
				} else {
					frontier.add(successors.get(i));

				}

			}

		}
		while (!frontier.isEmpty()) {
			MyNode current = frontier.pop();

			ArrayList<MyNode> currentSuccessors = current.getSuccessors();

			visited.add(current);

			for (int i = 0; i < currentSuccessors.size(); i++) {

				if (!(visited.contains(currentSuccessors.get(i)))
						&& !(frontier.contains(currentSuccessors.get(i)))) {

					allSearchNodes.put(currentSuccessors.get(i).getId(),
							new SearchNode(currentSuccessors.get(i).getId(),
									currentSuccessors.get(i).getLon(),
									currentSuccessors.get(i).getLat(),
									allSearchNodes.get(current.getId())));

					if (isGoal(currentSuccessors.get(i))) {
						visited.add(currentSuccessors.get(i));
						System.out.println("goal found in loop");
						goalFound();
						break;
					} else {
						frontier.add(currentSuccessors.get(i));
					}

				}

			}
		}
	}

	private boolean isGoal(MyNode toCheck) {
		boolean goal = false;
		if (toCheck.equals(this.getGoalNode())) {
			goal = true;
		}
		return goal;

	}

	private void goalFound() {
		System.out.println("goal found with " + visited.size());

		SearchNode i = allSearchNodes.get(this.getGoalNode().getId());
		while (i.getParent() != null) {
			System.out.println(i.getId());
			 Printer.writeArrayForPoly(i);
			i = i.getParent();
		}

		System.exit(0);

	}

}