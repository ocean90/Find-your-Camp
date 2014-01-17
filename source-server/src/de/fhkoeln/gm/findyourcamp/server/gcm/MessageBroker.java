package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import de.fhkoeln.gm.findyourcamp.server.model.Device;
import de.fhkoeln.gm.findyourcamp.server.model.User;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;


public class MessageBroker {

	public static final int ACTION_USER_REGISTRATION  = 1;
	public static final int ACTION_SEARCH_REQUEST = 2;

	Map<String, Object> data;
	private String from;

	@SuppressWarnings("unchecked")
	public MessageBroker(Map<String, Object> object) {
		Map<String, Object> rawData = (Map<String, Object>) object.get("data");

		String json = rawData.get("json").toString();
		try {
			this.data = (Map<String, Object>) JSONValue.parseWithException(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.from = object.get("from").toString();
	}

	public void handleRequest() {
		int action = ((Long) data.get("action")).intValue();

		switch (action) {
			case ACTION_USER_REGISTRATION:
				handleUserRegistration();
				break;
			case ACTION_SEARCH_REQUEST:
				handleSearchRequest();
				break;
			default:
				Logger.err("Unbekannte Aktion: " + action);
				break;
		}
	}

	public void handleUserRegistration() {
		System.out.println("Neue Registration...");
		String userName = (String) data.get("user_name");
		String userEmail = (String) data.get("user_email");
		String regId = this.from;

		int newUserId = User.createUser(userName, userEmail);

		if (newUserId > 0) {
			int newDeviceId = Device.assignDeviceToUser(regId, newUserId);
			System.out.println("Registration für User " + newUserId + " und Device " + newDeviceId + " abgeschlossen." );
		}

	}

	public void handleSearchRequest() {

	}
}
