package io.laniakia.util;

import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigUtil 
{
	private static final Logger logger = LogManager.getLogger(ConfigUtil.class);
	
	public static APICall getConfigValue(String key) throws Exception
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(ConfigUtil.class.getResourceAsStream("/config/config.xml"), "Cp1252");
		logger.debug("Initialized XMLStreamReader");
		APICall query = null;
		while (parser.hasNext()) 
		{
			int currentEvent = parser.next();
			if(currentEvent ==  XMLStreamReader.START_ELEMENT)
			{
				if(parser.getLocalName().equalsIgnoreCase(key))
				{
					query = new APICall(key);
				}
				else if(parser.getLocalName().equalsIgnoreCase("baseUrl"))
				{
					query.setBaseURL(parser.getElementText());
				}
				else if(parser.getLocalName().equalsIgnoreCase("parameter"))
				{
					getParameters(parser, query);
				}
			}
		}
		return query;
	}
	
	public static void getParameters(XMLStreamReader reader, APICall query) throws Exception
	{
		while (reader.hasNext()) 
		{
			int currentEvent = reader.next();
			if(currentEvent ==  XMLStreamReader.START_ELEMENT)
			{
				if(reader.getLocalName().equalsIgnoreCase("query"))
				{
					query.addParameter(reader.getElementText());
					return;
				}
			}
		}
	}
	
	public static String buildAPICall(APICall query, Map<String, String> parameters) throws Exception
	{
		if(query != null && StringUtils.isNotBlank(query.getBaseURL()))
		{
			logger.debug("APICall Base URL: " + query.getBaseURL());
			URIBuilder builder = new URIBuilder(query.getBaseURL());
			if(parameters != null && !parameters.isEmpty() && query.HasParameters())
			{
				for (Map.Entry<String, String> entry : parameters.entrySet())
				{
				    String key = entry.getKey();
				    if(StringUtils.isNotBlank(key) && query.getParameters().contains(key))
				    {
						String value = entry.getValue();
						builder.addParameter(key, value);
						logger.debug("Added Parameter: key=" + key + ", value=" + value);
				    }
				}
			}
			
			return builder.build().toString();
		}
		return null;
	}
	
	public static String getAPICall(String key, Map<String, String> parameters) throws Exception
	{
		APICall query = getConfigValue(key);
		if(query != null)
		{
			return buildAPICall(query, parameters);
		}
		return null;
	}
}
