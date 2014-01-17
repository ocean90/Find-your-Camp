package de.fhkoeln.gm.findyourcamp.app;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmMessage;
import de.fhkoeln.gm.findyourcamp.app.gcm.MessageConstants;
import de.fhkoeln.gm.findyourcamp.app.utils.GooglePlayServices;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;


/**
 * MainActivity als Startpunkt der Anwendung.
 *
 */
public class MainActivity extends Activity {


	private GcmClient client;

    /**
     * Initialisierung nach Start
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

<<<<<<< HEAD
		//  Content der Activity über Layoutresource setzen
		setContentView(R.layout.activity_main);
		
		// Abfrage ob GooglePlayService (dieser Activity) verfuegbar
		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			// Erstellen eines neuen GcmClients, sofern nicht verfuegbar
			client = new GcmClient(this);
			// Check ob RegistrierungsID vorhanden
=======

		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			client = GcmClient.getInstance(this);
>>>>>>> 6b0afb7cab32f2e665cf3de84e4cc7994940807c
			if (client.hasRegistrationId()) {
				setContentView(R.layout.activity_main);
				//Logger.info("reg id exists " + client.getRegistrationId() );
			} else {
				Logger.error("no reg id");
				client.register(); // Wenn keine RegID vorhanden, dann neu Einrichten
			}
		}
	}

	/**
	 * Optionsmenue initalisieren
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Steuert die Handlung der Menueoptionen und startet gewaehlte Activity
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// "Objekt" zum Wechsel der Activities
		Intent intent;
		switch (item.getItemId()) {
			// Activity Einstellungen
			case R.id.action_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			// Actitvity Ausstattung des eigenen Mietobjektes
			case R.id.action_insert_rental_property:
				intent = new Intent(this, InsertRentalPropertyActivity.class);
				startActivity(intent);
				return true;
			// Activity Ausstattung des angefragten Objektes
			case R.id.action_request_rental_property:
				intent = new Intent(this, RequestRentalPropertyActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 * @param view
	 */
	public void onButtonClicked(View view) {
		// Neue GCM Message
		GcmMessage message = new GcmMessage();
		message.setMessageId("m-" + (System.currentTimeMillis() / 1000L));
		message.setAction(MessageConstants.ACTION_USER_REGISTRATION);
		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put("user_name", "Max");
		payload.put("user_email", "Max@mustermann.com");
		message.setPayload(payload);
		// Message wird zum GCM Service gesendet
		client.sendMessage(message);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!GooglePlayServices.check(this)) {
			finish();
		}
	}

}
