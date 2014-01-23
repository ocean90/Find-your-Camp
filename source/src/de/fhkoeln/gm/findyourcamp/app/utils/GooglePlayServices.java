package de.fhkoeln.gm.findyourcamp.app.utils;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServices {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Check if Google Play services is available.
	 * 
	 * @see http://developer.android.com/google/play-services/setup.html#ensure
	 * @return
	 */
	public static boolean check( Activity activity ) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable( activity );

		/**
		 * Check the result code and show an error dialog on failure if error is
		 * user-recoverable.
		 */
		if ( resultCode != ConnectionResult.SUCCESS ) {
			if ( GooglePlayServicesUtil.isUserRecoverableError( resultCode ) ) {
				GooglePlayServicesUtil.getErrorDialog( resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST ).show();
			}

			return false;
		}

		return true;
	}
}
