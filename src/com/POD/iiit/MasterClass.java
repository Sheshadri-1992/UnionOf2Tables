package com.POD.iiit;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;
import static com.POD.iiit.MyValues.*;

public class MasterClass {
	
	public static long startTime;
	public static long endTime;
	public static long MAX_MEMORY;
	public static long BLOCK_SIZE ;
	public static int noOfAttributes;
	public static int noOfBtreeFiles;
	public static HashMap<String,String> attr_type;
	public static HashMap<String,Integer> attr_pos;
	public static HashMap<String,Integer> gg_map;
	public static HashMap<Integer,Vector<String>> smallHashMap;
	public static int M;
	public static int typeOfIndex;
	public static String table1;
	public static String table2;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length!=5)
		{
			System.out.println("Usage: java com.POD.iiit.MasterClass table1 table2 n m typeOfIndex");
			System.exit(0);
		}
			

		/**Declare your variables here**/
		startTime = System.currentTimeMillis();
		
		/**attribute type hash map **/		
		attr_type = new HashMap<String,String>();
		
		/**attribute position hash map **/
		attr_pos = new HashMap<String,Integer>();
		
		/**only hashmap**/
		gg_map = new HashMap<String,Integer>();
		
		/**record length**/
		long rec_length=1;
		
		/**number of records**/
		long noRecPerBlock=1;
		
		/**total number of records in a given input file **/
//		long total_records=1;
		
		/**Sorted lists**/
		double no_of_sorted_lists=1;
		
		/**Variable declarations end here**/
		
		System.out.println("POD Code starts here");
			
		/**Extract all parameters from the command line **/
		table1=args[0];
		table2=args[1];
		M=Integer.parseInt(args[3]); //will be given as input, this is the number of blocks
		noOfAttributes=Integer.parseInt(args[2]);//number of arguments
		typeOfIndex=Integer.parseInt(args[4]);//hashIndex or B+ tree index
		
		
		MAX_MEMORY= M*1024*1024;
		BLOCK_SIZE =1024*1024; //consider block size as 1MB(assumption)
		
		rec_length=computeRecordLength();//should find out how many records will fit into 1 block
		
		System.out.println("No of blocks are (M): "+M);
		System.out.println("Block Size is assumed to be 1MB");
		
		noRecPerBlock = noOfRecordsPerBlock(rec_length);
		
		System.out.println("No of records per block is "+noRecPerBlock);
//		createMetaAndInputFiles(noOfAttributes);
//		System.exit(0);	
		String string1 = "1938033931,ULhPlHg,24009587,1592664356,pBqwPdEMxHZzH";
		String string2 =	"1873368067,yJupSLjOasSzHvWQXd,606253326,704380509,lpjQKEbdZBNjtNWcR";
		String string3= "41810661,vvpFraN,630447478,1286277121,WGrxzdVKJrNfhLQuFQLy";
		String string4 = "1873368067,yJupSLjOasSzHvWQXd,606253326,704380509,lpjQKEbdZBNjtNWcR";
		
