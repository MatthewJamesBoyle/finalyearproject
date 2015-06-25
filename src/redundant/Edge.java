package redundant;

import java.util.ArrayList;

import common.MyNode;



public class Edge {

	@Override
	public String toString() {
		return "Edge [startNode=" + startNode + ", endNode=" + endNode
				+ ", length=" + length + "]";
	}

	private MyNode startNode, endNode;
	private double length;
	private ArrayList<Edge> successor;
	private long id;
	private Edge parent;

	protected Edge getParent() {
		return parent;
	}

	public void setParent(Edge parent) {
		this.parent = parent;
	}

	public Edge(long id, MyNode startNode, MyNode endNode, double length) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;
		this.successor = new ArrayList<Edge>();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public MyNode getStartNode() {
		return startNode;
	}

	public void setStartNode(MyNode startNode) {
		this.startNode = startNode;
	}

	public MyNode getEndNode() {
		return endNode;
	}

	public void setEndNode(MyNode endNode) {
		this.endNode = endNode;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public ArrayList<Edge> getSuccessor() {
		
		return successor;
	}

	public void setSuccessor(ArrayList<Edge> successor) {
		this.successor = successor;
	}

}
