package io.laniakia.util;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/** Shared HTTP helpers for Bandcamp pages and JSON endpoints. */
public final class HttpUtil {
	public static final String SLASH = "/";
	private static final int TIMEOUT_MILLIS = 15000;
	private static final int MAX_ATTEMPTS = 2;
	private static final long MIN_REQUEST_INTERVAL_MILLIS = 2000;
	private static final long DEFAULT_RETRY_DELAY_MILLIS = 10000;
	private static final long MAX_RETRY_DELAY_MILLIS = 60000;
	private static final Object REQUEST_SCHEDULER = new Object();
	private static long nextRequestTimeMillis;
	private static final Semaphore REQUEST_PERMIT = new Semaphore(1, true);
	private static final String USER_AGENT = "bandcamp-api/1.1 (Java; +https://github.com/scriptkittie/bandcamp-api)";

	private HttpUtil() {
	}

	/** Fetch a page with bounded timeouts and retries for transient I/O failures. */
	public static Document getDocument(String url) throws Exception {
		if (url == null || url.trim().isEmpty()) {
			throw new IllegalArgumentException("URL must not be blank");
		}

		Connection.Response response = execute(connection(url));
		Document document = response.parse();
		if ("Client Challenge".equalsIgnoreCase(document.title().trim())) {
			throw new BandcampAccessChallengeException(response.url().toString());
		}
		return document;
	}

	/** Perform a GET request and return its response body. */
	public static String httpGet(String host, Map<String, String> parameters) throws Exception {
		Connection request = connection(host).ignoreContentType(true);
		if (parameters != null) {
			for (Map.Entry<String, String> parameter : parameters.entrySet()) {
				request.data(parameter.getKey(), parameter.getValue());
			}
		}
		Connection.Response response = execute(request);
		String body = response.body();
		if (body.matches("(?is).*<title[^>]*>\\s*Client Challenge\\s*</title>.*")) {
			throw new BandcampAccessChallengeException(response.url().toString());
		}
		return body;
	}

	private static Connection.Response execute(Connection request) throws Exception {
		IOException lastFailure = null;
		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			Connection.Response response;
			try {
				response = executeAttempt(request);
			} catch (IOException e) {
				lastFailure = e;
				if (attempt == MAX_ATTEMPTS) {
					break;
				}
				pause(DEFAULT_RETRY_DELAY_MILLIS);
				continue;
			}

			int status = response.statusCode();
			if (status < 400) {
				return response;
			}
			IOException failure = new IOException("HTTP " + status + " fetching " + response.url());
			if (attempt == MAX_ATTEMPTS || (status < 500 && status != 429)) {
				throw failure;
			}
			lastFailure = failure;
			long retryDelay = retryDelayMillis(response.header("Retry-After"), status);
			if (retryDelay < 0) {
				throw failure;
			}
			pause(retryDelay);
		}
		throw new IOException("Unable to fetch Bandcamp page after " + MAX_ATTEMPTS + " attempts: " + request.request().url(), lastFailure);
	}

	private static long retryDelayMillis(String retryAfter, int status) {
		if (retryAfter != null) {
			try {
				long seconds = Long.parseLong(retryAfter.trim());
				if (seconds <= 0) {
					return 0;
				}
				return seconds > MAX_RETRY_DELAY_MILLIS / 1000
						? -1 : seconds * 1000;
			} catch (NumberFormatException ignored) {
				try {
					Instant retryAt = ZonedDateTime.parse(retryAfter, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
					long waitMillis = Duration.between(Instant.now(), retryAt).toMillis();
					if (waitMillis > MAX_RETRY_DELAY_MILLIS) {
						return -1;
					}
					return Math.max(0, waitMillis);
				} catch (RuntimeException invalidDate) {
					// Fall back to a conservative delay for malformed Retry-After values.
				}
			}
		}
		return status == 429 ? 30000 : DEFAULT_RETRY_DELAY_MILLIS;
	}

	private static Connection.Response executeAttempt(Connection request) throws Exception {
		try {
			REQUEST_PERMIT.acquire();
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
			throw interrupted;
		}
		try {
			long delayMillis;
			synchronized (REQUEST_SCHEDULER) {
				delayMillis = Math.max(0, nextRequestTimeMillis - System.currentTimeMillis());
			}
			if (delayMillis > 0) {
				pause(delayMillis);
			}
			try {
				return request.ignoreHttpErrors(true).execute();
			} finally {
				synchronized (REQUEST_SCHEDULER) {
					nextRequestTimeMillis = System.currentTimeMillis() + MIN_REQUEST_INTERVAL_MILLIS;
				}
			}
		} finally {
			REQUEST_PERMIT.release();
		}
	}

	private static void pause(long delayMillis) throws InterruptedException {
		try {
			Thread.sleep(delayMillis);
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
			throw interrupted;
		}
	}

	private static Connection connection(String url) {
		return Jsoup.connect(url)
				.userAgent(USER_AGENT)
				.timeout(TIMEOUT_MILLIS)
				.followRedirects(true);
	}

	public static String addPaths(String p1, String p2) {
		if (p1 == null || p1.length() == 0) {
			if (p1 != null && p2 == null)
				return p1;
			return p2;
		}
		if (p2 == null || p2.length() == 0)
			return p1;

		int split = p1.indexOf(';');
		if (split < 0)
			split = p1.indexOf('?');
		if (split == 0)
			return p2 + p1;
		if (split < 0)
			split = p1.length();

		StringBuilder buf = new StringBuilder(p1.length() + p2.length() + 2);
		buf.append(p1);

		if (buf.charAt(split - 1) == '/') {
			if (p2.startsWith(SLASH)) {
				buf.deleteCharAt(split - 1);
				buf.insert(split - 1, p2);
			} else
				buf.insert(split, p2);
		} else {
			if (p2.startsWith(SLASH))
				buf.insert(split, p2);
			else {
				buf.insert(split, '/');
				buf.insert(split + 1, p2);
			}
		}

		return buf.toString();
	}

	public static Elements getScriptList(String source) throws Exception {
		Document doc = getDocument(source);
		return doc.select("script[type=text/javascript]");
	}
}
