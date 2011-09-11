package utils;

import play.mvc.Router;
import siena.Json;
import controllers.Constants;

public class FourSquareHelper {
	private static String CLIENT_ID="3HEIFZIGIX0WUJCJWPDZP1QPGQUIVVIOLNZ4ASRBYCUO3XN4";
	private static String CLIENT_SECRET="UZ5ATEJSTJNPK2LKV0ZY11XHFV45YWYJKUHRFKGLUCX4ID4O";
	private static String END_POINTS_URL="https://api.foursquare.com/v2/venues/search?v=20110910&oauth_token=";
	private static String USER_DETAILS= " https://foursquare.com/users/";
	private static String VENUE_DETAILS= "https://api.foursquare.com/v2/venues/";

	
	
	private static Json processEndPointSearchJson(Json raw){
		Json venues = raw.get("response").get("venues");
		Json processedVenues = Json.list();
		for (Json json : venues) {
			Json processedVenue = Json.map();
			processedVenue.put("id", json.get("id"))
							.put("name", json.get("name"))
							.put("address", json.get("location").get("address"))
							.put("categories", json.get("categories").get("parents"))
							.put("image", json.get("categories").get("icon"))
							.put("count", json.get("hereNow").get("count"));
			processedVenues.add(processedVenue);
		}
		return processedVenues;
	}
	
	public static Json consume4SQCoordinates(String coordinates){
		String url = END_POINTS_URL+Constants.API_TOKEN_4SQ+"&ll="+coordinates;
		try {
			Json results=URLHelper.fetchJson(url);
			return(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String retrieveToken(String code){
		String uriCB = Router.getFullUrl("controllers.Application.index");
		String url = "https://foursquare.com/oauth2/access_token"
			  			+"?client_id="+CLIENT_ID
			  			+"&client_secret="+CLIENT_SECRET
			  			+"&grant_type=authorization_code"
			  			+"&redirect_uri="+uriCB
			  			+"&code="+code;
		try {
			Json token = URLHelper.fetchJson(url);
			return token.get("access_token").str();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Json getVenues(String coordinates){
		Json rawVenues = consume4SQCoordinates(coordinates);
		Json venues = processEndPointSearchJson(rawVenues);
		return venues;
	}
	
	private static Json queryByUserId(String uid){
		Json data = Json.map();
		String url = USER_DETAILS+uid+"?oauth_token="+Constants.API_TOKEN_4SQ;
		try {
			Json userData = URLHelper.fetchJson(url);
			data = userData.get("response").get("user").get("contact");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static Json queryVenue(String venueId){
		String url = VENUE_DETAILS+venueId+"?oauth_token"+Constants.API_TOKEN_4SQ;
		Json hereNow = Json.map();
		try {
			Json details = URLHelper.fetchJson(url).get("hereNow");
			hereNow.put("count", details.get("count"));
			hereNow.put("people", Json.list());
			Json people = Json.list();
			for(Json json : details.get("groups")){
				String type = json.get("type").str();
				Json items = json.get("items");
				if(items.isEmpty() || items.isNull()) continue;
				for(Json json2 : items){
 					Json item = json.map();
					item.put("type", type);
					item.put("firstName", json2.get("user").get("firstName")); 
					item.put("lastName", json2.get("user").get("lastName"));
					item.put("photo", json2.get("user").get("photo")); 
					String uid=json2.get("user").get("id").str();
					item.put("id", uid);
					item.put("contact", queryByUserId(uid));
					item.put("bio", TwitterScrapper.getTwitterInfo(item.get("contact")));
					people.add(item);
				}
			}
			hereNow.put("people", people);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hereNow;
	}
	
}
