package com.POD.iiit;

import java.io.IOException;
import java.util.*;
import static com.POD.iiit.MyValues.*;

public class HashFilesToOutput {

	public static HashMap<Integer,Vector<String>> smallHashMap;
	public static int M;
	public static int noOfAttributes;
	public static FileReaderClass[] readers;
	public static FileWriterClass outputFile;
	
	HashFilesToOutput(HashMap<Integer,Vector<String>> smallHashMapArg,int MArg,int noOfAttributesArg)
	{
		smallHashMap=smallHashMapArg;
		M=MArg;
		noOfAttributes = noOfAttributesArg;
	}
	

	public void openFiles()
	{
		readers = new FileReaderClass[M];
		for(int i =0;i<M;i++)
		{
			readers[i] = new FileReaderClass(HASHFILE+i);
			readers[i].openFile();
		}
		
		outputFile = new FileWriterClass(OUTPUTFILE);		
		outputFile.createFile();
		
	}
	

	
	public void callHashes()
	{
		for(int i=0;i<M;i++)
		{
			bringToMainMemory(i);
		}
	}
	
	
	public static void bringToMainMemory(int i)
	{
		HashMap<Integer,Vector<String>> file1HashMap = new HashMap<Integer,Vector<String>>();		
		Vector<Vector<String>> myHashVectors1 = new Vector<Vector<String>>(HASH2);
		 
		
//		System.out.println("Control is here");
		
		for(int j=0;j<HASH2;j++)
		{
			myHashVectors1.add(j, new Vector<String>());
			file1HashMap.put(j, myHashVectors1.get(j));
		}

		String line;
		String[] myArray;
		int key,hashIndex;
		
		for(int k=0;k<smallHashMap.get(i).size();k++)
		{
			line = smallHashMap.get(i).get(k);
					
			myArray = line.split(" ");
		
			key = myArray[noOfAttributes-1].hashCode();
			if(key<0)
				key=key*-1;
			
			hashIndex = key%HASH2;
			
			if(!file1HashMap.get(hashIndex).contains(line))
			{
				file1HashMap.get(hashIndex).add(line);
				outputFile.writeline(line);
			}
					
		}
		//File1 HashMap is ready
//		System.out.println("File1 HashMap is ready its size is "+file1HashMap.size());
		
		
		try {
//			System.out.println("File is "+HASHFILE+i);
			line = readers[i].buff_reader.readLine();		
//			int counter=0;
			while(line!=null)
			{
				myArray = line.split(" ");				
				key = myArray[noOfAttributes-1].hashCode();
				if(key<0)
					key=key*-1;
				
				hashIndex = key%HASH2;
//				System.out.println(line);
				
				if(!file1HashMap.get(hashIndex).contains(line))
				{
					file1HashMap.get(hashIndex).add(line);
					outputFile.writeline(line);
				}
				
				line =  readers[i].buff_reader.readLine();
//				System.out.println(counter++);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myHashVectors1.clear();
		file1HashMap.clear();
		
		
		
		
	}
	
	void closeFiles()
	{
		for(int i =0;i<M;i++)
		{
			readers[i].closeFile();
		}
		outputFile.closeFile();
		System.out.println("(Hash Index) Have you seen this file? --> "+OUTPUTFILE);
	}
	
	
	
}
