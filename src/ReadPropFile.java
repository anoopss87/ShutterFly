import java.io.*;
import java.util.*;

/**
* ReadPropFile reads configuration property file.  
* @author  anoop
*/
public class ReadPropFile
{
	protected static Properties prop = null;
			
	private static ReadPropFile instance = null;	
	
	protected ReadPropFile()	
	{		
		prop = new Properties();				
	}
	
	/**
	   * This method returns an instance of ReadPropFile 	
	   * @exception Exception If the configuration properties file read error
	   * @return Singleton object of ReadPropFile 	  
	 */
	public static ReadPropFile getInstance() throws Exception
	{
		try
		{
			if(instance == null)
			{
				instance = new ReadPropFile();
				
				/* FileInputStream throws an exception to avoid that first check if the file exists */
				File f = new File("conf.properties");
				if(f.exists())
				{
					FileInputStream fileStream = new FileInputStream("conf.properties");
					if(fileStream != null)
						prop.load(fileStream);
				}
				else
				{
					System.out.println("File not exists");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Unable to read configuration properties file : " + e);			
		}
		return instance;		
	}
	
	/**
	 * Gets customer average life span of the customer
	 * @return
	 */
	public String getCustomerLifeSpan()
	{
		return  prop.getProperty("customer_life_span");		
	}
	
	public String getEventsFileLocation()
	{
		return prop.getProperty("events_file_location");
	}
}