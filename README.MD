# bandcamp-api

A Java/Maven library for retrieving public artist, album, and track metadata from Bandcamp pages.

> This is an unofficial client that reads Bandcamp page data; Bandcamp does not publish or guarantee the page formats and search endpoints it uses. Automated access requires explicit authorization and must stay within its scope. Requests are rate-limited, but pacing does not grant permission. See Bandcamp's [Acceptable Use Policy](https://get.bandcamp.help/en/articles/15263124-bandcamp-s-acceptable-use-and-moderation-policy) and [API access documentation](https://bandcamp.com/developer).

## Requirements

- Java 8 or later
- Maven 3.6.3 or later

## Install

The last published release is `1.0.0`. To use the current development version, build it locally. Maven's default lifecycle runs the live test for `test`, `package`, and `install`. These commands make bounded network requests to Bandcamp and require network access and written authorization. Do not run them unless that authorization applies to your use.

```sh
git clone https://github.com/scriptkittie/bandcamp-api.git
cd bandcamp-api
mvn install
```

The project version is currently `1.1.0-SNAPSHOT`; no new Maven Central release is implied by this source update.

## Usage

```java
import io.laniakia.core.BandcampAPI;
import io.laniakia.domain.Album;
import io.laniakia.domain.Band;
import io.laniakia.domain.Track;

BandcampAPI bandcamp = new BandcampAPI();

Band artist = bandcamp.getBand("https://parallels.bandcamp.com/");
Band profile = bandcamp.getBandProfile("https://parallels.bandcamp.com/"); // profile only, no release crawl
Band labelProfile = bandcamp.getLabelProfile("https://northernsilenceproductions.bandcamp.com/");
Band label = bandcamp.getLabel("https://northernsilenceproductions.bandcamp.com/"); // first catalog page
Band labelCatalog = bandcamp.getLabel("https://northernsilenceproductions.bandcamp.com/", true); // up to five pages
Album album = bandcamp.getAlbum("https://parallels.bandcamp.com/album/visionaries");
Album byName = bandcamp.getAlbum("parallels", "visionaries");
Track track = bandcamp.getTrack("https://parallels.bandcamp.com/track/ultralight");
```

Search for artists and fetch their full release lists (optionally including all result pages):

```java
import java.util.List;
import io.laniakia.core.BandcampAPI;
import io.laniakia.domain.Band;

BandcampAPI bandcamp = new BandcampAPI(2); // at most two workers; HTTP requests remain serialized and paced
List<Band> artists = bandcamp.searchBand("parallels", true, true);
```

For a lighter search that retrieves only artist profiles and avoids fetching each result's discography, use `searchBandProfiles`:

```java
import java.util.List;
import io.laniakia.core.BandcampAPI;
import io.laniakia.domain.Band;

List<Band> artists = bandcamp.searchBandProfiles("parallels", true);
```

`getBand`, `getBandProfile`, `getLabelProfile`, `getLabel`, `getAlbum`, `getTrack`, `searchBand`, and `searchBandProfiles` can throw exceptions when a page cannot be fetched or parsed. A `BandcampAccessChallengeException` is thrown if Bandcamp returns a client-challenge page instead of content. Catch and handle those failures in the calling application. The default HTTP timeout is 15 seconds. Requests are serialized and paced at least two seconds apart across the process; transient failures get at most one retry. HTTP 429/5xx responses honor `Retry-After` up to one minute; longer delays stop the request instead of retrying early.

Label lookup is explicit because a label account's `/music` page lists releases that may belong to separate artist subdomains. `getLabelProfile(url)` reads only the label profile, `getLabel(url)` parses the releases shown on the first catalog page, and `getLabel(url, true)` follows advertised catalog pages with a maximum of five pages total. Use `getAlbum(albumUrl)` with a release's direct URL when you need to fetch an individual release; `getAlbum(labelSubdomain, slug)` only works when that release is hosted on the label's own subdomain.

## Development

```sh
mvn test
mvn package
```

The live test fetches artist, album, track, and label pages, with two-second spacing. It requires network access and written authorization for automated Bandcamp requests. CI builds Java packages on Java 8, 17, and 21, then runs the live check on trusted pushes and manual dispatches using Java 17.

Run only the live comparison with:

```sh
mvn -Dtest=BandcampLiveTest test
```

These checks may fail when Bandcamp changes its page markup, catalog content, or network behavior.

## Compatibility notes

- Public operations include artist search and lookup (`searchBand`, `searchBandProfiles`, `getBand`, `getBandProfile`), label lookup (`getLabelProfile`, `getLabel`), album lookup (`getAlbum`), and single-track lookup (`getTrack`). Profile methods fetch only the account page. `getBand` fetches an artist's music page and release pages; `getLabel` fetches its label catalog and listed release pages. Label pagination is opt-in and capped at five catalog pages. `getAlbum` includes its track list when the page exposes track metadata.
- Parser support for the legacy `BandData` and `TralbumData` page variables remains available for pages that still expose them.
- Search results for a blank query are returned as an empty list.
- This library reads public page markup and embedded data; it is not an official Bandcamp API client.
- The source is licensed under [GPL-3.0](LICENSE.MD).

## Contributing

Please include a reproducible example when reporting parsing issues. Tests are intentionally live-only and make bounded requests.
