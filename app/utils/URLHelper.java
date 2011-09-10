package utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import siena.Json;
import exceptions.FetchUrlException;

public class URLHelper {
	public static Json fetchJson(String url) throws Exception {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		try{
		conn.setRequestProperty("user-agent", "awesome mobile app");
		if (conn.getResponseCode() == 200) {
			InputStream in = conn.getInputStream();
			byte[] bytes = IOUtils.toByteArray(in);
			Json b =  Json.loads(new String(bytes, "UTF-8"));
			return b;
		}
		
		if (conn.getResponseCode() >=500) {
			throw new FetchUrlException("banned or downtime", conn.getResponseCode());
		}
		if (conn.getResponseCode() == 404) {
			throw new FetchUrlException("404", conn.getResponseCode());
		}
		if (conn.getResponseCode() != 200) {
			throw new Exception("GitHub code was: "+conn.getResponseCode()+" while fetching URL: "+url);
		}
		return null;
		}finally{
		}
	}
}
