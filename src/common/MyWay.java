package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

//A way is a representation of a road provided by OSM. I have made my own representation which will be used here
public class MyWay {

	// id of way
	private long id;

	// Way tages are the same as for nodes, gives some extra info about the
	// roads (one way)
	private Collection<Tag> wayTags;

	// A waynode is just a list of longs that reference the id of the nodes.
	private List<WayNode> wayNodes;

	// I make my own arrayList of MyNodes that contain the full nodes, not just
	// a reference
	private ArrayList<MyNode> completeRoads;

	// constructos, obvs
	public MyWay(long id, Collection<Tag> tags, List<WayNode> wayNodes) {
		this.id = id;
		this.wayTags = tags;
		this.wayNodes = wayNodes;
		completeRoads = new ArrayList<MyNode>();
	}

	// ####################### List of setters and getters
	// ######################
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Collection<Tag> getWayTags() {
		return wayTags;
	}

	public void setWayTags(Collection<Tag> wayTags) {
		this.wayTags = wayTags;
	}

	public List<WayNode> getWayNodes() {
		return wayNodes;
	}

	public void setWayNodes(List<WayNode> wayNodes) {
		this.wayNodes = wayNodes;
	}

	public ArrayList<MyNode> getCompleteRoads() {
		return completeRoads;
	}

	public void setCompleteRoads(ArrayList<MyNode> completeRoads) {
		this.completeRoads = completeRoads;
	}

	// #########End of getters and setters ##########

	// ################ Printing methods ############
	@Override
	public String toString() {
		return "MyWay [id=" + id + ", wayTags=" + printTags() + ", wayNodes="
				+ wayNodes + "]";
	}

	public String printTags() {

		String toPrint = "";
		Object[] tagsArr = wayTags.toArray();
		for (int i = 0; i < tagsArr.length; i++) {
			toPrint = toPrint + tagsArr[i];
		}

		return toPrint;
	}
	// ####End of printing ########

}
