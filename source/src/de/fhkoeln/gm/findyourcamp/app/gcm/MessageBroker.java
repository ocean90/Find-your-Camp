package de.fhkoeln.gm.findyourcamp.app.gcm;

import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import android.os.Bundle;

/**
 * MessageBroker zur Weiterleitung der Nachricht an geeignete Funktion
 *
 */
public class MessageBroker {


	Bundle data;

	public MessageBroker(Bundle data) {
		this.data = data;
	}

	/**
	 * Auswahl derAktion
	 */
	public void handleRequest() {
		int action = Integer.parseInt( data.getString("action") );

		switch (action) {
		// Registrierung
		case MessageConstants.ACTION_USER_REGISTRATION:
			Logger.info("Action: Register");
			break;
		// Suchanfrage	
		case MessageConstants.ACTION_SEARCH_REQUEST:
			Logger.info("Action: Search Request");
			break;
		default:
			Logger.error("Unsupported action: " + action);
			break;
		}

	}
}
