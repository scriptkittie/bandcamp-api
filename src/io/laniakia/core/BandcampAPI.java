package io.laniakia.core;

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

import io.laniakia.domain.Album;
import io.laniakia.domain.Band;
import io.laniakia.domain.Track;
import io.laniakia.util.ConfigUtil;
import io.laniakia.util.FilterType;
import io.laniakia.util.HttpUtil;
import io.laniakia.util.JSONUtil;

public class BandcampAPI 
{
	private static final Logger logger = LogManager.getLogger(BandcampAPI.class);
	private static final int MAX_THREAD_POOL_SIZE = 2;
	private final int THREAD_POOL_SIZE;
	
	public BandcampAPI(int threadPoolSize)
	{
		if (threadPoolSize < 1)
		{
			throw new IllegalArgumentException("threadPoolSize must be greater than zero");
		}
		this.THREAD_POOL_SIZE = Math.min(threadPoolSize, MAX_THREAD_POOL_SIZE);
	}
	
	public BandcampAPI()
	{	
		this(1);
	}

	public List<Band> searchBand(final String query, boolean multiplePages, boolean asynchronous) throws Exception
	{
		return searchBand(query, multiplePages, asynchronous, true);
	}

	/** Search artist profiles without fetching every artist's release pages. */
	public List<Band> searchBandProfiles(String query, boolean multiplePages) throws Exception
	{
		return searchBand(query, multiplePages, false, false);
	}

