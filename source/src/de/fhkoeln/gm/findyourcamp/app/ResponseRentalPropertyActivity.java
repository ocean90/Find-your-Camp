package de.fhkoeln.gm.findyourcamp.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * Activity zum Suchresultat
 *
 */
public class ResponseRentalPropertyActivity extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled( true );

		setContentView( R.layout.activity_response_rental_property );

		Intent intent = getIntent();
		String matchRate = intent.getStringExtra( "match_rate" );
		String userName = intent.getStringExtra( "user_name" );
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
