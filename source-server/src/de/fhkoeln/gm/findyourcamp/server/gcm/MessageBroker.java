package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import de.fhkoeln.gm.findyourcamp.server.gcm.model.GcmMessage;
import de.fhkoeln.gm.findyourcamp.server.matching.LocationMatch;
import de.fhkoeln.gm.findyourcamp.server.model.Device;
import de.fhkoeln.gm.findyourcamp.server.model.User;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

/**
 * Messagebroker leitet eingehende Nachricht an Aktion zur Registrierung oder Suchanfrage weiter.
 *
 */
public class MessageBroker {

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
			case MessageConstants.ACTION_USER_REGISTRATION:
				// Registrierung
				handleUserRegistration();
				break;
			case MessageConstants.ACTION_SEARCH_REQUEST:
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

			GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();

			GcmMessage message = new GcmMessage();
			message.setTo(this.from);
			message.setMessageId("m-" + (System.currentTimeMillis() / 1000L));
			message.setAction(MessageConstants.ACTION_USER_REGISTERED);

			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put("user_id", newUserId);
			message.setPayload(payload);

			gcmConnection.sendMessage(message);
		}

	}

	public void handleSearchRequest() {
		System.out.println("Neue Suchanfrage...");
		String location = (String) data.get("location");
		Logger.log(location);

		// ToDo
		List<String> devices = LocationMatch.getMatches(location);

		GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();
		GcmMessage message = new GcmMessage();
		message.setTo(this.from);
		message.setMessageId("m-" + (System.currentTimeMillis() / 1000L));
		message.setAction(MessageConstants.ACTION_SEARCH_RESULT);
		HashMap<String, Object> payload = new HashMap<String, Object>();

		if (!devices.isEmpty()) {
			payload.put("found_items", devices.size());
		} else {
			payload.put("found_items", 0);
		}

		message.setPayload(payload);
		gcmConnection.sendMessage(message);
	}
}
