package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.DevicesTable;
import de.fhkoeln.gm.findyourcamp.server.gcm.model.GcmMessage;
import de.fhkoeln.gm.findyourcamp.server.matching.LocationMatch;
import de.fhkoeln.gm.findyourcamp.server.model.Device;
import de.fhkoeln.gm.findyourcamp.server.model.RentalProperty;
import de.fhkoeln.gm.findyourcamp.server.model.User;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

/**
 * Messagebroker leitet eingehende Nachricht an Aktion zur Registrierung oder
 * Suchanfrage weiter.
 *
 */
public class MessageBroker {

	Map<String, Object> data;
	private String from;

	/**
	 * Uebergebenes Object wird in JSON Objekt zur weiteren Verarbeitung
	 * umgewandelt "from": "regid", "message_id": "wadsdsd", "data": { "json" :
	 * "{"action":1}" }
	 *
	 * @param object
	 */
	@SuppressWarnings( "unchecked" )
	public MessageBroker( Map<String, Object> object ) {
		// Payload holen
		Map<String, Object> rawData = (Map<String, Object>) object.get( "data" );

		// Ist json Feld vorhanden?
		if ( null == rawData.get( "json" ) ) {
			return;
		}

		// JSON Feld holen, in String umwandeln und zum JSON Objekt umwandelen.
		String json = rawData.get( "json" ).toString();
		try {
			this.data = (Map<String, Object>) JSONValue.parseWithException( json );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		// regID des Senderdevice
		this.from = object.get( "from" ).toString();
	}

	/**
	 * Anfrage wird nach gewuenschtem Wert an Aktion weitergeleitet
	 */
	public void handleRequest() {

		if ( null == data || null == data.get( "action" ) ) {
			return;
		}

		int action = ( (Long) data.get( "action" ) ).intValue();

		switch ( action ) {
			case MessageConstants.ACTION_USER_REGISTRATION:
				// User-Registrierung
				handleUserRegistration();
				break;
			case MessageConstants.ACTION_SEARCH_REQUEST:
				// Suchanfrage
				handleSearchRequest();
				break;
			case MessageConstants.ACTION_MATCH_RESPONSE:
				// Mietobjekt gefunden
				handleMatchResponse();
				break;
			case MessageConstants.ACTION_RENTAL_PROPERTY_REGISTRATION:
				// Mietobjekt anlegen
				handleRentalPropertyRegistration();
				break;
			default:
				Logger.err( "Unbekannte Aktion: " + action );
				break;
		}
	}

	private void handleMatchResponse() {
		long hostUserId = (Long) data.get( "host_user_id" );
		long rentUserId = (Long) data.get( "rent_user_id" );
		DbConnection dbConnection = DbConnection.getInstance();

		ArrayList<String> registrationIds = new ArrayList<String>();
		// Zugehörige Devices und deren Registierungs-ID holen.
		Statement statement;
		try {
			statement = dbConnection.createStatement();
			String sql = "SELECT * FROM " + DevicesTable.TABLE_NAME + " WHERE " + DevicesTable.COLUMN_NAME_DEVICE_ID
				+ " =" + rentUserId + ";";
			ResultSet DevicesResultSet = statement.executeQuery( sql );

			while ( DevicesResultSet.next() ) {
				registrationIds.add( DevicesResultSet.getString( 2 ) );
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		if ( registrationIds.isEmpty() ) {
			return;
		}

		GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();

		for ( Iterator<String> registrationIdsIterator = registrationIds.iterator(); registrationIdsIterator
			.hasNext(); ) {
			String registrationId = (String) registrationIdsIterator.next();
			GcmMessage message = new GcmMessage();
			message.setTo( registrationId );
			message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
			message.setAction( MessageConstants.ACTION_SEARCH_RESULT );
			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put( "host_user_id", hostUserId );
			message.setPayload( payload );

			gcmConnection.sendMessage( message );
		}

	}

	private void handleRentalPropertyRegistration() {
		System.out.println( "Neue Mietobjekt-Registrierung..." );
		long userId = (Long) data.get( "user_id" );
		long rentalPropertyLocalId = (Long) data.get( "rental_property_local_id" );
		String rentalPropertyLocation = (String) data.get( "rental_property_location" );

		// Neue ID nach Anlegen erhalten
		long newRentalPropertyId = RentalProperty.createRentalPropertyEntry( rentalPropertyLocalId,
			rentalPropertyLocation, userId );

		// Sende Feedback
		GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();

		GcmMessage message = new GcmMessage();
		message.setTo( this.from );
		message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
		message.setAction( MessageConstants.ACTION_RENTAL_PROPERTY_REGISTERED );

		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put( "rental_property_remote_id", newRentalPropertyId );
		message.setPayload( payload );

		gcmConnection.sendMessage( message );

		System.out.println( "Registrierung für Mietobjekt " + rentalPropertyLocalId + "(Remote: " + newRentalPropertyId
			+ ") abgeschlossen." );
	}

	/**
	 * Registrierung
	 */
	public void handleUserRegistration() {
		System.out.println( "Neue Benutzer-Registrierung..." );
		String userName = (String) data.get( "user_name" );
		String userEmail = (String) data.get( "user_email" );

		// DeviceID
		String regId = this.from;

		// Neue UserID nach Anlegen erhalten
		int newUserId = User.createUser( userName, userEmail );

		// Zuordnung User mit Device ID
		if ( newUserId > 0 ) {
			int newDeviceId = Device.assignDeviceToUser( regId, newUserId );
			System.out.println( "Registrierung für User " + newUserId + " und Device " + newDeviceId
				+ " abgeschlossen." );

			// Sende neu registrierten User ein Feedback
			GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();

			GcmMessage message = new GcmMessage();
			message.setTo( this.from );
			message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
			message.setAction( MessageConstants.ACTION_USER_REGISTERED );

			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put( "user_id", newUserId );
			message.setPayload( payload );

			gcmConnection.sendMessage( message );
		}

	}

	public void handleSearchRequest() {
		System.out.println( "Neue Suchanfrage..." );

		String location = (String) data.get( "location" );
		JSONObject features = (JSONObject) data.get( "features" );
		Long minPrice = (Long) data.get( "min_price" );
		Long maxPrice = (Long) data.get( "max_price" );
		Long groupSize = (Long) data.get( "group_size" );
		Long rentUserId = (Long) data.get( "rent_user_id" );

		HashMap<String, ArrayList<Object>> matches = LocationMatch.getMatches( location, rentUserId );
		ArrayList<Object> registrationIds = matches.get( "registrationIds" );
		ArrayList<Object> userIds = matches.get( "userIds" );
		ArrayList<Object> localRentalPropertyIds = matches.get( "localRentalPropertyIds" );

		GcmXmppConnection gcmConnection = GcmXmppConnection.getInstance();
		if ( registrationIds.isEmpty() ) {
			GcmMessage message = new GcmMessage();
			message.setTo( this.from );
			message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
			message.setAction( MessageConstants.ACTION_SEARCH_RESULT );
			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put( "found_items", 0 );
			message.setPayload( payload );
			gcmConnection.sendMessage( message );
		} else {
			int i = 0;
			for ( Iterator<Object> registrationIdsIterator = registrationIds.iterator(); registrationIdsIterator
				.hasNext(); ) {

				String registrationId = (String) registrationIdsIterator.next();
				Integer userId = (Integer) userIds.get( i );
				Integer localRentalPropertyId = (Integer) localRentalPropertyIds.get( i );

				GcmMessage message = new GcmMessage();
				message.setTo( registrationId );
				message.setMessageId( "m-" + ( System.currentTimeMillis() / 1000L ) );
				message.setAction( MessageConstants.ACTION_MATCH_REQUEST );
				HashMap<String, Object> payload = new HashMap<String, Object>();
				payload.put( "features", features );
				payload.put( "min_price", minPrice );
				payload.put( "max_price", maxPrice );
				payload.put( "group_size", groupSize );
				payload.put( "rental_property_local_id", localRentalPropertyId );
				payload.put( "user_id", userId );
				payload.put( "rent_user_id", rentUserId );
				message.setPayload( payload );

				gcmConnection.sendMessage( message );
			}
		}

		System.out.println( "Suche nach " + location + " ergab " + registrationIds.size() + " Treffer." );
	}
}
