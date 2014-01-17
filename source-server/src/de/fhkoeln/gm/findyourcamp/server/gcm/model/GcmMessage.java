package de.fhkoeln.gm.findyourcamp.server.gcm.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONValue;

public class GcmMessage {

	private String to = null;
	private String messageId = null;
	private String collapseKey = null;
	private Boolean delayWhileIdle = false;
	private Integer timeToLive = null;

	private HashMap<String, Object> payload = new HashMap<String, Object>();

	public GcmMessage() {
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

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
	 * @return the collapse_key
	 */
	public String getCollapseKey() {
		return collapseKey;
	}

	/**
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
		if ( to.isEmpty() || messageId.isEmpty() || payload.isEmpty() ) {
			return false;
		}

		return true;
	}

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
		message.put("data", payload);

		return JSONValue.toJSONString(message);
	}
}
