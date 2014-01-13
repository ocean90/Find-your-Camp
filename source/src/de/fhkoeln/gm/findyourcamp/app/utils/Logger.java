package de.fhkoeln.gm.findyourcamp.app.utils;

import android.util.Log;

/**
 * Simple logger.
 *
 */
final public class Logger {

	private static final String TAG = "FindYourCamp";

	// Private constructor.
	private Logger() {
	}

	/**
	 * Prints a debug log message.
	 *
	 * @param String message
	 */
	public static void debug(String message) {
		Log.d(TAG, message);
	}

	/**
	 * Prints an error message.
	 *
	 * @param String message
	 */
	public static void error(String message) {
		Log.e(TAG, message);
	}

	/**
	 * Prints an info message.
	 *
	 * @param String message
	 */
	public static void info(String message) {
		Log.i(TAG, message);
	}

	/**
	 * Prints an error message with exception data.
	 *
	 * @param String message
	 * @param Throwable tr
	 */
	public static void error(String message, Throwable tr) {
		Log.e(TAG, message, tr);
	}


}
