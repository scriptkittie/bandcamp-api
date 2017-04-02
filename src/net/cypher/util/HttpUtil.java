package net.cypher.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HttpUtil 
{
	public static final String SLASH = "/";
	
	public static Document getDocument(String url) throws Exception
	{
		int i = 0;
		while(i < 10)
		{
			try 
			{
				return Jsoup.connect(url).get();
			} 
			catch (Exception e) 
			{
				i++;
				Thread.sleep(2000);
			}
		}
		return Jsoup.connect(url).get();
	}
	
	public static String httpGet (String host, Map<String,String> parameters) throws Exception
	{
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();    
		clientBuilder.disableCookieManagement();
		CloseableHttpClient httpClient = clientBuilder.build();
		HttpGet httpGet = new HttpGet(host);	
		URIBuilder builder = new URIBuilder(httpGet.getURI());
		if(parameters != null && !parameters.isEmpty())
		{
			for (Map.Entry<String, String> entry : parameters.entrySet())
			{
			    String key = entry.getKey();
			    String value = entry.getValue();
			    builder.addParameter(key, value);
			}
		}
		
		((HttpRequestBase) httpGet).setURI(builder.build());
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String inputLine;
	    StringBuffer response = new StringBuffer();
	    
	    while ((inputLine = reader.readLine()) != null)
        {
            response.append(inputLine);
        }
	    
        reader.close();
        httpClient.close();  
        
        return response.toString();
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

		StringBuffer buf = new StringBuffer(p1.length() + p2.length() + 2);
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
	
	public static Elements getScriptList(String source) throws Exception
	{
		Document doc = HttpUtil.getDocument(source);
		return doc.select("script[type=text/javascript]"); 
	}
}
