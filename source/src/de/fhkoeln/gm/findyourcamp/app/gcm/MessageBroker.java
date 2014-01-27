package de.fhkoeln.gm.findyourcamp.app.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

import de.fhkoeln.gm.findyourcamp.app.ResponseRentalPropertyActivity;
import de.fhkoeln.gm.findyourcamp.app.ShareRentalPropertyActivity;
import de.fhkoeln.gm.findyourcamp.app.gson.JsonDatatypes;
import de.fhkoeln.gm.findyourcamp.app.matching.RentalPropertyMatch;
import de.fhkoeln.gm.findyourcamp.app.model.RentalProperty;
import de.fhkoeln.gm.findyourcamp.app.notification.UserNotification;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

/**
 * MessageBroker zur Weiterleitung der Nachricht an geeignete Funktion
 *
 */
public class MessageBroker {

	private JsonDatatypes data;
	private Context appContext;

	public MessageBroker( Bundle data, Context context ) {
		this.appContext = context;

		String json = data.getString( "json" );

		// Ist json Feld vorhanden?
		if ( null == json ) {
			return;
		}

		Gson gson = new Gson();
		this.data = gson.fromJson( json, JsonDatatypes.class );
	}

	/**
	 * Auswahl der Aktion
	 */
	public void handleRequest() {
		int action = data.getAction();

		switch ( action ) {
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
			// Match-Anfrage
			case MessageConstants.ACTION_MATCH_REQUEST:
				handleMatchRequest();
				break;
			default:
				Logger.error( "Unsupported action: " + action );
				break;
		}

	}

	private void handleMatchRequest() {
		UserNotification userNotification = new UserNotification( appContext );
		userNotification.show( "Matchvorgang läuft", "", "Matchvorgang läuft", null, null );

		int matchResult = RentalPropertyMatch.getMatchResult( data, appContext );

		if ( matchResult < RentalPropertyMatch.MIN_MATCH_RATE ) {
			return;
		}

		Bundle notificationData = new Bundle();
		notificationData.putInt( "match_rate", matchResult );
		notificationData.putInt( "rent_user_id", data.getRentUserId() );

		userNotification.show( "Matchvorgang erfolgreich", "Mietobjekt freigeben?",
			"Mietobjekt freigeben", notificationData, ShareRentalPropertyActivity.class );
	}

	private void handleSearchResult() {
		int foundItems = data.getFoundItems();
		int hostUserId = data.getHostUserId();
		UserNotification userNotification = new UserNotification( appContext );

		if ( hostUserId > 0 ) {
			userNotification.show( "Suchanfrage erfolgreich", "Es konnte ein Mietobjekt gefunden werden.",
				"Camp gefunden", null, ResponseRentalPropertyActivity.class );
		} else {
			userNotification.show( "Suchanfrage ohne Ergebnisse", "Es konnte kein Mietobjekt gefunden werden.",
				"Kein Camp gefunden", null, ResponseRentalPropertyActivity.class );
		}
	}

	private void handleUserRegistered() {
		int userId = data.getUserId();
		UserNotification userNotification = new UserNotification( appContext );

		PreferencesStorage preferences = new PreferencesStorage( this.appContext );
		SharedPreferences.Editor preferencesEditor = preferences.getEditor();
		preferencesEditor.putInt( "user_id", userId );
		preferencesEditor.commit();

		if ( userId > 0 ) {
			userNotification.show( "Registrierung erfolgreich", "Erfolgreich registriert.",
				"Registrierung erfolgreich", null, null );
		} else {
			userNotification.show( "Registrierung fehlerhaft", "Registrierung konnte nicht abgeschlossen werden.",
				"Registrierung fehlerhaft", null, null );
		}
	}

	private void handleRentalPropertyRegistered() {
		int rentalPropertyRemoteId = data.getRentalPropertyRemoteId();

		// TODO ID speichern.

		UserNotification userNotification = new UserNotification( appContext );
		if ( rentalPropertyRemoteId > 0 ) {
			userNotification.show( "Mietobjekt registriert", "Erfolgreich registriert.", "Mietobjekt registriert",
				null, null );
		} else {
			userNotification.show( "Mietobjekt fehlerhaft", "Registrierung konnte nicht abgeschlossen werden.",
				"Mietobjekt fehlerhaft", null, null );
		}
	}
}
