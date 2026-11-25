package io.laniakia.util;

import java.io.IOException;

/** Raised when Bandcamp returns a client challenge instead of the requested page. */
public class BandcampAccessChallengeException extends IOException {
	private static final long serialVersionUID = 1L;

	public BandcampAccessChallengeException(String url) {
		super("Bandcamp returned a client challenge for " + url);
	}
}
