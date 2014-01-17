package de.fhkoeln.gm.findyourcamp.app.gcm;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;

public class GcmMessage {
	private String messageId = null;
	private HashMap<String, Object> payload = new HashMap<String, Object>();
	private int action = 0;

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the payload
	 */
	public Map<String, Object> getPayload() {
		return payload;
	}

	/**
	 * @param payload2 the payload to set
	 */
	public void setPayload(HashMap<String, Object> payload) {
		this.payload = payload;
	}

	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * Gibt die Daten für die Nachricht zurück.
	 * Setzt sich zusammen aus der Aktion und dem Payload.
	 * Der Payload wird im Key 'json' als JSON Objekt gespeichert.
	 *
	 * Damit wird das Problem umgangen, dass nicht-String Werte verloren gehen.
	 *
	 * @return
	 */
	public Bundle getData() {
		payload.put("action", action);
		JSONObject json = new JSONObject(payload);

		Bundle data = new Bundle();
		data.putString("json", json.toString());

		return data;
	}

	public boolean isValid() {
		if (this.payload.isEmpty() || this.messageId.isEmpty() || this.action == 0) {
			return false;
		}

		return true;
	}
}
