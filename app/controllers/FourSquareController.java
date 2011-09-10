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
	
	public static Json queryVenue(String venueId){
		
		return null;
	}
	
	private static void processEndPointSearchJson(Json raw){
		Json venues = raw.get("response").get("venues");
		for (Json json : venues) {
			
		}
	}
	
	public static Json consume4SQCoordinates(String ll){
		String url = END_POINTS_URL+Constants.API_TOKEN_4SQ+"&ll="+ll;
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
	
	public static Json getPlacesNearBy(String latitude, String longitue){
		//concourse exhibition centre for testing purposes
		//2237.7718939
		//-122.4039808
		
		
		return null;
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
			// TODO Auto-generated catch block
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
	
//	public static void main(String[] args) {
//		Constants.API_TOKEN_4SQ="1IKLWBG4MSEQBUZCKT2OFR4J5ZDFVW12SHVH0WBE3IW1T1NP";
//		consume4SQCoordinates("37.7718939,-122.4039808");
//	}
}
