import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Customer class is a containment of event objects
 * @author anoop
 *
 */
public class Customer
{
	//key is event type and value is the list of events
	HashMap<String, ArrayList<Event>> eventMap = new HashMap<String, ArrayList<Event>>();
	Date earliestSiteVisitTime = null;
	Date latestSiteVisitTime = null;
	
	/**
	 * Add Customer event to the hashmap with customer id as its key
	 * @param event
	 * @return
	 * @throws ParseException 
	 */
	String addEvent(Event eveObj) throws ParseException
	{
		String eventType = eveObj.eventType;
		String custId = eveObj.getCustomerId();
		
		if(eventType != null)
		{
			ArrayList<Event> temp = new ArrayList<Event>();
			
			// if the event type already exists then append to the arraylist
			if(eventMap.containsKey(eventType))
			{
				temp = eventMap.get(eventType);
			}			
			temp.add(eveObj);
			eventMap.put(eventType, temp);
						
			/** Update earliest site visit time and latest site visit time **/
			String eventTime = eveObj.getValue("event_time");							
			updateSiteVisitTime(eventTime);
		}
		return custId;
	}
	
	/**
	 * Update earliest and latest site visit time when new event arrives
	 * @param eventTime
	 * @throws ParseException
	 */
	void updateSiteVisitTime(String eventTime) throws ParseException
	{
		try
		{
			if(eventTime != null)
			{
				String eveTime = eventTime.replace("T", " ").replace("Z", "");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date curEventTime = simpleDateFormat.parse(eveTime);
				if (earliestSiteVisitTime == null && latestSiteVisitTime == null)
				{
					earliestSiteVisitTime = curEventTime;
					latestSiteVisitTime = earliestSiteVisitTime;
				}
				else
				{	
					if (curEventTime.before(earliestSiteVisitTime))
					{
						earliestSiteVisitTime = curEventTime;
					}
					
					if(curEventTime.after(latestSiteVisitTime))
					{
						latestSiteVisitTime = curEventTime;
					}
				}
			}
		}
		catch(ParseException e)
		{
			System.out.format("Date format error: %s\n", e.getMessage());
		}
	}
	
	/**
	 * Returns a list of events for the specific event type
	 * @param eventType
	 * @return
	 */
	ArrayList<Event> getEventList(String eventType)
	{
		if (eventMap.containsKey(eventType))
		{
			return eventMap.get(eventType);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Get the total number of site visits
	 * @return
	 */
	int getNumberofSiteVisits()
	{
		ArrayList<Event> siteVisits = getEventList("SITE_VISIT");
		if (siteVisits != null)
			return siteVisits.size();
		else
			return 0;
	}
	
	/** 
	 * Get the duration of site visits in days
	 * @return
	 */
	int getDurationOfSiteVisits()
	{
		long timeDiff = latestSiteVisitTime.getTime() - earliestSiteVisitTime.getTime();
		int dayDiff = (int)timeDiff / (1000 * 3600 * 24);
		return dayDiff;
	}
	/**
	 * Displays the event list for debugging
	 */
	void display()
	{
		for(String id : eventMap.keySet())
		{
			ArrayList<Event> temp = eventMap.get(id);
			for(Event e : temp)
			{
				System.out.println(e.getEventAsString());
			}
		}
	}
}
