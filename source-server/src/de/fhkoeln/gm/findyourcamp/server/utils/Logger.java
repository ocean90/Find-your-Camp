package de.fhkoeln.gm.findyourcamp.server.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple logger for the console.
 *
 * Returns the date, time, package name, method name and the message to log. You
 * can set errortype to one of the ErrorType enum. E_NONE: Hides all messages
 * E_ERROR: Only shows message which are called via Logger.err() E_ALL: Shows
 * all message which are called via Logger.err() and Logger.log()
 *
 * @author Dominik Schilling
 */
public class Logger {

	/**
	 * Enum for error types.
	 */
	public enum ErrorType {
		E_NONE(0), E_ERROR(1), E_ALL(2);

		private int code;

		private ErrorType(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	}

	// Holds the current error type
	public static ErrorType errortype = ErrorType.E_ALL;

	// Private constructor.
	private Logger() {
	}

	/**
	 * Prints a normal log message.
	 *
	 * @param String
	 *            message
	 */
	public static void log(String message) {
		if (errortype.getCode() == 2)
			System.out.printf("%s: %s::%s() - %s\n", getDateTime(),
					getClassName(), getMethodName(), message);
	}

	/**
	 * Prints an error message in red.
	 *
	 * @param String
	 *            message
	 */
	public static void err(String message) {
		if (errortype.getCode() == 1 || errortype.getCode() == 2)
			System.err.printf("%s: %s::%s() - %s\n", getDateTime(),
					getClassName(), getMethodName(), message);
	}

	/**
	 * Returns the current date and time.
	 *
	 * @return String
	 */
	private static String getDateTime() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(Calendar
				.getInstance().getTime());
	}

	/**
	 * Returns the method name where the logger was called.
	 *
	 * @return String
	 */
	private static String getMethodName() {
		// 1) getMethodName - 2) log - 3) X
		return Thread.currentThread().getStackTrace()[3].getMethodName();
	}

	/**
	 * Returns the package and class name where the logger was called.
	 *
	 * @return String
	 */
	private static String getClassName() {
		// 1) getMethodName - 2) log - 3) X
		return Thread.currentThread().getStackTrace()[3].getClassName();
	}

}