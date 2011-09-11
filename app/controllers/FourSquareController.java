package controllers;


import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Redirect;
import utils.FourSquareHelper;

public class FourSquareController extends Controller {
	private static String CLIENT_ID="3HEIFZIGIX0WUJCJWPDZP1QPGQUIVVIOLNZ4ASRBYCUO3XN4";


	public static void callBack4SQAuth(){
		String code = params.get("code");
		String token = FourSquareHelper.retrieveToken(code);
		System.out.println(token);
		Application.index();
		//4square login
		}
	
	public static void austh4SQ(){
		String redirect = Router.getFullUrl("controllers.Listener.callBack4SQAuth");
		String url = "https://foursquare.com/oauth2/authenticate?client_id="+CLIENT_ID+
											"&response_type=code&redirect_uri="+redirect;	
		throw new Redirect(url);
	}
}
