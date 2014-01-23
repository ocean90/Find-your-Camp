package de.fhkoeln.gm.findyourcamp.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import de.fhkoeln.gm.findyourcamp.app.model.RentalProperty;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;

/**
 * Activity zur Ausgabe eines Mietobjektes
 * 
 */
public class SingleRentalPropertyActivity extends Activity {

	private RentalProperty rentalProperty;

	/**
	 * Initalisierung
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled( true );

		setContentView( R.layout.activity_single_rental_property );

		Intent intent = getIntent();
		Bundle intentExtras = intent.getExtras();

		if ( intentExtras == null ) {
			Logger.error( "Intent Extras are empty" );
			return;
		}

		long rental_property_id = intentExtras.getLong( "rental_property_id" );

		rentalProperty = RentalProperty.getFromId( rental_property_id, getApplicationContext() );

		TextView debugTextView = (TextView) findViewById( R.id.single_rental_property_debug );
		debugTextView.setText( rentalProperty.getLocation() );
	}

	/**
	 * Auswahl ueber Menue, bisher Navigationspunkt zurueck (zur Hauptseite)
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask( this );
				return true;
			default:
				return super.onOptionsItemSelected( item );
		}
	}

	/**
	 * Bei Buttonausfuehrung wird Aktion ausgefuehrt (speichern)
	 * 
	 * @param view
	 * @return
	 */
	public boolean onButtonClicked( View view ) {
		switch ( view.getId() ) {
			default:
				return false;
		}
	}

}
