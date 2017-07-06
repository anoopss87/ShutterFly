import java.util.*;

/**
 * Class to store key/value pairs related to any event
 * @author anoop
 *
 */
public class Event
{
	//key-value pairs of an event
	HashMap<String, String> eventData;
	String eventType;
	String customerId;
	
	/**
	 * Constructor
	 */
	Event()
	{
		eventData = new HashMap<String, String>();
		eventType = null;
		customerId = null;		
	}
	
	/**
	 * Update hashmap for a single event which consists of various key-value pairs
	 * @param entry
	 */
	void addEventData(String entry)
	{
		// remove double quotes and {} from the event string
		String event = entry.replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
		//System.out.println(event);
		
		String[] pairs = event.split(",");
		
		//add key value pairs into the hashmap
		for(String pair : pairs)
		{
			String[] keyValue = pair.split(":", 2);
			if (keyValue.length == 2)
			{
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				
				if (key.equals("type"))
					eventType = value;				
				
				eventData.put(key, value);
			}
			else
			{
				System.out.println(pair);
			}
		}
	}
	
	/**
	 * Returns the value of the given key in the event
	 * @param key
	 * @return
	 */
	String getValue(String key)
	{
		if(eventData.containsKey(key))
		{
			return eventData.get(key);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Get the customer id for this event
	 * @return
	 */
	String getCustomerId()
	{
		String custId = null;
		if (eventType.equals("CUSTOMER"))
		{
			custId = getValue("key");
		}
		else
		{
			custId = getValue("customer_id");
		}
		return custId;
	}
	
	/**
	 * Returns hashmap as a string for debugging
	 * @return
	 */
	String getEventAsString()
	{
		return eventData.toString();
	}
}