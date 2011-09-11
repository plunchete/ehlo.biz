package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class AuthController extends Controller{
	@Before
	public static void checkSecret() {
		if (Constants.API_TOKEN_4SQ == null || Constants.API_TOKEN_4SQ.isEmpty()) {
			Application.index();
		}
	}
 
}
