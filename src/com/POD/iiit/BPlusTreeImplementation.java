package com.POD.iiit;
import static com.POD.iiit.MyValues.*;

import java.io.IOException;

public class BPlusTreeImplementation {

	public static final int DEGREE = BTREEDEGREE*2;
	public static TreeNode mainRootNode;
	public static FileWriterClass[] bTreeFiles;
	public static int noOfLeafNodes;
	public static String InputFile1;
	public static String InputFile2;
	
	public BPlusTreeImplementation() {
		// TODO Auto-generated constructor stub
		mainRootNode = new TreeNode();
		mainRootNode.leafNode = true;
		initializeNodes(mainRootNode);		
	}
	
	public static void initializeNodes(TreeNode node)
	{
		node.keys = new int[2*BTREEDEGREE-1];
		node.childNodes = new TreeNode[2*BTREEDEGREE];
	}
	
	
	public void insertIntoBPlusTree(int key,String line)
	{
		TreeNode currentRootNode = mainRootNode;
//		if(search(key))
//			return;
		

		
		if(currentRootNode.numberOfKeys==(DEGREE-1))//addition of a node will make the keys  = DEGREE
		{
			TreeNode newNode = new TreeNode();
			newNode.leafNode = false;
			initializeNodes(newNode);
			mainRootNode = newNode;
			newNode.childNodes[0] = currentRootNode;
			//split and insert
			splitNode(currentRootNode,mainRootNode,0);
			addIntoNonFullNode(newNode,key);
//			outputFile.writeline(line);
		}
		else
		{
			addIntoNonFullNode(currentRootNode,key);
//			outputFile.writeline(line);
		}
	}
	
	@SuppressWarnings("unused")
	public static void splitNode(TreeNode currentRootNode,TreeNode parentNode,int i)
	{
		//need to create a new node here 
		TreeNode newNode = new TreeNode();
		/** If it is leaf node we are splitting, the new node will also be leaf node
		 * If it is internal node that we are splitting the new node will also be internal node
		 **/
		newNode.leafNode = currentRootNode.leafNode;
		initializeNodes(newNode);
	
		
//		System.out.println("BTree degree is "+ DEGREE + " No of elements to be copied are "+BTREEDEGREE);
		
		for(int j=0;j<BTREEDEGREE;j++)
		{
			newNode.keys[j] = currentRootNode.keys[j+BTREEDEGREE-1];
		}
		/**update the number of keys in node which got split and one which was before **/
		newNode.numberOfKeys=BTREEDEGREE;
		currentRootNode.numberOfKeys-= BTREEDEGREE;
		
		/**if the node is not a leaf node then the pointers to child nodes need to be updated **/
		if(newNode.leafNode==false)
		{
			/**Copy pointers **/
			for(int j=0;j<BTREEDEGREE+1;j++)
			{
				newNode.childNodes[j] = currentRootNode.childNodes[j+BTREEDEGREE-1];
			}
			for(int j=BTREEDEGREE;j<=currentRootNode.numberOfKeys;j++)
			{
				newNode.childNodes[j] =null;
			}
			
		}
		else
		{
			/**If it is a leaf node, key pointers are copied just make the node connections**/
			newNode.nextNode = currentRootNode.nextNode;
			currentRootNode.nextNode = newNode;
		}
		for(int j=BTREEDEGREE-1;j<currentRootNode.numberOfKeys;j++)
		{
			currentRootNode.keys[j]=0;
			currentRootNode.childNodes[j]=null;
		}
		
		for(int j=parentNode.numberOfKeys;j>=i+1;j--)
		{
			parentNode.childNodes[j+1]=parentNode.childNodes[j];
		}
		parentNode.childNodes[i+1]=newNode;
		
		for(int j=parentNode.numberOfKeys-1;j>=i;j--)
		{
			parentNode.keys[j+1]=parentNode.keys[j];			
		}
		
		parentNode.keys[i]=newNode.keys[0];
		parentNode.numberOfKeys++;
//		System.out.println("Split happening correctly");
	}
	
	public static void addIntoNonFullNode(TreeNode currentNode,int key)
	{
		int i=currentNode.numberOfKeys-1;
		if(currentNode.leafNode==true)
		{
			
			while(i>=0 && key<currentNode.keys[i])
			{
				currentNode.keys[i+1]=currentNode.keys[i];
				i--;
			}
			i++;
			currentNode.keys[i] = key;
			currentNode.numberOfKeys++;
		}
		else
		{
			while(i>=0 && key<currentNode.keys[i])
			{
				i--;
			}
			i++;
			if(currentNode.childNodes[i].numberOfKeys == (DEGREE-1))
			{
				splitNode(currentNode.childNodes[i] , currentNode, i);
				if(key > currentNode.keys[i])
					i++;
			}
			addIntoNonFullNode(currentNode.childNodes[i], key);
		}
	}
	
