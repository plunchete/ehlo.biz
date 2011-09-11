package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class AuthController extends Controller{
	@Before
	public static void checkSecret() {
		if (!session.contains("token") || session.get("token").isEmpty()) {
			Application.index();
		}
	}
 
}
