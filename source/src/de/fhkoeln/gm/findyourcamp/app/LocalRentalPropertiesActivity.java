package de.fhkoeln.gm.findyourcamp.app;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import de.fhkoeln.gm.findyourcamp.app.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.app.model.RentalProperty;

/**
 * Activity zur Ausgabe eines Mietobjektes
 * 
 */
public class LocalRentalPropertiesActivity extends ListActivity {

	/**
	 * Initalisierung
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_local_rental_properties );
		this.getListView().setDividerHeight( 2 );

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled( true );

		registerForContextMenu( getListView() );

		fillList();
	}

	private void fillList() {
		List<RentalProperty> localRentalProperties = new ArrayList<RentalProperty>();

		RentalPropertiesTable rentalPropertiesTable = new RentalPropertiesTable( getApplicationContext() );
		SQLiteDatabase rentalPropertiesDatabase = rentalPropertiesTable.getWritableDatabase();

		Cursor cursor = rentalPropertiesDatabase.query( RentalPropertiesTable.TABLE_NAME, null, null, null, null, null,
			null );

		cursor.moveToFirst();
		while ( !cursor.isAfterLast() ) {
			RentalProperty comment = RentalProperty.cursorToRentalProperty( cursor );
			localRentalProperties.add( comment );
			cursor.moveToNext();
		}
		cursor.close();

		ArrayAdapter<RentalProperty> adapter = new ArrayAdapter<RentalProperty>( this,
			android.R.layout.simple_list_item_1, localRentalProperties );

		setListAdapter( adapter );
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
