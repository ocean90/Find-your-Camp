package de.fhkoeln.gm.findyourcamp.app.gcm;

import android.os.Bundle;

public class GcmMessage {
	private String messageId = null;
	private Bundle payload = null;

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
	public Bundle getPayload() {
		return payload;
	}
	/**
	 * @param payload the payload to set
	 */
	public void setPayload(Bundle payload) {
		this.payload = payload;
	}

	public boolean isValid() {
		if (this.payload.isEmpty() || this.messageId.isEmpty()) {
			return false;
		}

		return true;
	}
}
