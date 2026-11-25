package io.laniakia.util;

import java.util.Map;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigUtil 
{
	private static final Logger logger = LogManager.getLogger(ConfigUtil.class);
	
	public static APICall getConfigValue(String key) throws Exception
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
		try (InputStream stream = ConfigUtil.class.getResourceAsStream("/config/config.xml"))
		{
			if (stream == null)
			{
				throw new IllegalStateException("Missing /config/config.xml from the classpath");
			}
			XMLStreamReader parser = factory.createXMLStreamReader(stream, "UTF-8");
			try
			{
				APICall query = null;
				while (parser.hasNext())
				{
					int currentEvent = parser.next();
					if (currentEvent != XMLStreamReader.START_ELEMENT)
					{
						continue;
					}
					if (parser.getLocalName().equalsIgnoreCase(key))
					{
						query = new APICall(key);
					}
					else if (query != null && parser.getLocalName().equalsIgnoreCase("baseUrl"))
					{
						query.setBaseURL(parser.getElementText());
					}
					else if (query != null && parser.getLocalName().equalsIgnoreCase("parameter"))
					{
						getParameters(parser, query);
					}
				}
				return query;
			}
			finally
			{
				parser.close();
			}
		}
	}
	
	public static void getParameters(XMLStreamReader reader, APICall query) throws Exception
	{
		if (query == null)
		{
			return;
		}
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
			StringBuilder url = new StringBuilder(query.getBaseURL());
			boolean hasQuery = query.getBaseURL().contains("?");
			if(parameters != null && !parameters.isEmpty() && query.HasParameters())
			{
				for (Map.Entry<String, String> entry : parameters.entrySet())
				{
				    String key = entry.getKey();
					if(StringUtils.isNotBlank(key) && query.getParameters().contains(key)
							&& entry.getValue() != null)
				    {
						String value = entry.getValue();
						url.append(hasQuery ? '&' : '?')
								.append(URLEncoder.encode(key, StandardCharsets.UTF_8.name()))
								.append('=')
								.append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
						hasQuery = true;
						logger.debug("Added Parameter: key=" + key + ", value=" + value);
				    }
				}
			}
			
			return url.toString();
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
