package de.fhkoeln.gm.findyourcamp.app.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import de.fhkoeln.gm.findyourcamp.app.notification.UserNotification;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

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
		// User-Registrierung
		case MessageConstants.ACTION_USER_REGISTERED:
			handleUserRegistered();
			break;
		// Suchanfrage
		case MessageConstants.ACTION_SEARCH_RESULT:
			handleSearchResult();
			break;
		// Mietobjekt anlegen
		case MessageConstants.ACTION_RENTAL_PROPERTY_REGISTERED:
			handleRentalPropertyRegistered();
			break;
		default:
			Logger.error("Unsupported action: " + action);
			break;
		}

	}

	private void handleSearchResult() {
		int foundItems = Integer.valueOf(data.getString("found_items"));
		UserNotification userNotification = new UserNotification(appContext);
		if (foundItems > 0) {
			userNotification.show("Suchanfrage erfolgreich", "Es konnte ein Mietobjekt gefunden werden.", "Camp gefunden");
		} else {
			userNotification.show("Suchanfrage ohne Ergebnisse", "Es konnte kein Mietobjekt gefunden werden.", "Kein Camp gefunden");
		}
	}

	private void handleUserRegistered() {
		int userId = Integer.valueOf(data.getString("user_id"));
		UserNotification userNotification = new UserNotification(appContext);

		PreferencesStorage preferences = new PreferencesStorage(this.appContext);
		SharedPreferences.Editor preferencesEditor = preferences.getEditor();
		preferencesEditor.putInt("user_id", userId);
		preferencesEditor.commit();

		if (userId > 0) {
			userNotification.show("Registrierung erfolgreich", "Erfolgreich registriert.", "Registrierung erfolgreich");
		} else {
			userNotification.show("Registrierung fehlerhaft", "Registrierung konnte nicht abgeschlossen werden.", "Registrierung fehlerhaft");
		}
	}

	private void handleRentalPropertyRegistered() {
		int rentalPropertyRemoteId = Integer.valueOf(data.getString("rental_property_remote_id"));
		UserNotification userNotification = new UserNotification(appContext);
		if (rentalPropertyRemoteId > 0) {
			userNotification.show("Registrierung erfolgreich", "Erfolgreich registriert.", "Registrierung erfolgreich");
		} else {
			userNotification.show("Registrierung fehlerhaft", "Registrierung konnte nicht abgeschlossen werden.", "Registrierung fehlerhaft");
		}
	}
}
