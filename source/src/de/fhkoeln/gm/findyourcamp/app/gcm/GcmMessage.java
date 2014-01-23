package de.fhkoeln.gm.findyourcamp.app.gcm;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.google.gson.Gson;

public class GcmMessage {
	private String messageId = null;
	private HashMap<String, Object> payload = new HashMap<String, Object>();
	private int action = 0;

	/**
	 * MessageID ausgeben
	 * 
	 * @return the messageId to get
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Neue MessageID setzen
	 * 
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId( String messageId ) {
		this.messageId = messageId;
	}

	/**
	 * Neuen Payload ausgeben
	 * 
	 * @return the payload to get
	 */
	public Map<String, Object> getPayload() {
		return payload;
	}

	/**
	 * Neuen Payload setzen
	 * 
	 * @param payload2
	 *            the payload to set
	 */
	public void setPayload( HashMap<String, Object> payload ) {
		this.payload = payload;
	}

	/**
	 * Neue Action ausgeben
	 * 
	 * @return the action to get
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Neue Aktion setzen
	 * 
	 * @param action
	 *            the action to set
	 */
	public void setAction( int action ) {
		this.action = action;
	}

	/**
	 * Gibt die Daten für die Nachricht zurück. Setzt sich zusammen aus der
	 * Aktion und dem Payload. Der Payload wird im Key 'json' als JSON Objekt
	 * gespeichert.
	 * 
	 * Damit wird das Problem umgangen, dass nicht-String Werte verloren gehen.
	 * 
	 * @return data
	 */
	public Bundle getData() {
		payload.put( "action", action );

		Gson gson = new Gson(); // Gson because it handles deep encoding better.
		String json = gson.toJson( payload );

		Bundle data = new Bundle();
		data.putString( "json", json );

		return data;
	}

	public boolean isValid() {
		if ( this.payload.isEmpty() || this.messageId.isEmpty() || this.action == 0 ) {
			return false;
		}

		return true;
	}
}
