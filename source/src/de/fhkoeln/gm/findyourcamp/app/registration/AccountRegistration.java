package de.fhkoeln.gm.findyourcamp.app.registration;

import android.content.Context;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmMessage;

/**
 * Registrierung eines Accounts
 *
 */
public class AccountRegistration {
	private Context appContext;

	public AccountRegistration(Context context) {
		this.appContext = context;
	}

	private void storeUserDataLocal() {

	}

	private void doRemoteRegistration() {
		GcmClient gcmClient = new GcmClient(this.appContext);

		GcmMessage message = new GcmMessage();

		gcmClient.sendMessage(message);
	}
}