	private List<Band> searchBand(String query, boolean multiplePages, boolean asynchronous,
			boolean includeReleases) throws Exception
	{
		if(StringUtils.isNotBlank(query))
		{
			List<Band> bandSearchResults = new ArrayList<Band>();
			String searchURL = ConfigUtil.getAPICall("search", new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
			{put("q",StringUtils.trim(query));}}
			);
			if (StringUtils.isBlank(searchURL))
			{
				throw new IllegalStateException("Bandcamp search URL is missing from configuration");
			}
			logger.debug("Search URL: " + searchURL);
			Document firstPage = HttpUtil.getDocument(searchURL);
			if (firstPage == null)
			{
				return bandSearchResults;
			}
			bandSearchResults.addAll(getBandsFromSearchPage(firstPage, asynchronous, includeReleases));
			if (multiplePages)
			{
				for (String pageURL : JSONUtil.getPageNumbers(firstPage))
				{
					Document nextPage = HttpUtil.getDocument(pageURL);
					if (nextPage != null)
					{
						bandSearchResults.addAll(getBandsFromSearchPage(nextPage, asynchronous, includeReleases));
					}
				}
			}
			
			return bandSearchResults;
		}
		return new ArrayList<Band>();
	}
	
	private List<Band> getBandsFromSearchPage(Document searchPage, boolean asynchronous,
			boolean includeReleases) throws Exception
	{
		if (searchPage == null)
		{
			return new ArrayList<Band>();
		}
		List<String> bandURLs = getBandURLsFromSearchPage(searchPage);
		logger.debug("Band links to process: " + bandURLs.size());
		if(!bandURLs.isEmpty())
		{
			List<Band> bandList;
			if(asynchronous)
			{
				bandList = processBandAsynchronous(bandURLs, includeReleases);
			}
			else
			{
				bandList = new ArrayList<Band>();
				for(String bandURL : bandURLs)
				{
					Band band = processBand(bandURL, includeReleases);
					if(band != null)
					{
						logger.debug("Processing band on single thread: " + bandURL);
						bandList.add(band);
					}
				}
			}
			return bandList;
		}
		return new ArrayList<Band>();
	}

	static List<String> getBandURLsFromSearchPage(Document searchPage)
	{
		List<String> bandURLs = new ArrayList<String>();
		if (searchPage == null)
		{
			return bandURLs;
		}
		Elements bandLinks = searchPage.select("div[class*=itemurl]");
		JSONUtil.getBandTypes(bandLinks, FilterType.ARTIST);
		Set<String> uniqueUrls = new java.util.LinkedHashSet<String>();
		for (Element result : bandLinks)
		{
			Element anchor = result.selectFirst("a[href]");
			if (anchor == null)
			{
				continue;
			}
			String bandURL = anchor.absUrl("href");
			if (StringUtils.isBlank(bandURL))
			{
				bandURL = anchor.attr("href");
			}
			if (StringUtils.endsWithIgnoreCase(bandURL, ".bandcamp.com")
					|| StringUtils.containsIgnoreCase(bandURL, ".bandcamp.com/"))
			{
				uniqueUrls.add(bandURL);
			}
		}
		bandURLs.addAll(uniqueUrls);
		return bandURLs;
	}
	
	private List<Band> processBandAsynchronous(List<String> bandURLs, boolean includeReleases) throws Exception
	{
		List<Band> bandList = new ArrayList<Band>();
		logger.debug("Available CPU Processors: " + Runtime.getRuntime().availableProcessors());
		logger.debug("Processing bands asynchronously wih threadpool size of: " + THREAD_POOL_SIZE);
		ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		List<Future<Band>> futures = new ArrayList<Future<Band>>();
		for(final String bandURL : bandURLs)
		{
			Callable<Band> callable = new Callable<Band>()
			{
	            public Band call() throws Exception
	            {
	            	logger.debug("Processing band asynchronously with URL: " + bandURL);
				Band band = processBand(bandURL, includeReleases);
					return band;
	            }
			};
			futures.add(service.submit(callable));
		}
		service.shutdown();
		
		try
		{
			for (Future<Band> future : futures)
			{
				Band band = future.get();
				if (band != null)
				{
					bandList.add(band);
				}
			}
		}
		catch (InterruptedException interrupted)
		{
			Thread.currentThread().interrupt();
			throw interrupted;
		}
		finally
		{
			service.shutdownNow();
		}
		return bandList;
	}
	
	private Band processBand(String bandURL, boolean includeReleases) throws Exception
	{
		String normalizedURL = normalizeArtistUrl(bandURL);
		if (normalizedURL != null)
		{
			Band band = includeReleases ? getBand(normalizedURL) : getBandProfile(normalizedURL);
			if(band != null)
			{
				return band;
			}
		}
		return null;
	}

	static String normalizeArtistUrl(String value)
	{
		try
		{
			java.net.URI uri = new java.net.URI(StringUtils.trimToEmpty(value));
			String host = uri.getHost();
			if (host == null || uri.getUserInfo() != null
					|| !("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
					|| !host.toLowerCase().endsWith(".bandcamp.com"))
			{
				return null;
			}
			return new java.net.URI("https", null, host.toLowerCase(), -1, "/", null, null).toString();
		}
		catch (java.net.URISyntaxException invalidUrl)
		{
			return null;
		}
	}
	
	public Band getBand(String bandURL) throws Exception
	{
		return getBand(bandURL, true);
	}

	/** Fetch only an artist's profile metadata without crawling its releases. */
	public Band getBandProfile(String bandURL) throws Exception
	{
		return getBand(bandURL, false);
	}

	private Band getBand(String bandURL, boolean includeReleases) throws Exception
	{
		JsonObject bandJson = JSONUtil.parseBandData(bandURL);
		if(bandJson != null && !bandJson.isJsonNull())
		{
			Band band = new Gson().fromJson(bandJson.toString(), Band.class);
			if(band != null && includeReleases)
			{
				String albumListURL = HttpUtil.addPaths(bandURL, "music");
				Document musicPage = HttpUtil.getDocument(albumListURL);
				Set<String> albumLinkList = getAlbumLinkList(musicPage, bandURL);
				Set<String> trackLinkList = getTrackLinkList(musicPage, bandURL);
				if(albumLinkList != null && !albumLinkList.isEmpty())
				{
					logger.debug("Processing Albums: " + albumLinkList.size());
					for(String albumLink : albumLinkList)
					{
						Map<String, Object> albumTrackMetadata = JSONUtil.parseAlbumData(albumLink);
						Album album = processaAlbumTrackMetadata(albumTrackMetadata);
						if(album != null)
						{
							album.setIsTrack(false);
							band.addAlbum(album);
						}
					}
				}
				if(trackLinkList != null && !trackLinkList.isEmpty())
				{
					logger.debug("Processing single tracks without albums: " + trackLinkList.size());
					for(String trackLink : trackLinkList)
					{
						Map<String, Object> trackMetadata = JSONUtil.parseAlbumData(trackLink);
						Album album = processaAlbumTrackMetadata(trackMetadata);
						if(album != null)
						{
							album.setIsTrack(true);
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
		String artist = StringUtils.trimToEmpty(bandName).toLowerCase(java.util.Locale.ROOT);
		String slug = StringUtils.trimToEmpty(albumName);
		if (!artist.matches("[a-z0-9-]+"))
		{
			throw new IllegalArgumentException("bandName must be a Bandcamp subdomain");
		}
		if (StringUtils.isBlank(slug) || slug.contains("/") || slug.contains("\\"))
		{
			throw new IllegalArgumentException("albumName must be a non-empty album path segment");
		}
		try
		{
			String encodedSlug = new java.net.URI(null, null, slug, null).getRawPath();
			return getAlbum("https://" + artist + ".bandcamp.com/album/" + encodedSlug);
		}
		catch (java.net.URISyntaxException invalidSlug)
		{
			throw new IllegalArgumentException("albumName is not a valid album path segment", invalidSlug);
		}
	}

	/** Fetch a single public track by its Bandcamp track page URL. */
	public Track getTrack(String trackURL) throws Exception
	{
		JsonObject trackData = JSONUtil.parseTrackData(trackURL);
		return trackData == null ? null : new Gson().fromJson(trackData, Track.class);
	}
	
	private Album processaAlbumTrackMetadata(Map<String, Object> albumTrackMetadata)
	{
		Album album = null;
		if (albumTrackMetadata == null)
		{
			return null;
		}
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
			Object trackMetadata = albumTrackMetadata.get("trackMetadata");
			if(trackMetadata instanceof Iterable)
			{
				for(Object item : (Iterable<?>) trackMetadata)
				{
					if (!(item instanceof JsonObject))
					{
						continue;
					}
					Track track = new Gson().fromJson((JsonObject) item, Track.class);
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
	
	private Set<String> getAlbumLinkList(Document doc, String baseURL)
	{
		Set<String> linkList = new HashSet<String>();
		if (doc == null)
		{
			return linkList;
		}
		Elements albumLinks = doc.select("a[href*=/album/]");
		for(Element link : albumLinks)
		{
			String albumLink = resolveBandcampLink(link, baseURL);
			if (albumLink != null && albumLink.contains("/album/"))
			{
				linkList.add(albumLink);
			}
			
		}
		return linkList;
	}
	
	private Set<String> getTrackLinkList(Document doc, String baseURL)
	{
		Set<String> linkList = new HashSet<String>();
		if (doc == null)
		{
			return linkList;
		}
		Elements albumLinks = doc.select("a[href*=/track/]");
		for(Element link : albumLinks)
		{
			String trackLink = resolveBandcampLink(link, baseURL);
			if (trackLink != null && trackLink.contains("/track/"))
			{
				linkList.add(trackLink);
			}
			
		}
		return linkList;
	}

	private String resolveBandcampLink(Element link, String baseURL)
	{
		String absoluteUrl = link.absUrl("href");
		if (StringUtils.isBlank(absoluteUrl))
		{
			String href = link.attr("href");
			if (StringUtils.isBlank(href))
			{
				return null;
			}
			absoluteUrl = href.startsWith("/") ? HttpUtil.addPaths(baseURL, href) : href;
		}
		try
		{
			String host = new java.net.URI(absoluteUrl).getHost();
			return host != null && (host.equalsIgnoreCase("bandcamp.com")
					|| host.toLowerCase().endsWith(".bandcamp.com")) ? absoluteUrl : null;
		}
		catch (java.net.URISyntaxException invalidUrl)
		{
			return null;
		}
	}
}