//		
//		System.out.println(string1.hashCode() + " = "+string2.hashCode() + " = "+string3.hashCode()+" = "+string4.hashCode());
//		System.exit(0);	
		
	
		if(typeOfIndex==0)
		{
			System.out.println("Union of Two Relations (Hash Index)");
			
			boolean union= unionPossible(no_of_sorted_lists, rec_length);
			
			if(union)
			{
//				total_records = noOfRecordsInGivenInput(table1);//this is the input file
				
				startTime = System.currentTimeMillis();
				CreateHashFiles hashFilesObj = new CreateHashFiles((int)noRecPerBlock, M, noOfAttributes);
				hashFilesObj.createFiles();
				hashFilesObj.writeToHashFiles(table2);
				hashFilesObj.closeFiles();
				
				//Create HashMap for Small Table
				createHashMapForSmallTable();
				
//				System.out.println("Small HashMap size is "+smallHashMap.size());
//				System.out.println("Bucket 0 size of SmallHashMap is "+smallHashMap.get(49).size());			
				
				HashFilesToOutput hashFileObj = new HashFilesToOutput(smallHashMap, M, noOfAttributes);
				hashFileObj.openFiles();
				hashFileObj.callHashes();
				hashFileObj.closeFiles();
			}
			
		}
		else
		{
			System.out.println("Union of Two Relations (B+ Tree)");
			BPlusTreeImplementation btreeObj = new BPlusTreeImplementation();
			btreeObj.enterElements(table1,table2);
			btreeObj.printAllNodes();
			btreeObj.writeToOutputFile();			
			noOfBtreeFiles =  btreeObj.noOfBPlusTreeFiles();
			btreeObj = null;
			
		
			for(int i=0;i<noOfBtreeFiles;i++)
			{
				BPlusTree btree = new BPlusTree();				
				btree.insertFileByFile(i);
				btree=null;
			}
			FinalBTreeOutput finalObj = new FinalBTreeOutput();
			finalObj.writeOutput();
			finalObj=null;					
			
		}
		
		
		
		endTime = System.currentTimeMillis();
		double totalTime = ((endTime-startTime)/(1000*1.0));
		System.out.println("Total Time Taken(in seconds) : " + totalTime);
		
	}
	
	public static void createMetaAndInputFiles(int n)
	{
		CreateMetadataAndFiles miFiles = new CreateMetadataAndFiles(4);
		File myFile = new File(METADATAFILE);
		boolean fileFlag;
		fileFlag = myFile.exists();
		if(fileFlag==false)
			miFiles.createMetaDataFile();
		
		miFiles.createHashMap();
		
		myFile = new File(INPUTFILE1);
		fileFlag = myFile.exists();
		if(fileFlag==false)
			miFiles.createInputFile1();
		else
			System.out.println("File1 input1_5MB.txt Already Present");
		
		myFile = new File(INPUTFILE2);
		fileFlag = myFile.exists();
		if(fileFlag==false)
			miFiles.createInputFile2();
		else
			System.out.println("File2 input1_1GB.txt Already Present");		

		
		
	}

	

	
	public static boolean unionPossible(double noOfSortedSublists,long recordLength)
	{
		double result = noOfSortedSublists*recordLength + recordLength; //one for the output buffer
		
		if(result>= MAX_MEMORY)
			return false;
		else
			return true;
	}

	public static void getMaxMemory()
	{
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		MAX_MEMORY = memoryBean.getHeapMemoryUsage().getMax();
//		5 MB is subtracted so that all local variables, references can be supported
		MAX_MEMORY = (MAX_MEMORY/2) - (5242880);
	}
	
	/**Method used to get the length of records **/
	public static long computeRecordLength()
	{	
		long record_length=0;
		
		FileReaderClass myFileReader = new FileReaderClass(table1);
		myFileReader.openFile();
		try {
			String myLine = myFileReader.buff_reader.readLine();
			record_length=myLine.length();
			System.out.println("length is "+record_length);
			record_length = record_length*2; //record_length = 24 + record_length*2;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return record_length;
	}
	
	
	/**Number of records that can be sorted in main memory **/
	public static long noOfRecordsPerBlock(long rec_length)	
	{
		return 1024*1024/rec_length;
	}
	
	/**Create a HashMap for the small table **/
	public static void createHashMapForSmallTable()
	{
		smallHashMap = new HashMap<Integer,Vector<String>>();
		
		Vector<Vector<String>> hashVectors = new Vector<Vector<String>>(M);
		
		for(int i=0;i<M;i++)
		{
			hashVectors.add(i,new Vector<String>());
			
		}
		
		FileReaderClass myReader = new FileReaderClass(table1);
		myReader.openFile();
		try
		{
			String line = myReader.buff_reader.readLine();			
			String[] myArray;			
			int key,hashIndex;
			
			while(line!=null)
			{			
				myArray = line.split(" ");
				key = myArray[noOfAttributes-1].hashCode();
				if(key<0)
					key=key*-1;
				hashIndex=key%M;
				hashVectors.get(hashIndex).add(line);
				
				line =  myReader.buff_reader.readLine();
			}
			
			for(int i=0;i<M;i++)
			{
				smallHashMap.put(i, hashVectors.get(i));
				
			}	
			
		}		
		catch(IOException e)
		{
			System.out.println("IO excpetion in method noOfRecords class: main ");
		}
		
		myReader.closeFile();		
	}
	
	/**Number of records present in the given input file **/
	public static long noOfRecordsInGivenInput(String filename)
	{
		long counter=0;
		
		FileReaderClass myReader = new FileReaderClass(filename);
		myReader.openFile();
		try
		{
			String line = myReader.buff_reader.readLine();
			
			while(line!=null)
			{
				counter++;
				line =  myReader.buff_reader.readLine();
			}
		}
		catch(IOException e)
		{
			System.out.println("IO excpetio in method noOfRecors class: main ");
		}
		
		myReader.closeFile();
		
		return counter;
						
	}
	
	
}


