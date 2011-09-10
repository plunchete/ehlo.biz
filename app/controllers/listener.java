package controllers;


import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Redirect;
import siena.Json;
import utils.URLHelper;
import controllers.Application;
public class Listener extends Controller {
	private String endPointsUriBase="https://api.foursquare.com/v2/venues/5104?oauth_token=";
	private static String CLIENT_ID="3HEIFZIGIX0WUJCJWPDZP1QPGQUIVVIOLNZ4ASRBYCUO3XN4";
	private static String CLIENT_SECRET="UZ5ATEJSTJNPK2LKV0ZY11XHFV45YWYJKUHRFKGLUCX4ID4O";
	
	public static void consume4SQCoordinates(String latitude, String longitude){
		
	}

	public static void callBack4SQAuth(){
		String code = params.get("code");
		String token = retrieveToken(code);
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
}
