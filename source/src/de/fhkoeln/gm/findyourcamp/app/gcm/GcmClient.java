package de.fhkoeln.gm.findyourcamp.app.gcm;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

/**
 * 
 *
 */
public class GcmClient {

	private static GcmClient instance;
	private final String API_ENDPOINT = "@gcm.googleapis.com";
	private final String SENDER_ID = "474445016109";

	private GoogleCloudMessaging gcm;
	private Context appContext;
	AtomicInteger msgId = new AtomicInteger();

	/**
	 * Konstruktor des GcmClient.
	 */
	private GcmClient(Context context) {
		this.appContext = context;
		this.gcm = GoogleCloudMessaging.getInstance(context);
	}

	/**
	 * Singleton.
	 * Erzeugt genau eine Instanz des Objektes für diese Klasse.
	 *
	 * @return
	 */
	public static GcmClient getInstance(Context context) {
		if (GcmClient.instance == null) {
			GcmClient.instance = new GcmClient(context);
		}

		return GcmClient.instance;
	}

	public boolean hasRegistrationId() {
		return !getRegistrationId().isEmpty();
	}

	/**
	 * Pruefen ob regID vorhanden und Ausgabe
	 * @return
	 */
	public String getRegistrationId() {
		PreferencesStorage preferences = new PreferencesStorage(this.appContext);
		SharedPreferences settings = preferences.getSettings();
		String registrationId = settings.getString("registrationId", "");

		// Wenn keine regID vorhanden
		if ( registrationId.isEmpty() )
			return "";

		// Rueckgabe vorhandener regID
		return registrationId;

	}

	/**
	 * Uebergebene regID wird gespeichert.
	 * @param registrationId
	 */
	public void saveRegisrationId( String registrationId ) {
		PreferencesStorage preferences = new PreferencesStorage(this.appContext);
		// Editor zum Modifizieren 
		SharedPreferences.Editor preferencesEditor = preferences.getEditor();
		// Neue regID setzen
		preferencesEditor.putString("registrationId", registrationId);
		//Speichern des regID zum Objekt
		preferencesEditor.commit();
	}

	/**
	 * Registrierung eines neuen Devices 
	 */
	public void register() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String registrationId = "";
				try {
					registrationId = gcm.register(SENDER_ID);
					Logger.info("Device registered, registration ID=" + registrationId);
					
					// neue regID sichern
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

	/**
	 * Registriertes Device wird abgemeldet
	 */
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
	public void sendMessage(GcmMessage message) {

        new AsyncTask<GcmMessage, Void, String>() {
            @Override
            protected String doInBackground(GcmMessage...params) {
            	GcmMessage message = params[0];

                String feedback = "";
                try {
                	gcm.send(SENDER_ID + API_ENDPOINT, message.getMessageId(), message.getData());
                	feedback = "Nachricht gesendet";
                } catch (IOException ex) {
                	feedback = "Error :" + ex.getMessage();
                }
                return feedback;
            }

            @Override
            protected void onPostExecute(String feedback) {
            	 Logger.error(feedback);
            }
        }.execute(message, null, null);
	}

}
