package de.fhkoeln.gm.findyourcamp.app;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmMessage;
import de.fhkoeln.gm.findyourcamp.app.gcm.MessageConstants;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

/**
 * Activity zur Freigabe
 *
 */
public class ShareRentalPropertyActivity extends Activity {

	private int matchRate;
	private int rentUserId;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled( true );

		setContentView( R.layout.activity_share_rental_property );

		Intent intent = getIntent();
		Bundle intentExtras = intent.getExtras();
		matchRate = intentExtras.getInt( "match_rate" );
		rentUserId = intentExtras.getInt( "rent_user_id" );

		TextView infoMessage = (TextView) findViewById( R.id.share_rental_property_info );
		infoMessage.setText( "Dein Mietobjekt passt zu einer Anfrage eines möglichen Mieters. Die Quote liegt bei "
			+ matchRate + "%.\n\nDas Mietobjekt nun freigeben?" );
	}

	public boolean onButtonClicked( View view ) {
		switch ( view.getId() ) {
			case R.id.share_rental_property:
				shareRentalProperty();
				return true;
			default:
				return false;
		}
	}

	private void shareRentalProperty() {
		GcmMessage message = new GcmMessage();
		message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
		message.setAction( MessageConstants.ACTION_MATCH_RESPONSE );

		PreferencesStorage preferences = new PreferencesStorage( getApplicationContext() );
		SharedPreferences settings = preferences.getSettings();
		int userId = settings.getInt( "user_id", 0 );

		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put( "host_user_id", userId );
		payload.put( "rent_user_id", rentUserId );
		message.setPayload( payload );
		GcmClient client = GcmClient.getInstance( this );
		client.sendMessage( message );
	}

	/**
	 * Auswahl ueber Menue, bisher Navigationspunkt zurueck (zur Hauptseite)
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case android.R.id.home:
				// Navigation zur Homeseite
				NavUtils.navigateUpFromSameTask( this );
				return true;
			default:
				return super.onOptionsItemSelected( item );
		}
	}
}