	public void printAllNodes()
	{
		TreeNode myNode = mainRootNode;
		while(myNode.leafNode==false)
		{
			myNode = myNode.childNodes[0];
		}
		
		int noOfLeafNodes=0;
		int counter=0;
		int totalElements=0;
		while(myNode!=null)
		{
			for(int i=0;i<myNode.numberOfKeys;i++)
			{
//				System.out.println(myNode.keys[i]);
				totalElements++;
			}
			myNode.fileNO = counter;
//			System.out.println(myNode.fileNO);
			counter++;
			myNode = myNode.nextNode;
			noOfLeafNodes++;
		}
		System.out.println("The no of leaf nodes are "+noOfLeafNodes);
		System.out.println("The total number of elements are "+totalElements);
		
		bTreeFiles = new FileWriterClass[noOfLeafNodes];
		BPlusTreeImplementation.noOfLeafNodes = noOfLeafNodes;
		
		for(int i=0;i<noOfLeafNodes;i++)
		{
			bTreeFiles[i] = new FileWriterClass(BTREEFILES+i);
			bTreeFiles[i].createFile();
		}
		
		
	}
	
	public static int searchIterative(TreeNode node, int key) {
		while (node != null) {
			int i = 0;
			while (i < node.numberOfKeys && key > node.keys[i]) {
				i++;
			}
			if (i < node.numberOfKeys && key == node.keys[i]) {
				return node.fileNO;
			}
			if (node.leafNode) {
				return -1;
			} else {
				node = node.childNodes[i];
			}
		}
		return -1;
	}
	
	public static int search(int key) {
		return searchIterative(mainRootNode, key);
	}

	
	public void enterElements(String File1,String File2)
	{
//		int numbers[] = new int[] { 1,2,3,4,5,6,7,8,9,10};
		InputFile1 = File1;
		InputFile2 = File2;
		
		FileReaderClass fileRead = new FileReaderClass(InputFile1);
		fileRead.openFile();
		try
		{
			String line = fileRead.buff_reader.readLine();
			while(line!=null)
			{
				insertIntoBPlusTree(Math.abs(line.hashCode()),line);
				line = fileRead.buff_reader.readLine();
			}
			fileRead.closeFile();
			
			fileRead = new FileReaderClass(InputFile2);
			fileRead.openFile();
			line = fileRead.buff_reader.readLine();
			while(line!=null)
			{
				insertIntoBPlusTree(Math.abs(line.hashCode()),line);
				line = fileRead.buff_reader.readLine();
			}
			fileRead.closeFile();
		
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in BPlusTreeImplementationClass: enterElementsMethod");
		}
//		for(int i=0;i<numbers.length;i++)
//		{
////			System.out.print(numbers[i]+" ");
//			insertIntoBPlusTree(numbers[i]);
//		}		
//		printAllNodes();		
	}
	
	public int noOfBPlusTreeFiles()
	{
		return noOfLeafNodes;
	}
	
	public void writeToOutputFile()
	{
		FileReaderClass fileRead = new FileReaderClass(InputFile1);
		fileRead.openFile();
		try
		{
			int result;
			String line = fileRead.buff_reader.readLine();
			while(line!=null)
			{
				
				result = search(Math.abs(line.hashCode()));
//				System.out.println("File1 is "+result );
				bTreeFiles[result].writeline(line);
				line = fileRead.buff_reader.readLine();
				
			}
			fileRead.closeFile();
			
			fileRead = new FileReaderClass(InputFile2);
			fileRead.openFile();
			line = fileRead.buff_reader.readLine();
			while(line!=null)
			{
				result = search(Math.abs(line.hashCode()));
				bTreeFiles[result].writeline(line);
				line = fileRead.buff_reader.readLine();
			}
			fileRead.closeFile();
			
			
			for(int i=0;i<noOfLeafNodes;i++)
				bTreeFiles[i].closeFile();
			
		}
		catch(IOException e)
		{
			System.out.println("Exception caught in BPlusTreeImplementationClass: writeToOutputFile");
		}
		
	}
	
	
	
}

