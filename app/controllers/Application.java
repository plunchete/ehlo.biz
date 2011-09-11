package controllers;

import play.Logger;
import play.mvc.Controller;
import siena.Json;
import utils.BigBrotherHelper;

public class Application extends Controller {

    public static void index() {
    	session.clear();
        render();
    }

    public static void doLogin(String latitude, String longitude) {
    	//TODO check we receive latitude and longitude
    	session.put("latitude", latitude);
    	session.put("longitude", longitude);
    	FourSquareController.austh4SQ();
    }
    
//    public static void renderSpinnerAndGetVenues() {
//    	render("application/prev-venues-list.html");
//    }
    
    public static void renderVenues() {
    	String coordinates = session.get("latitude") + "," + session.get("longitude");
    	Json venues = BigBrotherHelper.getVenues(coordinates, session.get("token"));
    	render("application/venues-list.html", venues);
    }
    
    public static void callBack4SQAuth(){
		String code = params.get("code");
		String token = BigBrotherHelper.retrieveToken(code);
		session.put("token", token);
		Application.renderVenues();
	}
	
	public static void showUser() {
		render("application/user-view.html");
	}
    
    public static void renderUsers(String venueId) {
    	Logger.info("v->" + venueId);
    	Json peopleHere = BigBrotherHelper.queryVenue(venueId, session.get("token"));
    	render("application/people-list.html", peopleHere);
    }
    
}