## bandcamp-api

[![Build Status](https://travis-ci.org/scriptkittie/bandcamp-api.svg?branch=master)](https://travis-ci.org/scriptkittie/bandcamp-api)

### Overview

Bandcamp-api is API wrapper written in JAVA to retrieve Bandcamp band, album, and track metadata. It utilizes an underlying existing JSON API framework which pre-exists within bandcamp which is also how the website retrieves its data. This project also provides an easy way to execute asynchronous requests against the API.

## Changelog

<details> 
  <summary>Open spoiler to view changelog </summary>
  
### 1.0.0
- Initial release.
</details>


## Installation
### Install from Maven Central

Just add the following dependencies to your maven pom.xml

```xml
<dependency>
    <groupId>io.laniakia</groupId>
    <artifactId>bandcamp-api</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Example Usage

**Search Endpoint**

Config details on the current bandcamp search REST endpoint are stored in /resources/config.xml

**Initialize BandcampAPI With Default Threadcount**

```java
import io.laniakia.core.BandcampAPI;
...
BandcampAPI bandCampObj = new BandcampAPI();
```
**Initialize BandcampAPI With Custom Thread Count**

```java
import io.laniakia.core.BandcampAPI;
...
int threadPoolCount = 4;
BandcampAPI bandCampObj = new BandcampAPI(threadPoolCount);
```
**Band Search Options**

```java
import io.laniakia.core.BandcampAPI;
...
int threadPoolCount = 4;
boolean useThreads = true;
boolean searchMultiplePages = true
BandcampAPI bandCampObj = new BandcampAPI(threadPoolCount);
bandCampObj.searchBand("parallels", searchMultiplePages, useThreads);
```

**Search Bands With Asynchronous Requests**

```java
import io.laniakia.core.BandcampAPI;
...
BandcampAPI bandCampObj = new BandcampAPI();
bandCampObj.searchBand("parallels", true, true);
```

**Search Only First Page Of Results**

```java
import io.laniakia.core.BandcampAPI;
...
BandcampAPI bandCampObj = new BandcampAPI();
bandCampObj.searchBand("parallels", false, true);
```

**Other API Calls**

```java
import io.laniakia.core.BandcampAPI;
...
BandcampAPI bandCampObj = new BandcampAPI();
bandCampObj.searchBand("parallels", false, true);
//Query specific band
bandCampObj.getBand("https://parallels.bandcamp.com/");
//Query album by URL
bandCampObj.getAlbum("https://parallels.bandcamp.com/album/visionaries");
//Query by band and album
bandCampObj.getAlbum("Parallels", "Visionaries");
```

## Install from Source

Clone from remote repository then `mvn install`. All of the modules will be installed to your local maven repository.

~~~bash
git clone https://github.com/scriptkittie/bandcamp-api.git
cd bandcamp-api
mvn install
~~~

## Issues/Forks
Please report any issues to the issues section & as always if you have any functionality requests go ahead and open an issue containing your suggestions.

If you have an addition to the project, fork it and submit a pull request. Any type of contributions are welcome.

## Credits
Package written by [StCypher](https://twitter.com/yo_scriptkittie/with_replies)


API calls made to [Bandcamp](https://bandcamp.com/)
