package com.POD.iiit;

import static com.POD.iiit.MyValues.*;

import java.util.*;

public class CreateMetadataAndFiles {
	
	//create Metadata file
	FileWriterClass gen_file;
	String testString;
	public static HashMap<String,String> attr_type;
	int noOfAttribs;

	
	public CreateMetadataAndFiles(int n) {
		// TODO Auto-generated constructor stub
		attr_type = new HashMap<String,String>();
		noOfAttribs=n;
	
	}
	
	public void createMetaDataFile()
	{
		gen_file = new FileWriterClass(METADATAFILE);
		gen_file.createFile();
		
		String type;
		for(int i=0;i<noOfAttribs-1;i++)
		{
			
			type=TYPE_LIST[0];
			gen_file.writeline(BASEATTRIB+i+","+type);
		}

		gen_file.writeline("Col3,int");
		
		gen_file.closeFile();		
		
	}
	
	public void createHashMap()
	{
		FileReaderClass read_obj = new FileReaderClass(METADATAFILE);
		read_obj.openFile();
		String line = read_obj.readFile();
		while(line!=null)
		{
			String[] array = line.split(" ");
			attr_type.put(array[0], array[1]);

			line = read_obj.readFile();
		}
		read_obj.closeFile();
		
		System.out.println("hashmap is "+attr_type.toString());
		
	}
	

	public String generateRandomString(){
        
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
	}
        
        
     private int getRandomNumber() {
            int randomInt = 0;
            Random randomGenerator = new Random();
            randomInt = randomGenerator.nextInt(CHAR_LIST.length());
            if (randomInt - 1 == -1) {
                return randomInt;
            } else {
                return randomInt - 1;
            }
     }
	
	
	public void createInputFile1()
	{
		gen_file = new FileWriterClass(INPUTFILE1);
		gen_file.createFile();
//		
		
		int recordLength = computeRecordLength();
		int fileSizeInMB = FILE1SIZE;
		
		long numberOfRecordsNeeded = computeNumberOfRecords(recordLength,fileSizeInMB);
		System.out.println("No of records needed are "+numberOfRecordsNeeded);
		
		StringBuilder myString = new StringBuilder();
		
		Random randomGenerator = new Random();
		int randomInt=0;	   
		
		Vector<String> vector100 = new Vector<String>();
		int counter_100=0;
		
		for(int k=0;k<numberOfRecordsNeeded;k++)
		{
			
			if(counter_100==100)
			{
				for(int p=0;p<50;p++,k++)
				{
					gen_file.writeline(vector100.get(p));
				}
				counter_100=0;
				vector100.clear();
			}
			
			
			for(int j=0;j<noOfAttribs-1;j++)
			{
				
				String string = generateRandomString();			
			    myString.append(string+" ");		     
			}
			randomInt = randomGenerator.nextInt(DIGITLENGTH);
		    myString.append(randomInt+"");
		    
		    gen_file.writeline(myString.toString());
		    vector100.insertElementAt(myString.toString(), counter_100%100);
		    counter_100++;
		    myString.delete(0, myString.length());
		  
		    
		}		
		
		
//        System.out.println("Line is "+myString.toString());				
		gen_file.closeFile();
	}
	
	public void createInputFile2()
	{
		gen_file = new FileWriterClass(INPUTFILE2);
		gen_file.createFile();
		

		int recordLength = computeRecordLength();
		long fileSizeInMB = FILE2SIZE;
		
		long numberOfRecordsNeeded = computeNumberOfRecords(recordLength,fileSizeInMB);
		System.out.println("No of records needed are "+numberOfRecordsNeeded);
		
		System.out.println("No of attribs are " + noOfAttribs);
		
		StringBuilder myString = new StringBuilder();
		
		Random randomGenerator = new Random();
		int randomInt=0;
		
		Vector<String> vector100 = new Vector<String>();
		int counter_100=0;

		
		for(int k=0;k<numberOfRecordsNeeded;k++)
		{
			
			if(counter_100==100)
			{
				for(int p=0;p<50;p++,k++)
				{
					gen_file.writeline(vector100.get(p));
				}
				counter_100=0;
				vector100.clear();
			}
			
			
			for(int j=0;j<noOfAttribs-1;j++)
			{
				
				String string = generateRandomString();			
			    myString.append(string+" ");		     
			}
			randomInt = randomGenerator.nextInt(DIGITLENGTH);
		    myString.append(randomInt+"");
		    
		    gen_file.writeline(myString.toString());
		    vector100.insertElementAt(myString.toString(), counter_100%100);
		    counter_100++;
		    myString.delete(0, myString.length());
		  
		    
		}		
		gen_file.closeFile();
	}
	
	/**find out the length of each record (from metadata.txt **/
	public static int computeRecordLength()
	{	
		int record_length=0;
		
		for(String key: attr_type.keySet())
		{
			String dataType = attr_type.get(key);
			if(dataType.contains("int")||dataType.contains("INT"))
			{
				record_length+=6;//specified 6 digits in given example (keep 8)
			}
			
			if(dataType.contains("date")||dataType.contains("DATE"))
			{
				record_length+=12;//10 but keep 2 extra
			}
			
			if(dataType.contains("char") || dataType.contains("CHAR"))
			{
				String charLength = dataType.substring(dataType.indexOf("(")+1,dataType.indexOf(")"));
				Integer charLength_in_Integer = Integer.parseInt(charLength);
				System.out.println("The size of char is " + charLength_in_Integer);				
				record_length+= charLength_in_Integer;
			}
			
		}
		
		return record_length;
	}
	
	/**find out the number of records needed for generating each file**/
	public long computeNumberOfRecords(int recordLength,long fileSizeInMB)
	{
		double result = (fileSizeInMB*1024*1024)/recordLength;
		result= Math.ceil(result);
		return (long)result;
	}
	
	
}
