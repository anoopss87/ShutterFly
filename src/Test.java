import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IOException, java.text.ParseException
	{
		EventDataIngestion dataIngest = new EventDataIngestion();
		
		JSONParser parser = new JSONParser();
		
		try
		{
            Object obj = parser.parse(new FileReader("sample_input/events.txt"));

            JSONArray jsonObject = (JSONArray) obj;
            
            @SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonObject.iterator();
            while (iterator.hasNext())
            {
            	String event = iterator.next().toString();
            	dataIngest.Ingest(event);
            	//System.out.println(event);
            }
            dataIngest.display();
            /*
            System.out.println("------------------------------------------");
            for(String id: dataIngest.data.keySet())
            {
            	Customer c = dataIngest.data.get(id);
            	c.display();
            }
            */
		}
		catch (ParseException e)
		{
			
		}
	}
}
