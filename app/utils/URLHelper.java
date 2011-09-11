package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

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
	
	public static TagNode fetchUrl(String url, Map<String, String> args) throws IOException {
		HttpClient client = new HttpClient();
		client.getParams().setParameter("user-agent", "hackdisrupt app");
		
		PostMethod post = new PostMethod(url);
		for (Entry<String, String> entry : args.entrySet()) {
			post.addParameter(new NameValuePair(entry.getKey(), entry.getValue()));
		}

		try{
			int returnCode = client.executeMethod(post);
			if (returnCode == 200) {
				String charset = "UTF-8";
				InputStream in = post.getResponseBodyAsStream();
				HtmlCleaner cleaner = new HtmlCleaner();
				TagNode tag = cleaner.clean(in, charset);
				return tag;
			}else if (returnCode == 404 || returnCode == 302){
				throw new FetchUrlException("Link Doesn't exists or moved", returnCode);	
			}
		}catch (Exception e ){
			e.printStackTrace();
		}
			
		return null;
	}
}
