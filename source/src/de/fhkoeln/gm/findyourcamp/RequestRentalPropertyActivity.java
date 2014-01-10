package de.fhkoeln.gm.findyourcamp;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RequestRentalPropertyActivity extends Activity {
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_rental_property);

		/*if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.rental_property_map))
	                            .getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }*/

		Spinner spinner = (Spinner) findViewById(R.id.rental_property_place_size);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.rental_property_place_size_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}
}
