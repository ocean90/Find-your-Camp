package de.fhkoeln.gm.findyourcamp.app;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.fhkoeln.gm.findyourcamp.app.R;
import de.fhkoeln.gm.findyourcamp.app.actionbar.adapter.TitleNavigationAdapter;
import de.fhkoeln.gm.findyourcamp.app.actionbar.model.SpinnerNavItem;
import de.fhkoeln.gm.findyourcamp.app.gcm.Client;
import de.fhkoeln.gm.findyourcamp.app.utils.GooglePlayServices;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;

public class MainActivity extends Activity implements ActionBar.OnNavigationListener {

    // action bar
    private ActionBar actionBar;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getActionBar();

        // Hide the action bar title
        actionBar.setDisplayShowTitleEnabled(false);

        // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem("Local", R.drawable.ic_launcher));
        navSpinner.add(new SpinnerNavItem("My Places", R.drawable.ic_launcher));
        navSpinner.add(new SpinnerNavItem("Checkins", R.drawable.ic_launcher));
        navSpinner.add(new SpinnerNavItem("Latitude", R.drawable.ic_launcher));

        // title drop down adapter
        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);

        // assigning the spinner navigation
        actionBar.setListNavigationCallbacks(adapter, this);

		if (!GooglePlayServices.check(this)) {
			finish();
		} else {
			Client client = new Client(this);
			if (client.hasRegistrationId()) {
				Logger.info("reg id exists " + client.getRegistrationId() );

				client.sendMessage();

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

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

}
