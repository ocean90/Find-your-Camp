package de.fhkoeln.gm.findyourcamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.fhkoeln.gm.findyourcamp.gcm.Client;
import de.fhkoeln.gm.findyourcamp.utils.GooglePlayServices;
import de.fhkoeln.gm.findyourcamp.utils.Logger;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			Client client = new Client(this);
			if (client.hasRegistrationId()) {
				Logger.info("reg id exists");
			} else {
				Logger.error("no reg id");
				client.register();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case R.id.action_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_insert_rental_property:
				intent = new Intent(this, InsertRentalPropertyActivity.class);
				startActivity(intent);
				return true;
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
