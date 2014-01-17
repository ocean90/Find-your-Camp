package de.fhkoeln.gm.findyourcamp.app.gcm;

import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import android.os.Bundle;

public class MessageBroker {

	public static final int ACTION_RESPONSE_USER_REGISTER  = 1;
	public static final int ACTION_RESPONSE_SEARCH_REQUEST = 2;

	Bundle data;

	public MessageBroker(Bundle data) {
		this.data = data;
	}

	public void handleRequest() {
		int action = Integer.parseInt( data.getString("action") );

		switch (action) {
		case ACTION_RESPONSE_USER_REGISTER:
			Logger.info("Action: Register");
			break;
		case ACTION_RESPONSE_SEARCH_REQUEST:
			Logger.info("Action: Search Request");
			break;
		default:
			Logger.error("Unsupported action: " + action);
			break;
		}

	}
}
