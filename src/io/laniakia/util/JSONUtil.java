package io.laniakia.util;

import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JSONUtil 
{
	private static final Logger logger = LogManager.getLogger(JSONUtil.class);
	
	public static JsonObject parseBandData(String URL) throws Exception
	{
		return parseBandData(HttpUtil.getDocument(URL));
	}

	static JsonObject parseBandData(Document page)
	{
		if (page == null)
		{
			return null;
		}
		Elements scriptTagList = page.select("script[type=text/javascript]");
		for(Element element : scriptTagList)
		{
			String text = element.data();
			if(StringUtils.containsIgnoreCase(text, "var BandData"))
			{
				String bandData = extractObjectAfter(text, "BandData");
				if(StringUtils.isNotBlank(bandData))
				{
					try
					{
						JsonReader reader = new JsonReader(new StringReader(bandData));
						reader.setLenient(true);
						return JsonParser.parseReader(reader).getAsJsonObject();
					}
					catch (Exception e)
					{
						logger.warn("Could not parse Bandcamp artist metadata", e);
					}
				}
			}
		}
		return parseArtistPageMetadata(page);
	}

	private static JsonObject parseArtistPageMetadata(Document page)
	{
		String canonicalUrl = page.select("link[rel=canonical]").attr("href");
		if (StringUtils.isBlank(canonicalUrl))
		{
			canonicalUrl = page.location();
		}
		String name = page.select("meta[property=og:title]").attr("content");
		if (StringUtils.isBlank(name))
		{
			name = page.title();
			int separator = name.lastIndexOf(" | ");
			if (separator >= 0)
			{
				name = name.substring(separator + 3);
			}
		}
		if (StringUtils.isBlank(name) || StringUtils.isBlank(canonicalUrl))
		{
			return null;
		}

		JsonObject artist = new JsonObject();
		artist.addProperty("name", name.trim());
		artist.addProperty("url", canonicalUrl);
		artist.addProperty("https_url", canonicalUrl);
		artist.addProperty("local_url", canonicalUrl);
		artist.addProperty("https_local_url", canonicalUrl);
		try
		{
			String host = new java.net.URI(canonicalUrl).getHost();
			if (host != null && host.endsWith(".bandcamp.com"))
			{
				artist.addProperty("subdomain", host.substring(0, host.length() - ".bandcamp.com".length()));
			}
		}
		catch (Exception e)
		{
			logger.debug("Could not derive Bandcamp subdomain from {}", canonicalUrl, e);
		}
		String description = page.select("meta[name=description]").attr("content");
		if (StringUtils.isNotBlank(description))
		{
			artist.addProperty("description", description);
		}
		return artist;
	}
	
	public static Map<String, Object> parseAlbumData(String albumURL) throws Exception
	{
		return parseAlbumData(HttpUtil.getDocument(albumURL));
	}

	public static JsonObject parseTrackData(String trackURL) throws Exception
	{
		return parseTrackData(HttpUtil.getDocument(trackURL));
	}

	static JsonObject parseTrackData(Document page)
	{
		if (page == null)
		{
			return null;
		}
		for (Element script : page.select("script[type=text/javascript]"))
		{
			String text = script.data();
			if (!StringUtils.containsIgnoreCase(text, "var TralbumData"))
			{
				continue;
			}
			String json = extractObjectAfter(text, "TralbumData");
			try
			{
				JsonObject tralbum = JsonParser.parseString(json).getAsJsonObject();
				JsonElement trackInfo = tralbum.get("trackinfo");
				if (trackInfo != null && trackInfo.isJsonArray() && !trackInfo.getAsJsonArray().isEmpty())
				{
					return tralbum.getAsJsonArray("trackinfo").get(0).getAsJsonObject();
				}
			}
			catch (Exception e)
			{
				logger.debug("Could not parse legacy Bandcamp track metadata", e);
			}
		}

		JsonObject recording = findSchemaTypeInScripts(page, "MusicRecording");
		if (recording == null)
		{
			return null;
		}
		JsonObject track = new JsonObject();
		copySchemaProperty(recording, track, "name", "title");
		copySchemaProperty(recording, track, "url", "title_link");
		JsonElement duration = recording.get("duration");
		if (duration != null && !duration.isJsonNull())
		{
			Double seconds = parseDurationSeconds(duration.getAsString());
			if (seconds != null)
			{
				track.addProperty("duration", seconds);
			}
		}
		return track;
	}

	static Map<String, Object> parseAlbumData(Document page)
	{
		Map<String, Object> albumMap = new HashMap<String, Object>();
		if (page == null)
		{
			return null;
		}
		Elements scriptTags = page.select("script[type=text/javascript]");
		for(Element element : scriptTags)
		{
			String text = element.data();
			if(StringUtils.containsIgnoreCase(text, "var TralbumData"))
			{
				String albumData = extractObjectAfter(text, "TralbumData");
				if(StringUtils.isNotBlank(albumData))
				{
					try
					{
						JsonObject tralbum = JsonParser.parseString(albumData).getAsJsonObject();
						if (!tralbum.has("current") || !tralbum.get("current").isJsonObject())
						{
							continue;
						}
						albumMap.put("albumMetadata", tralbum.getAsJsonObject("current"));
						JsonElement trackInfo = tralbum.get("trackinfo");
						List<JsonObject> trackList = trackInfo != null && trackInfo.isJsonArray()
								? getTrackInfo(trackInfo.toString()) : new ArrayList<JsonObject>();
						albumMap.put("trackMetadata", trackList);
						return albumMap;
					}
					catch (Exception e)
					{
						logger.warn("Could not parse Bandcamp album metadata", e);
						continue;
					}
				}
			}
		}
		return parseAlbumJsonLd(page);
	}

	private static Map<String, Object> parseAlbumJsonLd(Document page)
	{
		for (Element script : page.select("script[type=application/ld+json]"))
		{
			try
			{
				JsonElement root = JsonParser.parseString(script.data());
				JsonObject schema = findAlbumSchema(root);
				if (schema == null)
				{
					continue;
				}
				JsonObject metadata = new JsonObject();
				copySchemaProperty(schema, metadata, "name", "title");
				copySchemaProperty(schema, metadata, "url", "title_link");
				copySchemaProperty(schema, metadata, "datePublished", "release_date");
				String imageUrl = schemaImageUrl(schema.get("image"));
				if (imageUrl != null)
				{
					metadata.addProperty("image", imageUrl);
				}
				JsonElement artist = schema.get("byArtist");
				if (artist != null && artist.isJsonObject() && artist.getAsJsonObject().has("name"))
				{
					metadata.add("artist", artist.getAsJsonObject().get("name"));
				}

				List<JsonObject> tracks = new ArrayList<JsonObject>();
				collectSchemaTracks(schema.get("track"), tracks);
				if (tracks.isEmpty())
				{
					tracks = parseTrackRows(page);
				}
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("albumMetadata", metadata);
				result.put("trackMetadata", tracks);
				return result;
			}
			catch (Exception e)
			{
				logger.debug("Could not parse Bandcamp JSON-LD metadata", e);
			}
		}
		return null;
	}

	private static JsonObject findAlbumSchema(JsonElement element)
	{
		if (element == null || element.isJsonNull())
		{
			return null;
		}
		if (element.isJsonArray())
		{
			for (JsonElement child : element.getAsJsonArray())
			{
				JsonObject found = findAlbumSchema(child);
				if (found != null)
				{
					return found;
				}
			}
		}
		else if (element.isJsonObject())
		{
			JsonObject object = element.getAsJsonObject();
			if (hasSchemaType(object, "MusicAlbum") || hasSchemaType(object, "AlbumRelease"))
			{
				return object;
			}
			if (object.has("@graph"))
			{
				return findAlbumSchema(object.get("@graph"));
			}
		}
		return null;
	}

	private static JsonObject findSchemaTypeInScripts(Document page, String schemaType)
	{
		for (Element script : page.select("script[type=application/ld+json]"))
		{
			try
			{
				JsonObject found = findSchemaType(JsonParser.parseString(script.data()), schemaType);
				if (found != null)
				{
					return found;
				}
			}
			catch (Exception e)
			{
				logger.debug("Could not parse Bandcamp JSON-LD while looking for {}", schemaType, e);
			}
		}
		return null;
	}

	private static JsonObject findSchemaType(JsonElement element, String schemaType)
	{
		if (element == null || element.isJsonNull())
		{
			return null;
		}
		if (element.isJsonArray())
		{
			for (JsonElement child : element.getAsJsonArray())
			{
				JsonObject found = findSchemaType(child, schemaType);
				if (found != null)
				{
					return found;
				}
			}
		}
		else if (element.isJsonObject())
		{
			JsonObject object = element.getAsJsonObject();
			if (hasSchemaType(object, schemaType))
			{
				return object;
			}
			for (Map.Entry<String, JsonElement> entry : object.entrySet())
			{
				JsonObject found = findSchemaType(entry.getValue(), schemaType);
				if (found != null)
				{
					return found;
				}
			}
		}
		return null;
	}

	private static boolean hasSchemaType(JsonObject object, String schemaType)
	{
		JsonElement type = object.get("@type");
		if (type == null || type.isJsonNull())
		{
			return false;
		}
		if (type.isJsonArray())
		{
			for (JsonElement candidate : type.getAsJsonArray())
			{
				if (candidate.isJsonPrimitive() && schemaType.equals(candidate.getAsString()))
				{
					return true;
				}
			}
			return false;
		}
		return type.isJsonPrimitive() && schemaType.equals(type.getAsString());
	}

	private static void copySchemaProperty(JsonObject source, JsonObject target, String sourceName, String targetName)
	{
		JsonElement value = source.get(sourceName);
		if (value != null && !value.isJsonNull())
		{
			target.add(targetName, value);
		}
	}

	private static String schemaImageUrl(JsonElement image)
	{
		if (image == null || image.isJsonNull())
		{
			return null;
		}
		if (image.isJsonArray())
		{
			for (JsonElement candidate : image.getAsJsonArray())
			{
				String value = schemaImageUrl(candidate);
				if (StringUtils.isNotBlank(value))
				{
					return value;
				}
			}
			return null;
		}
		if (image.isJsonPrimitive())
		{
			return image.getAsString();
		}
		if (image.isJsonObject())
		{
			JsonObject imageObject = image.getAsJsonObject();
			JsonElement url = imageObject.has("url") ? imageObject.get("url") : imageObject.get("contentUrl");
			return url == null || url.isJsonNull() ? null : url.getAsString();
		}
		return null;
	}

	private static void collectSchemaTracks(JsonElement element, List<JsonObject> tracks)
	{
		if (element == null || element.isJsonNull())
		{
			return;
		}
		if (element.isJsonArray())
		{
			for (JsonElement child : element.getAsJsonArray())
			{
				collectSchemaTracks(child, tracks);
			}
			return;
		}
		if (!element.isJsonObject())
		{
			return;
		}
		JsonObject object = element.getAsJsonObject();
		if (isTrackSchema(object))
		{
			JsonObject track = new JsonObject();
			copySchemaProperty(object, track, "name", "title");
			copySchemaProperty(object, track, "url", "title_link");
			JsonElement duration = object.get("duration");
			if (duration != null && !duration.isJsonNull())
			{
				Double durationSeconds = parseDurationSeconds(duration.getAsString());
				if (durationSeconds != null)
				{
					track.addProperty("duration", durationSeconds);
				}
			}
			track.addProperty("track_num", tracks.size() + 1);
			tracks.add(track);
			return;
		}
		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			collectSchemaTracks(entry.getValue(), tracks);
		}
	}

	private static boolean isTrackSchema(JsonObject object)
	{
		if (!object.has("name"))
		{
			return false;
		}
		JsonElement type = object.get("@type");
		if (type != null && type.isJsonPrimitive()
				&& "MusicRecording".equalsIgnoreCase(type.getAsString()))
		{
			return true;
		}
		return object.has("duration") && object.has("url");
	}

	private static List<JsonObject> parseTrackRows(Document page)
	{
		List<JsonObject> tracks = new ArrayList<JsonObject>();
		for (Element row : page.select("tr.track_row_view"))
		{
			Element title = row.selectFirst(".track-title");
			if (title == null || StringUtils.isBlank(title.text()))
			{
				continue;
			}
			JsonObject track = new JsonObject();
			track.addProperty("title", title.text().trim());
			Element number = row.selectFirst(".track_number");
			track.addProperty("track_num", number == null ? tracks.size() + 1 : parseTrackNumber(number.text(), tracks.size() + 1));
			Element duration = row.selectFirst(".time, .time-col");
			if (duration != null && StringUtils.isNotBlank(duration.text()))
			{
				Double durationSeconds = parseDurationSeconds(duration.text().trim());
				if (durationSeconds != null)
				{
					track.addProperty("duration", durationSeconds);
				}
			}
			Element link = title.closest("a[href]");
			if (link != null)
			{
				track.addProperty("title_link", link.absUrl("href"));
			}
			String trackId = row.attr("data-trackid");
			if (StringUtils.isNumeric(trackId))
			{
				track.addProperty("id", trackId);
			}
			tracks.add(track);
		}
		return tracks;
	}

	private static int parseTrackNumber(String text, int fallback)
	{
		String digits = StringUtils.trimToEmpty(text).replaceAll("[^0-9].*$", "");
		try
		{
			return Integer.parseInt(digits);
		}
		catch (NumberFormatException e)
		{
			return fallback;
		}
	}

	private static Double parseDurationSeconds(String value)
	{
		if (StringUtils.isBlank(value))
		{
			return null;
		}
		try
		{
			if (value.startsWith("P"))
			{
				return (double) Duration.parse(value).getSeconds();
			}
			String[] components = value.split(":");
			if (components.length == 2 || components.length == 3)
			{
				long seconds = 0;
				for (String component : components)
				{
					seconds = seconds * 60 + Long.parseLong(component);
				}
				return (double) seconds;
			}
			return Double.valueOf(value);
		}
		catch (RuntimeException invalidDuration)
		{
			return null;
		}
	}
	
	public static List<JsonObject> getTrackInfo(String json)
	{
		List<JsonObject> jsonTrackList = new ArrayList<JsonObject>();
		if (StringUtils.isNotBlank(json))
		{
			Gson gson = new Gson();
			String trackJson = json.trim();
			if (!trackJson.startsWith("["))
			{
				trackJson = "[" + trackJson + "]";
			}
			JsonElement parsed = JsonParser.parseString(trackJson);
			if (parsed.isJsonArray())
			{
				for (JsonElement track : parsed.getAsJsonArray())
				{
					if (track.isJsonObject())
					{
						jsonTrackList.add(gson.fromJson(track, JsonObject.class));
					}
				}
			}
		}
		return jsonTrackList;
	}
	
	private static String extractObjectAfter(String script, String variableName)
	{
		int variableIndex = script.indexOf(variableName);
		if (variableIndex < 0)
		{
			return null;
		}
		int equalsIndex = script.indexOf('=', variableIndex + variableName.length());
		int start = equalsIndex < 0 ? -1 : script.indexOf('{', equalsIndex + 1);
		if (start < 0)
		{
			return null;
		}

		int depth = 0;
		boolean inString = false;
		boolean escaped = false;
		for (int i = start; i < script.length(); i++)
		{
			char current = script.charAt(i);
			if (inString)
			{
				if (escaped)
				{
					escaped = false;
				}
				else if (current == '\\')
				{
					escaped = true;
				}
				else if (current == '"')
				{
					inString = false;
				}
				continue;
			}
			if (current == '"')
			{
				inString = true;
			}
			else if (current == '{')
			{
				depth++;
			}
			else if (current == '}' && --depth == 0)
			{
				return script.substring(start, i + 1);
			}
		}
		return null;
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
		if (StringUtils.isNotEmpty(input))
		{
			if(input.charAt(input.length() - 1) == ',')
			{
				input = input.substring(0, input.length()-1);
			}
			if(input.length() >= 2 && input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"')
			{
				input = input.substring(1, input.length()-1);
			}
			if(input.length() >= 2 && input.charAt(0) == '[' && input.charAt(input.length() - 1) == ']')
			{
				input = input.substring(1, input.length()-1);
			}
			
		}
		return input;
	}
	
	public static List<String> getPageNumbers(String html) throws Exception
	{
		return getPageNumbers(HttpUtil.getDocument(html));
	}

	/** Return the distinct next-page URLs advertised by a parsed search page. */
	public static List<String> getPageNumbers(Document page)
	{
		List<String> pageNumberURLList = new ArrayList<String>();
		if (page == null)
		{
			return pageNumberURLList;
		}
		java.util.Set<String> seen = new java.util.LinkedHashSet<String>();
		String currentPage = paginationKey(page.location());
		for (Element link : page.select("a.pagenum[href], a[class*=pagenum][href]"))
		{
			String href = link.attr("href");
			if (StringUtils.isBlank(href))
			{
				continue;
			}
			try
			{
				java.net.URI baseUri = new java.net.URI(page.location());
				String pageURL = href.startsWith("?")
						? new java.net.URI(baseUri.getScheme(), baseUri.getAuthority(), baseUri.getPath(),
								href.substring(1), null).toString()
						: baseUri.resolve(href).toString();
				if (!paginationKey(pageURL).equals(currentPage))
				{
					seen.add(pageURL);
				}
			}
			catch (java.net.URISyntaxException invalidLocation)
			{
				logger.debug("Ignoring malformed pagination link: {}", href, invalidLocation);
			}
		}
		pageNumberURLList.addAll(seen);
		return pageNumberURLList;
	}

	private static String paginationKey(String url)
	{
		try
		{
			java.net.URI uri = new java.net.URI(url);
			List<String> queryParts = new ArrayList<String>();
			if (uri.getRawQuery() != null)
			{
				for (String part : uri.getRawQuery().split("&"))
				{
					if (!part.matches("(?i)page=0*1"))
					{
						queryParts.add(part);
					}
				}
			}
			java.util.Collections.sort(queryParts);
			StringBuilder key = new StringBuilder();
			key.append(uri.getScheme()).append("://").append(uri.getAuthority()).append(uri.getPath());
			if (!queryParts.isEmpty())
			{
				key.append('?').append(String.join("&", queryParts));
			}
			return key.toString();
		}
		catch (java.net.URISyntaxException invalidUrl)
		{
			return url;
		}
	}
	
	public static Elements getBandTypes(Elements searchLinks, FilterType filterType)
	{
		if (searchLinks == null || filterType == null)
		{
			return searchLinks;
		}
		for(Iterator<Element> i = searchLinks.iterator(); i.hasNext();) {
			Element link = i.next();
			Element parent = link.parent();
			Element itemType = parent == null ? null : parent.select("div[class*=itemtype]").first();
			if(itemType == null || !itemType.hasText()
					|| !itemType.text().trim().equalsIgnoreCase(filterType.toString()))
			{
				i.remove();
			}
		}
		return searchLinks;
	}
}
