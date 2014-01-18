package de.fhkoeln.gm.findyourcamp.app.gcm;

import android.content.Context;
import android.os.Bundle;
import de.fhkoeln.gm.findyourcamp.app.notification.UserNotification;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;

/**
 * MessageBroker zur Weiterleitung der Nachricht an geeignete Funktion
 *
 */
public class MessageBroker {

	private Bundle data;
	private Context appContext;

	public MessageBroker(Bundle data, Context context) {
		this.data = data;
		this.appContext = context;
		Logger.info(data.toString());
	}

	/**
	 * Auswahl derAktion
	 */
	public void handleRequest() {
		int action = Integer.valueOf(data.getString("action"));

		switch (action) {
		// Registrierung
		case MessageConstants.ACTION_USER_REGISTERED:
			Logger.info("Action: Register");
			handleUserRegistration();
			break;
		// Suchanfrage
		case MessageConstants.ACTION_SEARCH_RESULT:
			Logger.info("Action: Search Request");
			handleSearchResult();
			break;
		default:
			Logger.error("Unsupported action: " + action);
			break;
		}

	}

	private void handleSearchResult() {
		int found_items = Integer.valueOf(data.getString("found_items"));
		UserNotification userNotification = new UserNotification(appContext);
		if (found_items > 0) {
			userNotification.show("Suchanfrage erfolgreich", "Es konnte ein Mietobjekt gefunden werden.", "Camp gefunden");
		} else {
			userNotification.show("Suchanfrage ohne Ergebnise", "Es konnte kein Mietobjekt gefunden werden.", "Kein Camp gefunden");
		}
	}

	private void handleUserRegistration() {
		int user_id = Integer.valueOf(data.getString("user_id"));
		UserNotification userNotification = new UserNotification(appContext);
		if (user_id > 0) {
			userNotification.show("Registrierung erfolgreich", "Erfolgreich registriert.", "Registrierung erfolgreich");
		} else {
			userNotification.show("Registrierung fehlerhaft", "Registrierung konnte nicht abgeschlossen werden.", "Registrierung fehlerhaft");
		}
	}
}
