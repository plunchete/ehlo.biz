package utils;

import java.util.Arrays;
import java.util.List;

import siena.Json;

public class QwerlyHelper {

	public static final List<String> supportedServices = Arrays.asList("facebook", "twitter", "github", "klout", "linkedin", "twitter", "quora");
	
	public static Json getUserServices(Json json) {
		String twitterId = (json.containsKey("twitter"))? json.get("twitter").str():"";
		String facebookId = (json.containsKey("facebook"))? json.get("facebook").str():"";
		Json allServices = Json.map();
		
		if(!twitterId.isEmpty()){
			try {
				Json info = URLHelper.fetchJson(twitterUrl(twitterId));
				String status = info.get("status").str();
				if ("200".equals(status)){
					Json services = info.get("profile").get("services");
					for (Json json2 : services) {
						String keyService = json2.get("type").str();
						if (supportedServices.contains(keyService)) {
							allServices.put(keyService, 
									Json.map().put("url", json2.get("url")).put("username", json2.get("username")));
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!facebookId.isEmpty()){
			try {
				Json info = URLHelper.fetchJson(facebookUrl(facebookId));
				String status = info.get("status").str();
				if ("200".equals(status)){
					Json services = info.get("profile").get("services");
					for (Json json2 : services) {
						String keyService = json2.get("type").str();
						if (supportedServices.contains(keyService)) {
							allServices.put(keyService, 
									Json.map().put("url", json2.get("url")).put("username", json2.get("username")));
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return allServices;
	}
	
	private static String twitterUrl(String twitter){
		return "http://api.qwerly.com/v1/twitter/"+twitter+"?api_key=d2f29ccdma3eh3cmsnyp6bwa";
	}
	
	private static String facebookUrl(String facebook){
		return "http://api.qwerly.com/v1/facebook/"+facebook+"?api_key=d2f29ccdma3eh3cmsnyp6bwa";
	}

}
