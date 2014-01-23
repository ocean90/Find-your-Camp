package de.fhkoeln.gm.findyourcamp.server.gcm.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Aktionen im Umgang mit GcmMessage.
 * Ausgabe und Setzen der Werte sowie Umwandeln in JSON
 */
public class GcmMessage {

	private String to = null;
	private String messageId = null;
	private String collapseKey = null;
	private Boolean delayWhileIdle = false;
	private Integer timeToLive = null;
	private int action = 0;

	private HashMap<String, Object> payload = new HashMap<String, Object>();

	public GcmMessage() {
	}

	/**
	 * Empfaenger ausgeben
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Empfaenger setzen
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * MessageId ausgaben
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * MessageIs setzen
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * CollapseKey ausgeben
	 * @return the collapse_key
	 */
	public String getCollapseKey() {
		return collapseKey;
	}

	/**
	 * CollapseKey setzen
	 * @param collapse_key the collapse_key to set
	 */
	public void setCollapseKey(String collapseKey) {
		this.collapseKey = collapseKey;
	}

	/**
	 * @return the delay_while_idle
	 */
	public Boolean getDelayWhileIdle() {
		return delayWhileIdle;
	}

	/**
	 * @param delay_while_idle the delay_while_idle to set
	 */
	public void setDelayWhileIdle(Boolean delayWhileIdle) {
		this.delayWhileIdle = delayWhileIdle;
	}

	/**
	 * @return the time_to_live
	 */
	public Integer getTimeToLive() {
		return timeToLive;
	}

	/**
	 * @param time_to_live the time_to_live to set
	 */
	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
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
	 * Payload der Message ausgeben
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

	public boolean isValid() {
		if ( action == 0 || to.isEmpty() || messageId.isEmpty() || payload.isEmpty() ) {
			return false;
		}

		return true;
	}

	/**
	 * Message in JSON umwandeln
	 * @return
	 */
	public String toJson() {
		Map<String, Object> message = new HashMap<String, Object>();

		message.put("to", to);

		if (collapseKey != null) {
		  message.put("collapse_key", collapseKey);
		}

		if (timeToLive != null) {
		  message.put("time_to_live", timeToLive);
		}

		if (delayWhileIdle != null && delayWhileIdle) {
		  message.put("delay_while_idle", true);
		}

		message.put("message_id", messageId);

		payload.put("action", action);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("json", payload);

		message.put("data", data);

		Gson gson = new Gson();

		return gson.toJson(message);
	}
}
