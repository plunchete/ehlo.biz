package utils;

import java.io.IOException;
import java.util.HashMap;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import siena.Json;

public class TwitterScrapper {
	private static String BASE_URL="http://twitter.com/";
	
	public static String getTwitterInfo(Json userContacts){
		String bio="";
		if(userContacts!=null &&!userContacts.isNull() && !userContacts.isEmpty() && userContacts.contains("twitter")){
			String twitter = userContacts.get("twitter").str();
			bio = getUserBio(twitter);
		}
		return bio;
	}
	private static String getUserBio(String user){
		String url = BASE_URL+user;
		System.out.println(url);
		try {
			TagNode html = URLHelper.fetchUrl(url, new HashMap<String, String>());
			Object [] tags = html.evaluateXPath("//span[@class='bio']");
			TagNode bioTag = (TagNode)tags[0];
			String bio = bioTag.getText().toString();
			return bio;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) {
		System.out.println(getUserBio("jlbelmonte"));
	}
}
