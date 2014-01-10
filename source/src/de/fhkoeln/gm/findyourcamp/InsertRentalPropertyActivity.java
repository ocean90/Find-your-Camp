package de.fhkoeln.gm.findyourcamp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import de.fhkoeln.gm.findyourcamp.map.PlaceAutocompleteAdapter;

public class InsertRentalPropertyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_rental_property);

		/**
		 * Country autocomplete.
		 */
		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.rental_property_location_autocomplete);
		autoCompView.setAdapter(new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1));

		/**
		 * Spinner for place size.
		 */
		Spinner rentalPropertyPlaceSizeSpinner = (Spinner) findViewById(R.id.rental_property_place_size);
		ArrayAdapter<CharSequence> rentalPropertyPlaceSizeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.rental_property_place_size_array, android.R.layout.simple_spinner_item);
		rentalPropertyPlaceSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rentalPropertyPlaceSizeSpinner.setAdapter(rentalPropertyPlaceSizeAdapter);

		/**
		 * Spinner for group size.
		 */
		Spinner rentalPropertyGroupSizeSpinner = (Spinner) findViewById(R.id.rental_property_group_size);
		ArrayAdapter<CharSequence> rentalPropertyGroupSizeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.rental_property_group_size_array, android.R.layout.simple_spinner_item);
		rentalPropertyGroupSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rentalPropertyGroupSizeSpinner.setAdapter(rentalPropertyGroupSizeAdapter);
	}

}
