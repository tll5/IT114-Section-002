import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FIleSystemSample 
{
	public static void main(String[] args)
	{
		String fileName = "test.txt";
		FIleSystemSample fss = new FIleSystemSample();
		fss.createFileAndGetDetails(fileName);
		fss.writeToFile(fileName, "Hello world! We're writing to files");
		int myNumber = -1;
		try
		{
			myNumber = Integer.parseInt("10");
			String[] data = new String[2];
			data[1].trim();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(myNumber);
		if(myNumber >= 1)
		{
			
		}
		
		String jsonfile = "sample.json";
		fss.createFileAndGetDetails(jsonfile);
		fss.writeToFile(jsonfile, "{}");
		try
		{
			JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(jsonfile));
			jo.put("name", "John");
			jo.put("name", "James");
			jo.put("age", 55);
			
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("address", "123 Fake Street");
			map.put("city", "Nowhere");
			map.put("state", "Bliss");
			map.put("zip", "01010");
			
			jo.put("fulladdress", map);
			fss.writeToFile(jsonfile, jo.toJSONString());
			fss.readFromFile(jsonfile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void Log(Object obj)
	{
		System.out.println(obj);
	}
	
	public void createFileAndGetDetails(String fileName)
	{
		try
		{
			File fileReference = new File(fileName);
			if(fileReference.createNewFile())
			{
				System.out.println("Didn't exist, created new");
			}
			else
			{
				System.out.println("File already exists");
			}
			//pause here!!
			
			System.out.println(fileName + " is located at " + fileReference.getAbsolutePath());
			if(fileReference.canRead())
			{
				System.out.println(fileName + " is readable");
			}
			else
			{
				System.out.println(fileName + " is not readable");
			}
			//next to see if its executable/able to be written in
			if(fileReference.canWrite())
			{
				System.out.println(fileName + " is writable");
			}
			else
			{
				System.out.println(fileName + " is not writable");
			}
			//execute - write into the file 
			if(fileReference.canExecute())
			{
				System.out.println(fileName + " is executable");
			}
			else
			{
				System.out.println(fileName + " is not executable");
			}
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	public void writeToFile(String fileName, String msg) 
	{
		//Hint: use BufferedWriter for less IO operations(better overall performance)
		try(FileWriter fw = new FileWriter(fileName))
		{
			fw.write(msg);
			System.out.println("Wrote " + msg + " to " + fileName);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void readFromFile(String fileName) 
	{
		File file = new File(fileName);
		try(Scanner reader = new Scanner(file))
		{
		
			String fullText = "";
			while(reader.hasNextLine())
			{
				String nl = reader.nextLine();
				System.out.println("Next Line: " + nl);
				fullText += nl;
				//Scanner.nextLine() returns the line but excludes the line separator
				if(reader.hasNextLine())
				{
					fullText += System.lineSeparator();
				}
			}
			System.out.println("Contents of " + fileName + ": ");
			System.out.println(fullText);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	public void appendToFile(String fileName, String msg) 
	{
		try(FileWriter fw = new FileWriter(fileName, true);)
		{
			fw.write(System.lineSeparator());
			fw.write(msg);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
