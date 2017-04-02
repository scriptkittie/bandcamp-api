package net.cypher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.cypher.core.BandcampAPI;
import net.cypher.domain.Album;
import net.cypher.domain.Band;
import net.cypher.util.APICall;
import net.cypher.util.ConfigUtil;
import net.cypher.util.JSONUtil;

public class APITest 
{
	private static final Logger logger = LogManager.getLogger(APITest.class);
	
	@Test
	public void testConfigFile() throws Exception
	{
		APICall query = ConfigUtil.getConfigValue("search");
		logger.debug("Parameter: " + query.getParameters());
		assertEquals("search", query.getName());
		assertEquals(1, query.getParameters().size());
	}
	
	@Test
	public void testSearchSingle() throws Exception
	{
		BandcampAPI b = new BandcampAPI();
		List<Band> bandList = b.searchBand("parallels", true, false);
		for(Band band : bandList)
		{
			assertTrue(band != null);
			logger.debug("Band Name: "  + band.getName() + ", Band URL: " + band.getUrl() + ", Albums: " + band.getAlbums().size());
		}
		logger.debug("Band search results size: " + bandList.size());
	}
	
	@Test
	public void testSearchMulti() throws Exception
	{
		BandcampAPI b = new BandcampAPI(20);
		List<Band> bandList = b.searchBand("parallels", true, true);
		for(Band band : bandList)
		{
			assertTrue(band != null);
			logger.debug("Band Name: "  + band.getName() + ", Band URL: " + band.getUrl() + ", Albums: " + band.getAlbums().size());
		}
		logger.debug("Band search results size: " + bandList.size());
	}
	
	@Test
	public void testBandParse() throws Exception
	{
		JsonObject p = JSONUtil.parseBandData("http://parallels.bandcamp.com/music");
		Band b = new Gson().fromJson(p.toString(), Band.class);
		logger.debug("Json POJO: " + b.toString());
		assertTrue(b != null);
		assertEquals("Parallels", b.getName());
	}
	
	@Test
	public void testMetadata() throws Exception
	{
		BandcampAPI b = new BandcampAPI();
		Band band = b.getBand("http://parallels.bandcamp.com/");
		assertTrue(band.getTrackList().size() > 0);
		
		for(Album track: band.getTrackList())
		{
			logger.debug("Track: " + track.toString());
		}
	}
	
	@Test
	public void testSingleTracks() throws Exception
	{
		BandcampAPI b = new BandcampAPI();
		Band band = b.getBand("https://thaddeusdavid.bandcamp.com");
		assertTrue(band.getTrackList().size() > 0);
		
		for(Album track: band.getTrackList())
		{
			logger.debug("Track: " + track.toString());
		}
	}
	
	@Test
	public void testGetAlbum() throws Exception
	{
		BandcampAPI b = new BandcampAPI();
		Album album = b.getAlbum("https://parallels.bandcamp.com/album/visionaries");
		assertTrue(album != null);
		album = b.getAlbum("parallels", "visionaries");
		assertTrue(album != null);
	}
}
