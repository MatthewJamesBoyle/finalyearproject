package common;

public class SearchNode implements Comparable<SearchNode> {
	private long id;
	private double lon, lat;
	private SearchNode parent;

	public SearchNode(long id, double lon, double lat, SearchNode parent) {
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.parent = parent;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public SearchNode getParent() {
		return parent;
	}

	public void setParent(SearchNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "SearchNode [id=" + id + ", lon=" + lon + ", lat=" + lat + "]";
	}

	@Override
	public int compareTo(SearchNode o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
