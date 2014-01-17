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

public class MainActivity extends Activity {

	private GcmClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			client = new GcmClient(this);
			if (client.hasRegistrationId()) {
				Logger.info("reg id exists " + client.getRegistrationId() );
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

	public void onButtonClicked(View view) {
		GcmMessage message = new GcmMessage();
		message.setMessageId("m-" + (System.currentTimeMillis() / 1000L));
		message.setAction(MessageConstants.ACTION_USER_REGISTRATION);
		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put("user_name", "Max");
		payload.put("user_email", "Max@mustermann.com");
		message.setPayload(payload);
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
