package com.POD.iiit;

import static com.POD.iiit.MyValues.*;
import java.io.*;

public class CreateHashFiles {

	public int noRecordsPerBlock;
	public int noOfBlocks;
	public int noOfAttributes;
	FileWriterClass[] hashFiles;
	
	CreateHashFiles(int noRecordsPerBlockArg,int noOfBlocksArg,int noOfAttributesArg)
	{
		noRecordsPerBlock=noRecordsPerBlockArg;
		noOfBlocks=noOfBlocksArg;
		noOfAttributes=noOfAttributesArg;
	}
	
	void createFiles()
	{
		hashFiles= new FileWriterClass[noOfBlocks];
		
		for(int i=0;i<noOfBlocks;i++)
		{
			hashFiles[i]=new FileWriterClass(HASHFILE+i);
			hashFiles[i].createFile();
		}
			
	}
	
	public void writeToHashFiles(String table2)
	{
		
		int hashIndex=0;
		int key =0;
		FileReaderClass myReader = new FileReaderClass(table2);
		myReader.openFile();
		try
		{
			String line = myReader.buff_reader.readLine();
			
			String[] myArray;
			
			while(line!=null)
			{
				
				
				myArray = line.split(" ");
				key = myArray[noOfAttributes-1].hashCode();
				if(key<0)
					key=key*-1;
				
				hashIndex=key%noOfBlocks;
				hashFiles[hashIndex].writeline(line);
				
				line =  myReader.buff_reader.readLine();
			}
		}
		catch(IOException e)
		{
			System.out.println("IO excpetion in method noOfRecors class: main ");
		}
		
		myReader.closeFile();
	}
	
	public void closeFiles()
	{
		for(int i=0;i<noOfBlocks;i++)
		{		
			hashFiles[i].closeFile();
		}
	}
	
}
