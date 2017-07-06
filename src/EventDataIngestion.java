import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 * Event Data Ingestion to an in memory data structure
 * @author anoop
 *
 */
public class EventDataIngestion
{
	//key is customer id and value is Customer object
	private HashMap<String, Customer> data = new HashMap<String, Customer>();
	
	/**
	 * Ingest event data into the in-memory data structure
	 * @param event
	 * @throws ParseException 
	 */
	void Ingest(String event) throws ParseException
	{
		try
		{
			Event eveObj = new Event();
			eveObj.addEventData(event);
			String custId = eveObj.getCustomerId();
			Customer customer = null;
			
			if(data.containsKey(custId))
				customer = data.get(custId);
			else
				customer = new Customer();
			
			String key = customer.addEvent(eveObj);
			data.put(key, customer);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Ingets the event data by reading the events file
	 * @return
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException
	 */
	HashMap<String, Customer> IngestDataFromFile() throws ParseException, FileNotFoundException, IOException, org.json.simple.parser.ParseException
	{
		try
		{
			JSONParser parser = new JSONParser();
			String eventFileLocation = getEventFileLocation();
			Object obj = parser.parse(new FileReader(eventFileLocation));
	
	        JSONArray jsonObject = (JSONArray) obj;
	        
	        @SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonObject.iterator();
	        while (iterator.hasNext())
	        {
	        	String event = iterator.next().toString();
	        	Ingest(event);
	        }
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
        return data;
	}
	
	/**
	 * Returns customer object for a given customer id.
	 * @param custId
	 * @return
	 */
	Customer getCustomerObj(String custId)
	{
		if (data.containsKey(custId))
		{
			return data.get(custId);
		}
		else
		{
			return null;
		}
	}
	
	public static String getEventFileLocation()
	{
		String val = "";
		try
		{
			ReadPropFile inst;
			if((inst = ReadPropFile.getInstance()) != null)
			{
				val =  inst.getEventsFileLocation();
				if(val == null)
					val ="sample_input/events.txt";
			}			
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown  :" + e);
		}		
		return val;
	}
}
