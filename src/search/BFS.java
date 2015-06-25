package search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import common.MyNode;
import common.Parser;



public class BFS extends Search {

	Queue<MyNode> frontier;
	Queue<MyNode> visited;
	ArrayList<MyNode> route;
	Parser p;
	boolean found = false;
	private int toWriteInt = 0;
	private double distance;

	public BFS(MyNode startNode, MyNode goalNode, Parser p) {
		super(startNode, goalNode, p);
		frontier = new LinkedList<MyNode>();
		visited = new LinkedList<MyNode>();
		this.p = p;
		route = new ArrayList<MyNode>();

	}

	public void Search() {

		visited.add(this.getStartNode());

		if (isGoal(this.getStartNode())) {
			System.out.println("goal found at start");
			goalFound();

		}
		ArrayList<MyNode> successors = this.getStartNode().getSuccessors();

		for (int i = 0; i < successors.size(); i++) {
//			successors.get(i).setParent(this.getStartNode());
			if (!(visited.contains(successors.get(i)))
					&& !(frontier.contains(successors.get(i)))) {
				if (isGoal(successors.get(i))) {
					visited.add(successors.get(i));
					System.out.println("goal found at start successor");

					goalFound();
				} else {
					frontier.add(successors.get(i));

				}

			}

		}
		while (!frontier.isEmpty()) {
			MyNode current = frontier.poll();

			ArrayList<MyNode> currentSuccessors = current.getSuccessors();

			visited.add(current);

			for (int i = 0; i < currentSuccessors.size(); i++) {

				if (!(visited.contains(currentSuccessors.get(i)))
						&& !(frontier.contains(currentSuccessors.get(i)))) {
//					currentSuccessors.get(i).setParent(current);

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
//		printRoute();
		// System.exit(0);

	}

}