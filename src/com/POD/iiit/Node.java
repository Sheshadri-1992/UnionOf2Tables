package com.POD.iiit;

//import com.POD.iiit.BPlusTree.Node;
import static com.POD.iiit.MyValues.*;

class Node {
	public int numberOfKeys = 0;
	public int[] keys = new int[2 * T - 1];
	public Object[] records = new Object[2 * T - 1];
	public Node[] childNodes = new Node[2 * T];
	public boolean leafNode;
	public Node nextNode;
	public int fileNO;
}