package common;

import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

public class SinkImplementation implements Sink {

	private Parser p;

	public SinkImplementation(Parser p) {
		this.p = p;
	}

	public void process(EntityContainer entityContainer) {
		Entity entity = entityContainer.getEntity();
		if (entity instanceof Node) {
			// Create a new node...
			MyNode node = new MyNode(entity.getId(),
					((Node) entity).getLatitude(),
					((Node) entity).getLongitude(), entity.getTags());

			// add it to the list...simple.

			p.getAllNodes().put(node.getId(), node);

		} else if (entity instanceof Way) {

			// If it finds a way, create a new way
			MyWay way = new MyWay(entity.getId(), entity.getTags(),
					((Way) entity).getWayNodes());

			// Now cycle through all the waypoints it has. The waypoints
			// are only a reference to nodes, so I must search their ID
			// against the complete list of nodes and add those that are
			// necessary to the "complete road" list in the way.
			for (int i = 0; i < way.getWayNodes().size(); i++) {
				way.getCompleteRoads().add(
						p.getAllNodes().get(
								way.getWayNodes().get(i).getNodeId()));

			}

			// for (MyNode node : way.getCompleteRoads()) {
			// System.out.println(node);
			// }

			// Add the way to complete list of ways.
			p.getAllWays().add(way);

		} else if (entity instanceof Relation) {
			// Not interested at this moment in time- will be useful in
			// future

		}
	}

	public void release() {
	}

	public void complete() {

		// generateEdges();
		p.generateSuccessors();
	}

	@Override
	public void initialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}
}
