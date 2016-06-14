package com.POD.iiit;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static com.POD.iiit.MyValues.*;


public class BPlusTree {

	
	private Node mRootNode;
	
	public BPlusTree() {
		mRootNode = new Node();
		mRootNode.leafNode = true;		
	}
	
	public void add(int key, Object object) {
		Node rootNode = mRootNode;
		if (rootNode.numberOfKeys == (2 * T - 1)) {
			Node newRootNode = new Node();
			mRootNode = newRootNode;
			newRootNode.leafNode = false;
			mRootNode.childNodes[0] = rootNode;
			splitChildNode(newRootNode, 0, rootNode); // Split rootNode and move its median (middle) key up into newRootNode.
			insertIntoNonFullNode(newRootNode, key, object); // Insert the key into the B-Tree with root newRootNode.
		} else {
			insertIntoNonFullNode(rootNode, key, object); // Insert the key into the B-Tree with root rootNode.
		}
	}
	

	void splitChildNode(Node parentNode, int i, Node node) {
		Node newNode = new Node();
		newNode.leafNode = node.leafNode;
		newNode.numberOfKeys = T;
		for (int j = 0; j < T; j++) { // Copy the last T elements of node into newNode. Keep the median key as duplicate in the first key of newNode.
			newNode.keys[j] = node.keys[j + T - 1];
			newNode.records[j] = node.records[j + T - 1];
		}
		if (!newNode.leafNode) {
			for (int j = 0; j < T + 1; j++) { // Copy the last T + 1 pointers of node into newNode.
				newNode.childNodes[j] = node.childNodes[j + T - 1];
			}
			for (int j = T; j <= node.numberOfKeys; j++) {
				node.childNodes[j] = null;
			}
		} else {
			// Manage the linked list that is used e.g. for doing fast range queries.
			newNode.nextNode = node.nextNode;
			node.nextNode = newNode;
		}
		for (int j = T - 1; j < node.numberOfKeys; j++) {
			node.keys[j] = 0;
			node.records[j] = null;
		}
		node.numberOfKeys = T - 1;
		
		// Insert a (child) pointer to node newNode into the parentNode, moving other keys and pointers as necessary.
		for (int j = parentNode.numberOfKeys; j >= i + 1; j--) {
			parentNode.childNodes[j + 1] = parentNode.childNodes[j];
		}
		parentNode.childNodes[i + 1] = newNode;	
		for (int j = parentNode.numberOfKeys - 1; j >= i; j--) {
			parentNode.keys[j + 1] = parentNode.keys[j];
			parentNode.records[j + 1] = parentNode.records[j];
		}
		parentNode.keys[i] = newNode.keys[0];
		parentNode.records[i] = newNode.records[0];
		parentNode.numberOfKeys++;
	}
	
	
	void insertIntoNonFullNode(Node node, int key, Object object) {
		int i = node.numberOfKeys - 1;
		if (node.leafNode) {
			// Since node is not a full node insert the new element into its proper place within node.
			while (i >= 0 && key < node.keys[i]) {
				node.keys[i + 1] = node.keys[i];
				node.records[i + 1] = node.records[i];
				i--;
			}
			i++;
			node.keys[i] = key;
			node.records[i] = object;
			node.numberOfKeys++;
		} else {
			while (i >= 0 && key < node.keys[i]) {
				i--;
			}
			i++;
			if (node.childNodes[i].numberOfKeys == (2 * T - 1)) {
				splitChildNode(node, i, node.childNodes[i]);
				if (key > node.keys[i]) {
					i++;
				}
			}
			insertIntoNonFullNode(node.childNodes[i], key, object);
		}
	}	
	

	public Object search(Node node, int key) {		
		int i = 0;
		while (i < node.numberOfKeys && key > node.keys[i]) {
			i++;
		}
		if (i < node.numberOfKeys && key == node.keys[i]) {
			return node.records[i];
		}
		if (node.leafNode) {
			return null;
		} else {
			return search(node.childNodes[i], key);
		}	
	}
	
	public Object search(int key) {
		return search(mRootNode, key);
	}
	

	// Inorder walk over the tree.
	public String toString() {
		String string = "";
		try
		{
			File outputFile = new File(BTREEONEBEFORE);
			if(outputFile.exists()==false)
				outputFile.createNewFile();
			
			FileWriter myWriter = new FileWriter(outputFile, true);
		
	
			Node node = mRootNode;		
			while (!node.leafNode) {			
				node = node.childNodes[0];
			}		
			while (node != null) {
				for (int i = 0; i < node.numberOfKeys; i++) {
					myWriter.write(node.records[i]+"\n");
				}
				node = node.nextNode;
			}
			myWriter.close();
		
		}
		catch(IOException e)
		{
			System.out.println("In BTreeClass toString Method");
		}
		
		return "Shesh";
	}
	
	// Inorder walk over parts of the tree.
	public String toString(int fromKey, int toKey) {
		String string = "";
		
		Node node = getLeafNodeForKey(fromKey);
		while (node != null) {
			for (int j = 0; j < node.numberOfKeys; j++) {
				string += node.records[j] + ", ";
				if (node.keys[j] == toKey) {
					return string;
				}
			}
			node = node.nextNode;
		}
		return string;
	}
	
	Node getLeafNodeForKey(int key) {
		Node node = mRootNode;
		while (node != null) {
			int i = 0;
			while (i < node.numberOfKeys && key > node.keys[i]) {
				i++;
			}
			if (i < node.numberOfKeys && key == node.keys[i]) {
				node = node.childNodes[i + 1];
				while (!node.leafNode) {			
					node = node.childNodes[0];
				}
				return node;
			}
			if (node.leafNode) {
				return null;
			} else {
				node = node.childNodes[i];
			}
		}
		return null;
	}
	
	public void insertFileByFile(int i) {
		BPlusTree bPlusTree = new BPlusTree();
		
		
		FileReaderClass fileRead = new FileReaderClass(BTREEFILES+i);
		fileRead.openFile();
		try
		{
			String line = fileRead.buff_reader.readLine();
			while(line!=null)
			{
				bPlusTree.add(Math.abs(line.hashCode()),line);
				line = fileRead.buff_reader.readLine();
			}
			fileRead.closeFile();
			bPlusTree.toString();
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in BPlusTreeImplementationClass: enterElementsMethod");
		}
		
	}
}