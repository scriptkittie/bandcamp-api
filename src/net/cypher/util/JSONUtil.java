package net.cypher.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;

public class JSONUtil 
{
	private static final Logger logger = LogManager.getLogger(JSONUtil.class);
	
	public static JsonObject parseBandData(String URL) throws Exception
	{
		Elements scriptTagList = HttpUtil.getScriptList(URL);
		for(Element element : scriptTagList)
		{
			String text = element.outerHtml();
			if(StringUtils.containsIgnoreCase(text, "var BandData"))
			{
				if(!StringUtils.containsIgnoreCase(text, "current:"))
				{
					String bandData = extractJson(text, "var BandData = ", "ReferrerToken",true);
					if(StringUtils.isNotBlank(bandData))
					{
						try
						{
							return new JsonParser().parse(bandData).getAsJsonObject();
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					String bandData = extractJson(text, "var BandData = ", "var EmbedData",false);
					if(StringUtils.isNotBlank(bandData))
					{
						try
						{
							
							JsonReader reader = new JsonReader(new StringReader(bandData));
							return new JsonParser().parse(reader).getAsJsonObject();
						} 
						catch (Exception e)
						{
							return null;
						}
					}
				}
			}
		}
		return null;
	}
	
	public static Map<String, Object> parseAlbumData(String albumURL) throws Exception
	{
		Map<String, Object> albumMap = new HashMap<String, Object>();
		Elements scriptTags = HttpUtil.getScriptList(albumURL);
		for(Element element : scriptTags)
		{
			String text = element.outerHtml();
			if(StringUtils.containsIgnoreCase(text, "var TralbumData"))
			{
				String albumData = extractJson(text, "var TralbumData =", "};",false) + "};";
				if(StringUtils.isNotBlank(albumData))
				{
					Map<String, String> jsonMap = getJsonMap(albumData);
					JsonObject albumJsonObject =  new JsonParser().parse(jsonMap.get("current")).getAsJsonObject();
					List<JsonObject> trackList = getTrackInfo(jsonMap.get("trackinfo"));
					albumMap.put("albumMetadata", albumJsonObject);
					albumMap.put("trackMetadata", trackList);
					return albumMap;
				}
			}
		}
		return null;
	}
	
	public static List<JsonObject> getTrackInfo(String json)
	{
		Gson gson = new Gson();
		List<JsonObject> jsonTrackList = new ArrayList<JsonObject>();
		JsonStreamParser parser = new JsonStreamParser( new StringReader(json.replace("},{", "}{")));
		while (parser.hasNext())
		{
			jsonTrackList.add(gson.fromJson(parser.next(),JsonObject.class));
		}
		return jsonTrackList;
	}
	
	private static Map<String, String> getJsonMap(String jsonString)
	{
		Map<String,String> jsonMap = new HashMap<String,String>();
		List<String> lineList = Arrays.asList(StringUtils.split(jsonString, "\n"));
		for(String line: lineList)
		{
			String[] splitJson = line.split(":", 2);
			if(splitJson != null && splitJson.length > 1)
			{
				jsonMap.put(splitJson[0].trim(), normalizeJsonString(splitJson[1].trim()));
			}
		}
		
		return jsonMap;
	}
	
	public static String extractJson(String input, String startTag, String endTag, boolean removeLast)
	{
		if(removeLast)
		{
			return removeLastCharacter(getJsonData(input.trim(),startTag,endTag).trim());
		}
		else
		{
			return getJsonData(input.trim(),startTag,endTag).trim();
		}
		
	}
	
	public static String removeLastCharacter(String str) 
	{
	    if (str != null && str.length() > 0) 
	    {
	      return str.substring(0, str.length()-1);
	    }
	    return null;
	}
	
	public static String getJsonData(String input, String startTag, String endTag)
	{
		if(StringUtils.isNotBlank(startTag) && StringUtils.isNotBlank(endTag))
		{
			return StringUtils.substringBetween(input,startTag, endTag);
		}
		return null;
	}
	
	public static String normalizeJsonString(String input)
	{
		if (input.length() > 0)
		{
			if(input.charAt(input.length() - 1) == ',')
			{
				input = input.substring(0, input.length()-1);
			}
			if(input.charAt(0) == '"' && input.charAt(0) == input.charAt(input.length() - 1))
			{
				input = input.substring(1, input.length()-1);
			}
			if(input.charAt(0) == '[' &&  input.charAt(input.length() - 1) == ']')
			{
				input = input.substring(1, input.length()-1);
			}
			
		}
		return input;
	}
	
	public static List<String> getPageNumbers(String html) throws Exception
	{
		Document d = HttpUtil.getDocument(html);
		if(d != null)
		{
			List<String> pageNumberURLList = new ArrayList<String>();
			Elements pageLinks = d.select("a[class*=pagenum]");
			for(Element page : pageLinks)
			{
				String pageURL = "https://bandcamp.com/search" + page.attr("href");
				logger.debug("New search result page: " + pageURL);
				pageNumberURLList.add(pageURL);
			}
			if(!pageNumberURLList.isEmpty())
			{
				pageNumberURLList.add(0, pageNumberURLList.get(0).replace("search?page=2", "search?page=1"));
				logger.debug("Added first page: " + pageNumberURLList.get(0));
			}
			return pageNumberURLList;
		}
		return null;
	}
	
	public static Elements getBandTypes(Elements searchLinks, FilterType filterType)
	{
		for(Iterator<Element> i = searchLinks.iterator(); i.hasNext();) {
			Element itemType = i.next().parent().select("div[class*=itemtype]").first();
			if(itemType.hasText() && StringUtils.isNotBlank(itemType.text().trim()) && !itemType.text().equalsIgnoreCase(filterType.toString()))
			{
				i.remove();
			}
		}
		return searchLinks;
	}
}
