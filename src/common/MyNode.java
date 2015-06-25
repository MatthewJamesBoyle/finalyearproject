package common;

import java.util.ArrayList;
import java.util.Collection;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

//This class is my representation of a node and is used throughout my searches.
public class MyNode implements Comparable<MyNode> {

	// This will be the ID of the node. For less intelligent search, they will
	// be ordered.
	private long id;
	// Latitude and longitude of the node.
	private double lat, lon;
	// Tags are used for adding extra information to a node. For example
	// "one way"
	private Collection<Tag> tags;

	public boolean isJunction = false;

	// A list of the nodes that this node can attained by expanding this node.
	private ArrayList<MyNode> successors;

	// The node that was expanded to get to this node.
	// private MyNode parent;
	// heuristic function for A star search.
	private double g, h, f = 0;

	// Constructor for the class -straight forward.
	public MyNode(long id, double lat, double lon, Collection<Tag> tags) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.tags = tags;
		this.successors = new ArrayList<MyNode>();
	}

	// ############Obvious setters and getters##############################
	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public ArrayList<MyNode> getSuccessors() {
		return successors;
	}

	public void setSuccessors(ArrayList<MyNode> successors) {
		this.successors = successors;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Collection<Tag> getTags() {
		return tags;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	// ##############end getters #############

	// ########Printing methods ###################
	public String printTags() {

		String toPrint = "";
		Object[] tagsArr = tags.toArray();
		for (int i = 0; i < tagsArr.length; i++) {
			toPrint = toPrint + tagsArr[i];
		}

		return toPrint;
	}

	@Override
	public String toString() {
		return "node [id=" + id + ", lat=" + lat + ", lon=" + lon + "]";

	}

	@Override
	public int compareTo(MyNode node) {
		if (this.getF() < node.getF()) {
			return -1;
		}
		if (this.getF() > node.getF()) {
			return 1;
		} else
			return 0;

	}

	// #########End printing methods ##############

	public boolean equals(Object o) {
		if (o instanceof MyNode) {
			return this.getId() == ((MyNode) o).getId();
		}
		return false;
	}

	public void setJunction(boolean toSet) {
		isJunction = toSet;
	}
}
