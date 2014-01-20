package de.fhkoeln.gm.findyourcamp.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.fhkoeln.gm.findyourcamp.app.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.utils.GooglePlayServices;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

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

		// Abfrage ob GooglePlayService verfuegbar ist
		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			client = GcmClient.getInstance(this);
			if (client.hasRegistrationId()) {
				setContentView(R.layout.activity_main);

				Intent intent = getIntent();
				String status = intent.getStringExtra("status");
				String userName = intent.getStringExtra("user_name");

				if (status != null && status.equals("registered")) {
					TextView welcomeMessageView = (TextView) findViewById(R.id.main_welcome_message);
					welcomeMessageView.setText(String.format(getResources()
							.getString(R.string.main_registerd_message),
							userName));

				}
			} else {
				Intent intent = new Intent(this, RegistrationActivity.class);
				startActivity(intent);
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


	@Override
	protected void onResume() {
		super.onResume();

		if (!GooglePlayServices.check(this)) {
			finish();
		}
	}

}
