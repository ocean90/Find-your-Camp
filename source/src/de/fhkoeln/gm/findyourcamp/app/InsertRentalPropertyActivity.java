package de.fhkoeln.gm.findyourcamp.app;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.code.widget.RangeSeekBar;
import com.google.code.widget.RangeSeekBar.OnRangeSeekBarChangeListener;

import de.fhkoeln.gm.findyourcamp.app.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmMessage;
import de.fhkoeln.gm.findyourcamp.app.gcm.MessageConstants;
import de.fhkoeln.gm.findyourcamp.app.map.PlaceAutocompleteAdapter;
import de.fhkoeln.gm.findyourcamp.app.model.RentalProperty;
import de.fhkoeln.gm.findyourcamp.app.model.RentalPropertyFeatures;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;
import de.fhkoeln.gm.findyourcamp.app.utils.PreferencesStorage;

/**
 * Activity zum Angeben der Ausstattung eines Mietobjektes
 * 
 */
public class InsertRentalPropertyActivity extends Activity {

	private RangeSeekBar<Integer> priceRangeSeekBar;

	/**
	 * Initalisierung
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled( true );

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_insert_rental_property );

		/**
		 * Autovervollstaendigung der Location (Country)
		 */
		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById( R.id.rental_property_location_autocomplete );
		autoCompView.setAdapter( new PlaceAutocompleteAdapter( this, android.R.layout.simple_list_item_1 ) );
		autoCompView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
				InputMethodManager in = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
				in.hideSoftInputFromWindow( arg1.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );

			}
		} );

		/**
		 * Spinner zur Auswahl der Gruppengroeße
		 */
		Spinner rentalPropertyGroupSizeSpinner = (Spinner) findViewById( R.id.rental_property_group_size );
		ArrayAdapter<CharSequence> rentalPropertyGroupSizeAdapter = ArrayAdapter.createFromResource( this,
			R.array.rental_property_group_size_array, android.R.layout.simple_spinner_item );
		rentalPropertyGroupSizeAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		rentalPropertyGroupSizeSpinner.setAdapter( rentalPropertyGroupSizeAdapter );

		/**
		 * Preisspanne von 0 bis 20
		 */
		int priceMin = 0;
		int priceMax = 20;

		final NumberFormat nf = NumberFormat.getCurrencyInstance( Locale.GERMANY );
		String priceMinFormatted = nf.format( priceMin );
		String priceMaxFormatted = nf.format( priceMax );

		final TextView priceRangeFeedback = (TextView) findViewById( R.id.rental_property_section_title_price_range_feedback );
		priceRangeFeedback.setText( String.format( getResources().getString(
			R.string.rental_property_section_title_price_range_feedback, priceMinFormatted, priceMaxFormatted ) ) );

		priceRangeSeekBar = new RangeSeekBar<Integer>( priceMin, priceMax, getApplicationContext() );
		priceRangeSeekBar.setOnRangeSeekBarChangeListener( new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged( RangeSeekBar<?> bar, Integer minValue, Integer maxValue ) {
				if ( minValue.equals( maxValue ) ) {
					priceRangeFeedback.setText( String.format( getResources().getString(
						R.string.rental_property_section_title_price_feedback, nf.format( minValue ) ) ) );
				} else {
					priceRangeFeedback.setText( String.format( getResources().getString(
						R.string.rental_property_section_title_price_range_feedback, nf.format( minValue ),
						nf.format( maxValue ) ) ) );
				}
			}
		} );

		ViewGroup rentalPropertyPriceView = (ViewGroup) findViewById( R.id.rental_property_price_view );
		rentalPropertyPriceView.addView( priceRangeSeekBar );
	}

	/**
	 * Auswahl ueber Menue, bisher Navigationspunkt zurueck (zur Hauptseite)
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
		// Navigation zur Homeseite
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
			case R.id.rental_property_button_save:
				saveRentalProperty();
				return true;
			default:
				return false;
		}
	}

	/**
	 * Laden des Formulars zur Ausstattung
	 * 
	 * @return
	 */
	private RentalProperty prepareRentalPropertyData() {
		RentalProperty rentalPropertyModel = new RentalProperty();
		RentalPropertyFeatures rentalPropertyFeatures = new RentalPropertyFeatures();

		// Ort
		final AutoCompleteTextView countryField = (AutoCompleteTextView) findViewById( R.id.rental_property_location_autocomplete );
		String location = countryField.getText().toString();
		rentalPropertyModel.setLocation( location );

		// Gruppengroeße
		final Spinner groupSizeField = (Spinner) findViewById( R.id.rental_property_group_size );
		int groupSize = Integer.parseInt( groupSizeField.getSelectedItem().toString() );
		rentalPropertyModel.setGroupSize( groupSize );

		// Preisspanne
		int priceRangeMin = priceRangeSeekBar.getSelectedMinValue();
		int priceRangeMax = priceRangeSeekBar.getSelectedMaxValue();
		rentalPropertyModel.setPriceRange( priceRangeMin, priceRangeMax );

		// Features
		final CheckBox toiletsAvailableField = (CheckBox) findViewById( R.id.rental_property_toilets_available );
		boolean toiletsAvailable = toiletsAvailableField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.TOILETS_AVAILABLE_KEY, toiletsAvailable );

		final CheckBox bbqAllowedField = (CheckBox) findViewById( R.id.rental_property_bbq_allowed );
		boolean bbqAllowed = bbqAllowedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.BBQ_ALLOWED_KEY, bbqAllowed );

		final CheckBox internetAvailableField = (CheckBox) findViewById( R.id.rental_property_internet_available );
		boolean internetAvailable = internetAvailableField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.INTERNET_AVAILABLE_KEY, internetAvailable );

		final CheckBox childrenAllowedField = (CheckBox) findViewById( R.id.rental_property_children_allowed );
		boolean children_allowed = childrenAllowedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.CHILDREN_ALLOWED_KEY, children_allowed );

		final CheckBox publicTransportNearbyField = (CheckBox) findViewById( R.id.rental_property_public_transport_nearby );
		boolean publicTransportNearby = publicTransportNearbyField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.PUBLIC_TRANSPORT_NEARBY_KEY, publicTransportNearby );

		final CheckBox showerBathAvailableField = (CheckBox) findViewById( R.id.rental_property_shower_bath_available );
		boolean showerBathAvailable = showerBathAvailableField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.SHOWER_BATH_AVAILABLE_KEY, showerBathAvailable );

		final CheckBox waterProvidedField = (CheckBox) findViewById( R.id.rental_property_water_provided );
		boolean waterProvided = waterProvidedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.WATER_PROVIDED_KEY, waterProvided );

		final CheckBox tentsProvidedField = (CheckBox) findViewById( R.id.rental_property_tents_provided );
		boolean tentsProvided = tentsProvidedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.TENTS_PROVIDED_KEY, tentsProvided );

		final CheckBox powerProvidedField = (CheckBox) findViewById( R.id.rental_property_power_provided );
		boolean powerProvided = powerProvidedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.POWER_PROVIDED_KEY, powerProvided );

		final CheckBox dogsAllowedField = (CheckBox) findViewById( R.id.rental_property_dogs_allowed );
		boolean dogsAllowed = dogsAllowedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.DOGS_ALLOWED_KEY, dogsAllowed );

		final CheckBox caravanParkingPossibleField = (CheckBox) findViewById( R.id.rental_property_caravan_parking_possible );
		boolean caravanParkingPossible = caravanParkingPossibleField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.CARAVAN_PARKING_POSSIBLE_KEY, caravanParkingPossible );

		final CheckBox campfireAllowedField = (CheckBox) findViewById( R.id.rental_property_campfire_allowed );
		boolean campfireAllowed = campfireAllowedField.isChecked();
		rentalPropertyFeatures.setFeature( RentalPropertyFeatures.CAMPFIRE_ALLOWED_KEY, campfireAllowed );

		rentalPropertyModel.setFeatures( rentalPropertyFeatures );

		return rentalPropertyModel;
	}

	/**
	 * Datenmodell der Grundstuecksausstattung wird abgespeichert
	 */
	private void saveRentalProperty() {
		RentalProperty rentalProperty = prepareRentalPropertyData();

		if ( rentalProperty.isValid() ) {
			RentalPropertiesTable rentalPropertiesTable = new RentalPropertiesTable( this );
			SQLiteDatabase rentalPropertiesDatabase = rentalPropertiesTable.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put( RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_LOCATION, rentalProperty.getLocation() );
			values.put( RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_GROUP_SIZE, rentalProperty.getGroupSize() );
			values.put( RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_MIN_PRICE, rentalProperty.getPriceRange()[0] );
			values.put( RentalPropertiesTable.COLUMN_NAME_RENTAL_PROPERTY_MAX_PRICE, rentalProperty.getPriceRange()[1] );

			ArrayList<String> features = rentalProperty.getFeatures().getAvailableFeatures();
			for ( String feature : features ) {
				values.put( feature, true ); // Feature Key
			}

			final long newRentalPropertyId = rentalPropertiesDatabase.insert( RentalPropertiesTable.TABLE_NAME, null,
				values );

			if ( newRentalPropertyId == -1 ) {
				Logger.error( "Fehler beim Eintragen des Mietobjektes" );
				return;
			}

			rentalPropertiesDatabase.close();

			GcmMessage message = new GcmMessage();
			message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
			message.setAction( MessageConstants.ACTION_RENTAL_PROPERTY_REGISTRATION );

			PreferencesStorage preferences = new PreferencesStorage( getApplicationContext() );
			SharedPreferences settings = preferences.getSettings();
			int userId = settings.getInt( "user_id", 0 );

			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put( "user_id", userId );
			payload.put( "rental_property_local_id", newRentalPropertyId );
			payload.put( "rental_property_location", rentalProperty.getLocation() );
			message.setPayload( payload );
			GcmClient client = GcmClient.getInstance( this );
			client.sendMessage( message );

			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setMessage( R.string.insert_rental_property_success_dialog_message ).setTitle(
				R.string.insert_rental_property_success_dialog_title );
			builder.setPositiveButton( R.string.insert_rental_property_success_dialog_yes,
				new DialogInterface.OnClickListener() {
					public void onClick( DialogInterface dialog, int id ) {
						// User wants to see the rental property
						dialog.dismiss();
						Intent intent = new Intent( getApplicationContext(), SingleRentalPropertyActivity.class );
						intent.putExtra( "rental_property_id", newRentalPropertyId );
						startActivity( intent );
					}
				} );
			builder.setNegativeButton( R.string.insert_rental_property_success_dialog_back,
				new DialogInterface.OnClickListener() {
					public void onClick( DialogInterface dialog, int id ) {
						// User cancelled the dialog
						dialog.cancel();
						finish();
					}
				} );
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
		}
	}

}
