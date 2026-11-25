package io.laniakia.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.laniakia.core.BandcampAPI;
import io.laniakia.domain.Album;
import io.laniakia.domain.Band;
import io.laniakia.domain.Track;

public class BandcampLiveTest {
	@Test
	public void parsesArtistAlbumAndTrackPages() throws Exception {
		String artistUrl = "https://parallels.bandcamp.com/";
		String albumUrl = "https://parallels.bandcamp.com/album/visionaries";
		String trackUrl = "https://parallels.bandcamp.com/track/ultralight";

		BandcampAPI api = new BandcampAPI();
		Band artist = api.getBandProfile(artistUrl);
		Album album = api.getAlbum("parallels", "visionaries");
		Track track = api.getTrack(trackUrl);

		assertNotNull("Artist metadata was not parsed from " + artistUrl, artist);
		assertNotNull("Album metadata was not parsed from " + albumUrl, album);
		assertEquals("Parallels", artist.getName());
		assertTrue("Profile-only lookup must not fetch a discography", artist.getAlbums().isEmpty());
		assertEquals("Visionaries", album.getTitle());
		assertNotNull("Expected cover image URL from album metadata", album.getImageUrl());
		assertEquals("Expected the 12 visible tracks on the album page", 12, album.getTrackList().size());
		assertEquals("Find The Fire", album.getTrackList().get(0).getTitle());
		assertEquals("All We'll Ever Know", album.getTrackList().get(11).getTitle());
		assertNotNull("Track metadata was not parsed from " + trackUrl, track);
		assertEquals("Ultralight", track.getTitle());
	}

	@Test
	public void parsesLabelProfileAndFirstCatalogPage() throws Exception {
		String labelUrl = "https://northernsilenceproductions.bandcamp.com/";
		BandcampAPI api = new BandcampAPI();

		Band profile = api.getLabelProfile(labelUrl);
		assertNotNull("Label profile was not parsed from " + labelUrl, profile);
		assertTrue("Label lookup must identify the account as a label", profile.isIsLabel());

		Band label = api.getLabel(labelUrl);
		assertNotNull("Label catalog was not parsed from " + labelUrl, label);
		assertTrue("Label catalog lookup must identify the account as a label", label.isIsLabel());
		assertTrue("Expected releases on the first label music page", !label.getAlbums().isEmpty());
		assertNotNull("Expected a title on the first listed release", label.getAlbums().get(0).getTitle());
	}
}
