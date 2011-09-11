package controllers;


import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Redirect;
import siena.Json;
import utils.URLHelper;
import controllers.Application;

public class FourSquareController extends Controller {
	private String endPointsUriBase="https://api.foursquare.com/v2/venues/5104?oauth_token=";
	private static String CLIENT_ID="3HEIFZIGIX0WUJCJWPDZP1QPGQUIVVIOLNZ4ASRBYCUO3XN4";
	private static String CLIENT_SECRET="UZ5ATEJSTJNPK2LKV0ZY11XHFV45YWYJKUHRFKGLUCX4ID4O";
	private static String END_POINTS_URL="https://api.foursquare.com/v2/venues/search?v=20110910&oauth_token=";
	private static String VENUE_DETAILS= "https://api.foursquare.com/v2/venues/";


	public static Json queryVenue(String venueId){
		String url = VENUE_DETAILS+venueId+"?oauth_token"+Constants.API_TOKEN_4SQ;
		Json hereNow = Json.map();
		try {
			Json details = URLHelper.fetchJson(url).get("hereNow");
			hereNow.put("count", details.get("count"));
			hereNow.put("people", Json.list());
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
					item.put("id", json2.get("user").get("id")); 
					hereNow.get("people").add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hereNow;
	}
	
	public static Json getVenues(String coordinates){
		Json rawVenues = consume4SQCoordinates(coordinates);
		Json venues = processEndPointSearchJson(rawVenues);
		return venues;
	}

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
	
	private static Json consume4SQCoordinates(String coordinates){
		String url = END_POINTS_URL+Constants.API_TOKEN_4SQ+"&ll="+coordinates;
		try {
			Json results=URLHelper.fetchJson(url);
			return(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void callBack4SQAuth(){
		String code = params.get("code");
		String token = retrieveToken(code);
		System.out.println(token);
		Application.index();
		//4square login
		}
	
	private static String retrieveToken(String code){
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
	
	public static void auth4SQ(){
		String redirect = Router.getFullUrl("controllers.Listener.callBack4SQAuth");
		String url = "https://foursquare.com/oauth2/authenticate?client_id="+CLIENT_ID+
											"&response_type=code&redirect_uri="+redirect;	
		throw new Redirect(url);
	}
}
