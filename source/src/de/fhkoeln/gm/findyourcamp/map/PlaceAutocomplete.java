package de.fhkoeln.gm.findyourcamp.map;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhkoeln.gm.findyourcamp.utils.Logger;

/**
 * Places API https://developers.google.com/places/documentation/autocomplete
 *
 */
public final class PlaceAutocomplete {

	final static String API_KEY = "AIzaSyAfBe9q55CiWwm6l31fhuJYpH_d6zkQxFI";
	final static String API_ENDPOINT = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

	public static ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(API_ENDPOINT);
	        sb.append("?sensor=false&key=" + API_KEY);
	        sb.append("&components=country:de");
	        sb.append("&types=(cities)");
	        sb.append("&language=de");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Logger.error("Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	    	Logger.error( "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            String value = predsJsonArray.getJSONObject(i).getString("description");
	            Logger.info(value);
	        	resultList.add( value);
	        }
	    } catch (JSONException e) {
	        Logger.error("Cannot process JSON results", e);
	    }

	    return resultList;
	}
}
