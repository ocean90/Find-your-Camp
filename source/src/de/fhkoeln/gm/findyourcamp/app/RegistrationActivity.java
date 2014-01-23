package de.fhkoeln.gm.findyourcamp.app;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmClient;
import de.fhkoeln.gm.findyourcamp.app.gcm.GcmMessage;
import de.fhkoeln.gm.findyourcamp.app.gcm.MessageConstants;

public class RegistrationActivity extends Activity {

	/**
     *
     */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_registration );
	}

	public boolean onButtonClicked( View view ) {
		switch ( view.getId() ) {
			case R.id.registration_user_button_register:
				doRegistration();
				return true;
			default:
				return false;
		}
	}

	public void doRegistration() {
		GcmClient client = GcmClient.getInstance( this );

		// Device wird registriert
		client.register();

		GcmMessage message = new GcmMessage();
		message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
		message.setAction( MessageConstants.ACTION_USER_REGISTRATION );

		final EditText userNameField = (EditText) findViewById( R.id.registration_user_name );
		String userName = userNameField.getText().toString();

		final EditText userEmailField = (EditText) findViewById( R.id.registration_user_email );
		String userEmail = userEmailField.getText().toString();

		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put( "user_name", userName );
		payload.put( "user_email", userEmail );
		message.setPayload( payload );
		client.sendMessage( message );

		Intent intent = new Intent( this, MainActivity.class );
		intent.putExtra( "status", "registered" );
		intent.putExtra( "user_name", userName );
		startActivity( intent );
	}
}
