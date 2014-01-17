package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.util.Map;


public class MessageBroker {

	public static final int ACTION_USER_REGISTRATION  = 1;
	public static final int ACTION_SEARCH_REQUEST = 2;

	Map<String, Object> data;

	public MessageBroker(Map<String, Object> data) {
		this.data = data;
	}

	public void handleRequest() {

		int action = (Integer) data.get("action");

		switch (action) {
			case ACTION_USER_REGISTRATION:
				break;
			case ACTION_SEARCH_REQUEST:
				break;
			default:
				break;
		}

	}
}
