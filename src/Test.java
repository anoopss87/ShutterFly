import java.io.*;
import java.text.ParseException;
import java.util.HashMap;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, ParseException, IOException, org.json.simple.parser.ParseException
	{
		EventDataIngestion dataIngester = new EventDataIngestion();
		HashMap<String, Customer> eventData = dataIngester.IngestDataFromFile();
		
		SimpleLTV sLTV = new SimpleLTV();
		sLTV.topNLTVCustomers(3, eventData);
	}
}
