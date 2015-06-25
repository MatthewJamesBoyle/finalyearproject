package common;

import java.util.ArrayList;
import java.util.Collections;

public class SortedNodeList {

	ArrayList<MyNode> sortedList;

	public SortedNodeList() {
		sortedList = new ArrayList<MyNode>();

	}

	public void push(MyNode node) {
		sortedList.add(node);
		Collections.sort(sortedList);

	}

	public MyNode pop() {
		return sortedList.remove(0);

	}

	public ArrayList<MyNode> getSortedList() {
		return sortedList;
	}

}
