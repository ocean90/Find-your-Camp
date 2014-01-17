package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import de.fhkoeln.gm.findyourcamp.server.matching.LocationMatch;
import de.fhkoeln.gm.findyourcamp.server.model.Device;
import de.fhkoeln.gm.findyourcamp.server.model.User;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

/**
 * Messagebroker leitet eingehende Nachricht an Aktion zur Registrierung oder Suchanfrage weiter.
 *
 */
public class MessageBroker {

	public static final int ACTION_USER_REGISTRATION  = 1;
	public static final int ACTION_SEARCH_REQUEST = 2;

	Map<String, Object> data;
	private String from;


	/**
	 * Uebergebenes Object wird in JSON Objekt zur weiteren Verarbeitung umgewandelt
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	public MessageBroker(Map<String, Object> object) {
		Map<String, Object> rawData = (Map<String, Object>) object.get("data");

		if (null == rawData.get("json")) {
			return;
		}

		String json = rawData.get("json").toString();
		try {
			this.data = (Map<String, Object>) JSONValue.parseWithException(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// regID des Senderdevice
		this.from = object.get("from").toString();
	}

	/**
	 * Anfrage wird nach gewuenschtem Wert an Aktion weitergeleitet
	 */
	public void handleRequest() {

		if (null == data || null == data.get("action")) {
			return;
		}

		int action = ((Long) data.get("action")).intValue();

		switch (action) {
			case ACTION_USER_REGISTRATION:
				// Registrierung
				handleUserRegistration();
				break;
			case ACTION_SEARCH_REQUEST:
				// Suchanfrage
				handleSearchRequest();
				break;
			default:
				Logger.err("Unbekannte Aktion: " + action);
				break;
		}
	}

	/**
	 * Registrierung
	 */
	public void handleUserRegistration() {
		System.out.println("Neue Registration...");
		String userName = (String) data.get("user_name");
		String userEmail = (String) data.get("user_email");

		//DeviceID
		String regId = this.from;

		// Neue UserID nach Anlegen erhalten
		int newUserId = User.createUser(userName, userEmail);

		// Zuordnung User mit Device ID
		if (newUserId > 0) {
			int newDeviceId = Device.assignDeviceToUser(regId, newUserId);
			System.out.println("Registration für User " + newUserId + " und Device " + newDeviceId + " abgeschlossen." );
		}

	}

	public void handleSearchRequest() {
		System.out.println("Neue Suchanfrage...");
		String location = (String) data.get("location");
		Logger.log(location);

		// ToDo
		List<Integer> users = LocationMatch.getMatches(location);
	}
}
