package de.fhkoeln.gm.findyourcamp.gcm;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.fhkoeln.gm.findyourcamp.utils.Logger;
import de.fhkoeln.gm.findyourcamp.utils.PreferencesStorage;

public class Client {

	private final String API_ENDPOINT = "@gcm.googleapis.com";
	private final String SENDER_ID = "474445016109";

	private GoogleCloudMessaging gcm;
	private Context appContext;

	/**
	 * Constructor.
	 */
	public Client(Context context) {
		this.appContext = context;
		this.gcm = GoogleCloudMessaging.getInstance(context);
	}

	public boolean hasRegistrationId() {
		return ! getRegistrationId().isEmpty();
	}

	public String getRegistrationId() {
		PreferencesStorage preferences = new PreferencesStorage(this.appContext);
		SharedPreferences settings = preferences.getSettings();
		String registrationId = settings.getString("registrationId", "");

		if ( registrationId.isEmpty() )
			return "";

		return registrationId;

	}

	public void saveRegisrationId( String registrationId ) {
		PreferencesStorage preferences = new PreferencesStorage(this.appContext);
		SharedPreferences.Editor preferencesEditor = preferences.getEditor();
		preferencesEditor.putString("registrationId", registrationId);
		preferencesEditor.commit();
	}

	public void register() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String registrationId = "";
				try {
					registrationId = gcm.register(SENDER_ID);
					Logger.info("Device registered, registration ID=" + registrationId);

					saveRegisrationId(registrationId);
				} catch (IOException ex) {
					Logger.error(ex.getMessage());
				}
				return registrationId;
			}

			@Override
			protected void onPostExecute(String msg) {
				Logger.info(msg);
			}
		}.execute(null, null, null);
	}

	public void unregister() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					gcm.unregister();
					Logger.info("Device unregistered");

					// @TODO Remove regid from store
				} catch (IOException ex) {
					Logger.error(ex.getMessage());
				}
				return "0";
			}

			@Override
			protected void onPostExecute(String msg) {
				Logger.info(msg);
			}
		}.execute(null, null, null);
	}

	/**
	 * Send a message to GCM service.
	 *
	 * @param message
	 * @return
	 */
	public Response send(Message message) {
		return null;
	}

}
