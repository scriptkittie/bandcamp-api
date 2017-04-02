package net.cypher.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.cypher.domain.Album;
import net.cypher.domain.Band;
import net.cypher.domain.Track;
import net.cypher.util.ConfigUtil;
import net.cypher.util.FilterType;
import net.cypher.util.HttpUtil;
import net.cypher.util.JSONUtil;

public class BandcampAPI 
{
	private static final Logger logger = LogManager.getLogger(BandcampAPI.class);
	private int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	public BandcampAPI(int threadPoolSize)
	{
		this.THREAD_POOL_SIZE = threadPoolSize;
	}
	
	public BandcampAPI()
	{	
	}

	public List<Band> searchBand(final String query, boolean multiplePages, boolean asynchronous) throws Exception
	{
		if(StringUtils.isNotBlank(query))
		{
			List<Band> bandSearchResults = new ArrayList<Band>();
			String searchURL = ConfigUtil.getAPICall("search", new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
			{put("q",query);}}
			);
			//Praise cthulhu
			logger.debug("Search URL: " + searchURL);
			List<String> searchPageList = JSONUtil.getPageNumbers(searchURL);
			if(searchPageList != null && !searchPageList.isEmpty())
			{
				for(String page: searchPageList)
				{
					List<Band> bandList = getBandsFromSearchPage(page, asynchronous);
					if(bandList != null)
					{
						bandSearchResults.addAll(bandList);
					}
					if(!multiplePages)
					{
						break;
					}
				}
			}
			else
			{
				List<Band> bandList = getBandsFromSearchPage(searchURL, asynchronous);
				if(bandList != null)
				{
					bandSearchResults.addAll(bandList);
				}
			}
			
			return bandSearchResults;
		}
		return null;
	}
	
	private List<Band> getBandsFromSearchPage(String pageLink, boolean asynchronous) throws Exception
	{
		Document searchPage = HttpUtil.getDocument(pageLink);
		Elements bandLinks = searchPage.select("div[class*=itemurl]");
		bandLinks = JSONUtil.getBandTypes(bandLinks, FilterType.ARTIST);
		logger.debug("Band links to process: " + bandLinks.size());
		if(bandLinks != null && !bandLinks.isEmpty())
		{
			List<Band> bandList;
			if(asynchronous)
			{
				bandList = processBandAsynchronous(bandLinks);
			}
			else
			{
				bandList = new ArrayList<Band>();
				for(Element divURL : bandLinks)
				{
					String bandURL = divURL.select("a").first().text();
					Band band = processBand(bandURL);
					if(band != null)
					{
						logger.debug("Processing band on single thread: " + bandURL);
						bandList.add(band);
					}
				}
			}
			return bandList;
		}
		return null;
	}
	
	private List<Band> processBandAsynchronous(Elements bandLinks) throws Exception
	{
		List<Band> bandList = new ArrayList<Band>();
		logger.debug("Available CPU Processors: " + Runtime.getRuntime().availableProcessors());
		logger.debug("Processing bands asynchronously wih threadpool size of: " + THREAD_POOL_SIZE);
		ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		List<Future<Band>> futures = new ArrayList<Future<Band>>();
		for(final Element divURL : bandLinks)
		{
			Callable<Band> callable = new Callable<Band>()
			{
	            public Band call() throws Exception
	            {
	            	String bandURL = divURL.select("a").first().text();
	            	logger.debug("Processing band asynchronously with URL: " + bandURL);
	            	Band band =  processBand(bandURL);
					return band;
	            }
			};
			futures.add(service.submit(callable));
		}
		service.shutdown();
		
		for (Future<Band> future : futures) 
		{
			bandList.add(future.get());
		}
		return bandList;
	}
	
	private Band processBand(String bandURL) throws Exception
	{
		if(StringUtils.isNotBlank(bandURL) && bandURL.endsWith("bandcamp.com"))
		{
			Band band = getBand(bandURL);
			if(band != null)
			{
				return band;
			}
		}
		return null;
	}
	
	public Band getBand(String bandURL) throws Exception
	{
		JsonObject bandJson = JSONUtil.parseBandData(bandURL);
		if(bandJson != null && !bandJson.isJsonNull())
		{
			Band band = new Gson().fromJson(bandJson.toString(), Band.class);
			if(band != null )
			{
				String albumListURL = HttpUtil.addPaths(bandURL, "music");
				Set<String> albumLinkList = getAlbumLinkList(albumListURL, bandURL);
				Set<String> trackLinkList = getTrackLinkList(albumListURL, bandURL);
				if(albumLinkList != null && !albumLinkList.isEmpty())
				{
					logger.debug("Processing Albums: " + albumLinkList.size());
					for(String albumLink : albumLinkList)
					{
						Album album = null;
						Map<String, Object> albumTrackMetadata = JSONUtil.parseAlbumData(albumLink);
						album = processaAlbumTrackMetadata(albumTrackMetadata);
						album.setIsTrack(false);
						if(album != null)
						{
							band.addAlbum(album);
						}
					}
				}
				if(trackLinkList != null && !trackLinkList.isEmpty())
				{
					Album album = null;
					logger.debug("Processing single tracks without albums: " + trackLinkList.size());
					for(String trackLink : albumLinkList)
					{
						Map<String, Object> trackMetadata = JSONUtil.parseAlbumData(trackLink);
						logger.debug("Track Metadata JSON: " + trackMetadata.get("albumMetadata"));
						logger.debug("Track JSON: " + trackMetadata.get("trackMetadata"));
						album = processaAlbumTrackMetadata(trackMetadata);
						album.setIsTrack(true);
						if(album != null)
						{
							band.addTrack(album);
						}
					}
				}
			}
			return band;
		}
		return null;
	}
	
	public Album getAlbum(String albumURL) throws Exception
	{
		Map<String, Object> albumTrackMetadata = JSONUtil.parseAlbumData(albumURL);
		Album album = processaAlbumTrackMetadata(albumTrackMetadata);
		return album;
	}
	
	public Album getAlbum(String bandName, String albumName) throws Exception
	{
		return getAlbum("https://" + bandName + ".bandcamp.com/album/" + albumName);
	}
	
	private Album processaAlbumTrackMetadata(Map<String, Object> albumTrackMetadata)
	{
		Album album = null;
		logger.debug("Album JSON: " + albumTrackMetadata.get("albumMetadata"));
		logger.debug("Album Track JSON: " + albumTrackMetadata.get("trackMetadata"));
		if(albumTrackMetadata.get("albumMetadata") != null)
		{
			logger.debug("Seralizing Album JSON...");
			album = new Gson().fromJson(albumTrackMetadata.get("albumMetadata").toString(), Album.class);
			if(album != null)
			{
				logger.debug("Album serialized: " + album.toString());
			}
		}
		if(albumTrackMetadata.get("trackMetadata") != null)
		{
			logger.debug("Seralizing track JSON...");
			if(albumTrackMetadata.get("trackMetadata") instanceof List)
			{
				List<JsonObject> trackJSONList = (List<JsonObject>) albumTrackMetadata.get("trackMetadata");
				for(JsonObject trackJSON: trackJSONList)
				{
					Track track = new Gson().fromJson(trackJSON.toString(), Track.class);
					if(track != null)
					{
						logger.debug("Processing Track: " + track.toString());
						if(album != null)
						{
							album.addTrack(track);
						}
					}
				}
			}
		}
		return album;
	}
	
	private Set<String> getAlbumLinkList(String bandURL, String baseURL) throws Exception
	{
		Set<String> linkList = new HashSet<String>();
		Document doc = HttpUtil.getDocument(bandURL);
		Elements albumLinks = doc.select("a[href*=/album/]");
		for(Element link : albumLinks)
		{
			String albumLink = link.attr("href");
			if(StringUtils.isNotBlank(albumLink) && albumLink.contains(".bandcamp.com/album")){
				linkList.add(albumLink);
			}
			else if(StringUtils.isNotBlank(albumLink) && albumLink.startsWith("/album/"))
			{
				linkList.add(HttpUtil.addPaths(baseURL, link.attr("href")));
			}
			
		}
		return linkList;
	}
	
	private Set<String> getTrackLinkList(String bandURL, String baseURL) throws Exception
	{
		Set<String> linkList = new HashSet<String>();
		Document doc = HttpUtil.getDocument(bandURL);
		Elements albumLinks = doc.select("a[href*=/track/]");
		for(Element link : albumLinks)
		{
			String albumLink = link.attr("href");
			if(StringUtils.isNotBlank(albumLink) && albumLink.contains(".bandcamp.com/track")){
				linkList.add(albumLink);
			}
			else if(StringUtils.isNotBlank(albumLink) && albumLink.startsWith("/track/"))
			{
				linkList.add(HttpUtil.addPaths(baseURL, link.attr("href")));
			}
			
		}
		return linkList;
	}
}
