import java.text.ParseException;
import java.util.*;
/**
 * Event Data Ingestion to an in memeory data structure
 * @author anoop
 *
 */
public class EventDataIngestion
{
	//key is customer id and value is Customer object
	HashMap<String, Customer> data = new HashMap<String, Customer>();
	
	/**
	 * Ingest event data into the in-memory data structure
	 * @param event
	 * @throws ParseException 
	 */
	void Ingest(String event) throws ParseException
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
	
	void display()
	{
		SimpleLTV sLTV = new SimpleLTV();
		sLTV.topNLTVCustomers(10, data);
	}
}
