package com.POD.iiit;

import java.io.IOException;
import static com.POD.iiit.MyValues.*;

public class FinalBTreeOutput {

	private FileReaderClass finalRead;
	private FileWriterClass fileWriter;
	
	FinalBTreeOutput()
	{
	}
	
	public void writeOutput()
	{
		finalRead = new FileReaderClass(BTREEONEBEFORE);
		finalRead.openFile();
		
		try
		{
			fileWriter = new FileWriterClass(BTREEFINALOUTPUT);
			fileWriter.createFile();
			
			String prev = finalRead.buff_reader.readLine();
			String next = finalRead.buff_reader.readLine();;
//			if(next==null)
			fileWriter.writeline(prev);
			
			while(next!=null)
			{
				if(!prev.equals(next))
				{
					fileWriter.writeline(next);
				}
				prev = next;
				next = finalRead.buff_reader.readLine(); 
			}
			
			
		}
		catch(IOException e)
		{
			System.out.println("Excpeiton caught in FinalBTreeOutput: writeOutput MEthod");
		}
		finally
		{
			fileWriter.closeFile();
			finalRead.closeFile();
		}
		
		System.out.println("(B+ Tree)Have you seen this file? --> "+BTREEFINALOUTPUT );

		
		
	}
	
}
