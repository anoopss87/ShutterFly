import java.io.*;
import java.util.*;

/**
 * Computes Simple LTV from the customer event data
 * @author anoop
 *
 */
public class SimpleLTV
{
	private static int customer_lifespan;
	
	/**
	 * Constructor
	 */
	SimpleLTV()
	{
		customer_lifespan = getCustLifeSpan();
	}
	
	/**
	 * Reads the property file to get the customer average life span 
	 * @return
	 */
	public static int getCustLifeSpan()
	{
		try
		{
			ReadPropFile inst;
			if((inst = ReadPropFile.getInstance()) != null)
			{
				String val =  inst.getCustomerLifeSpan();
				if(val != null)
					return Integer.parseInt(val);
			}			
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown  :" + e);
		}		
		return 10;
	}
	
	/**
	 * Returns top n customers with highest LTV 
	 * @param n
	 * @param data
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	void topNLTVCustomers(int N, HashMap<String, Customer> data) throws FileNotFoundException, UnsupportedEncodingException
	{
		try
		{
			HashMap<String, Double> customerLTVMap = new HashMap<String, Double>();
			for(String custId : data.keySet())
			{			
				Customer custObj = data.get(custId);
				double customerLTV = computeSimpleLTV(custObj);
				customerLTVMap.put(custId, customerLTV);
				//System.out.println(custId + " " + customerLTV);
			}
			Map<String, Double> sortedCustLTVMap = sortByValues(customerLTVMap, N);
			
			PrintWriter outFile = new PrintWriter("output/output.txt", "UTF-8");
			outFile.format("The top %d customer with highest LTV are:\n", N);
			for(String custId : sortedCustLTVMap.keySet())
			{
				outFile.format("Customer Id : %s	LTV : %f\n", custId, sortedCustLTVMap.get(custId));
			}
			outFile.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Computes the simple LTV for the given customer
	 * @param custObj
	 * @return
	 */
	private double computeSimpleLTV(Customer custObj)
	{
		ArrayList<Event> orderEvents = custObj.getEventList("ORDER");
		double customerLTV = 0.0;
		double totalOrderAmount = 0.0;
		for(Event event : orderEvents)
		{
			String orderAmount = event.getValue("total_amount").replace("USD", "").trim();
			totalOrderAmount += Double.parseDouble(orderAmount);
		}
		
		int duration = custObj.getDurationOfSiteVisits();
		int numberOfSiteVisits = custObj.getNumberofSiteVisits();
		
		if(duration == 0)
			duration = 1;
		
		if(numberOfSiteVisits == 0)
			numberOfSiteVisits = 1;
		
		double expenditurePerVisit = totalOrderAmount / numberOfSiteVisits;
		double siteVisitsPerWeek = 1;
		
		if(duration <= 7)
			siteVisitsPerWeek = numberOfSiteVisits;
		else
			siteVisitsPerWeek = (numberOfSiteVisits / (double)duration) * 7.0;
		
		double avgCustomerValuePerWeek = expenditurePerVisit * siteVisitsPerWeek;
		customerLTV = 52 * avgCustomerValuePerWeek * customer_lifespan;
		return customerLTV;
	}
	
	/**
	 * Sort the hash map by values
	 * @param map
	 * @param N
	 * @return
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map, int N)
	{
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>()
        {
            //decreasing order
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
			{
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        int counter = 0;
        for (Map.Entry<K, V> entry : entries)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
            counter++;
            if (counter >= N)
            	break;
        }
        return sortedMap;
    }
}
