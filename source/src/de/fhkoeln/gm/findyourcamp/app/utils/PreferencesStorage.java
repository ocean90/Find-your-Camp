package de.fhkoeln.gm.findyourcamp.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesStorage {

	private SharedPreferences settings;

	public PreferencesStorage( Context context ) {
		this.settings = PreferenceManager.getDefaultSharedPreferences( context );
	}

	public SharedPreferences getSettings() {
		return this.settings;
	}

	public SharedPreferences.Editor getEditor() {
		return this.settings.edit();
	}
}
