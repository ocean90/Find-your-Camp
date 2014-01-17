package de.fhkoeln.gm.findyourcamp.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.db.DevicesTable;
import de.fhkoeln.gm.findyourcamp.server.db.LocationsTable;
import de.fhkoeln.gm.findyourcamp.server.db.RentalPropertiesTable;
import de.fhkoeln.gm.findyourcamp.server.db.UsersTable;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmConfig;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmPacketExtension;
import de.fhkoeln.gm.findyourcamp.server.gcm.GcmXmppConnection;
import de.fhkoeln.gm.findyourcamp.server.model.Device;
import de.fhkoeln.gm.findyourcamp.server.model.User;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

/**
 * Sample Smack implementation of a client for GCM Cloud Connection Server.
 *
 * <p>
 * For illustration purposes only.
 */
public class Main {

	public static final String GCM_SERVER = "gcm.googleapis.com";
	public static final int GCM_PORT = 5235;

	public static final String GCM_ELEMENT_NAME = "gcm";
	public static final String GCM_NAMESPACE = "google:mobile:data";

	public static final String CSS_USERNAME = "474445016109";
	public static final String CSS_PASSWORD = "AIzaSyCCkoUqGUd-O3RI3cf3ZZZme0dIOri5Mf0";

	static Random random = new Random();
	private XMPPConnection connection;
	private ConnectionConfiguration config;

	public DbConnection dbConnection;

	public GcmXmppConnection gmcConnection;

	public Main() {
		// Add GcmPacketExtension
		ProviderManager.getInstance().addExtensionProvider(GcmPacketExtension.GCM_ELEMENT_NAME,
				GcmPacketExtension.GCM_NAMESPACE, new PacketExtensionProvider() {

					@Override
					public PacketExtension parseExtension(XmlPullParser parser)
							throws Exception {
						String json = parser.nextText();
						GcmPacketExtension packet = new GcmPacketExtension(json);
						return packet;
					}
				});

		System.out.println("Datenbanverbindung wird aufgebaut...");
		dbConnection = DbConnection.getInstance();
		if ( ! dbConnection.connect() ) {
			exit(dbConnection.errorMessage);
		}
		System.out.println("Datenbanverbindung erfolgreich aufgebaut.");

		System.out.println("App-Check...");
		checkAppStatus();
		System.out.println("App-Check beendet.");

		System.out.println("Verbindung zu CCS wird aufgebaut...");
		gmcConnection = GcmXmppConnection.getInstance();
		if ( ! gmcConnection.connect(GcmConfig.CSS_USERNAME, GcmConfig.CSS_PASSWORD) ) {
			exit( gmcConnection.errorMessage );
		}
		System.out.println("CCS Verbindung erfolgreich aufgebaut.");

		System.out.println("Die Anwendung läuft nun...");

		//int userId = User.createUser( "peter", "peter@mail.com");

		//int deviceId = Device.assignDeviceToUser("dfdsjfn", userId);
		//Logger.log("Device" + deviceId);
		/*GcmMessage message = new GcmMessage();
		String toRegId = "APA91bE5GOwn36c8XJzFC0Df0Xa6jNjNlTMAsQgu0JHSNegUK_tpLMVyaclD5n5cpr-BZywqRmKwyNnEHWICVl_NAheVGOiCYevUzv3beBP1fPwmfZKxNiE-p1kGbbtrDaIXnCX7KR8A8fI7KMXce5RN6c81n7x-KdI2pCQsJjyTTs4r5CsJRYI";
		message.setTo(toRegId);
		message.setMessageId("ss");

		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put("action", 2);
		payload.put("status", "successfull");

		message.setPayload(payload);

		if (message.isValid()) {
			gmcConnection.sendPacket(message.toJson());
		} else {
			System.err.println("Message not valid");
		}*/
	}

	/**
	 * Prüft, ob eine beliebige Tabelle existiert.
	 */
	private void checkAppStatus() {
		try {
			Statement statement = dbConnection.createStatement();
			// TODO Remove drop calls
			// statement.execute(UsersTable.TABLE_DROP);
			ResultSet tablesResult = statement.executeQuery("SHOW TABLES LIKE '" + UsersTable.TABLE_NAME + "'");
			boolean tableExists = tablesResult.first();
			statement.close();

			if (tableExists) {
				// Tabelle existiert, nichts weiter zu tun.
				return;
			}

			setUpDatebaseTables();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.err(e.getMessage());
			return;
		}

	}

	private void setUpDatebaseTables() {
		System.out.println("Installation begonnen...");

		try {
			Statement statement = dbConnection.createStatement();
			statement.execute(UsersTable.TABLE_CREATE);
			statement.execute(DevicesTable.TABLE_CREATE);
			statement.execute(RentalPropertiesTable.TABLE_CREATE);
			statement.execute(LocationsTable.TABLE_CREATE);
			statement.close();
			System.out.println("Installation abgeschlossen...");
		} catch (SQLException e) {
			Logger.err(e.getMessage());
		}


	}

//	/**
//	 * Returns a random message id to uniquely identify a message.
//	 *
//	 * <p>
//	 * Note: This is generated by a pseudo random number generator for
//	 * illustration purpose, and is not guaranteed to be unique.
//	 *
//	 */
//	public String getRandomMessageId() {
//		return "m-" + Long.toString(random.nextLong());
//	}
//
//	/**
//	 * Sends a downstream GCM message.
//	 */
//	public void send(String jsonRequest) {
//		Packet request = new GcmPacketExtension(jsonRequest).toPacket();
//		connection.sendPacket(request);
//	}
//
//	/**
//	 * Handles an upstream data message from a device application.
//	 *
//	 * <p>
//	 * This sample echo server sends an echo message back to the device.
//	 * Subclasses should override this method to process an upstream message.
//	 */
//	public void handleIncomingDataMessage(Map<String, Object> jsonObject) {
//		String from = jsonObject.get("from").toString();
//
//		// PackageName of the application that sent this message.
//		String category = jsonObject.get("category").toString();
//
//		// Use the packageName as the collapseKey in the echo packet
//		String collapseKey = "echo:CollapseKey";
//		@SuppressWarnings("unchecked")
//		Map<String, String> payload = (Map<String, String>) jsonObject
//				.get("data");
//		payload.put("ECHO", "Application: " + category);
//
//		// Send an ECHO response back
//		/*String echo = createJsonMessage(from, getRandomMessageId(), payload,
//				collapseKey, null, false);
//		send(echo);*/
//	}
//
//	/**
//	 * Handles an ACK.
//	 *
//	 * <p>
//	 * By default, it only logs a INFO message, but subclasses could override it
//	 * to properly handle ACKS.
//	 */
//	public void handleAckReceipt(Map<String, Object> jsonObject) {
//		String messageId = jsonObject.get("message_id").toString();
//		String from = jsonObject.get("from").toString();
//		logger.log(Level.INFO, "handleAckReceipt() from: " + from
//				+ ", messageId: " + messageId);
//	}
//
//	/**
//	 * Handles a NACK.
//	 *
//	 * <p>
//	 * By default, it only logs a INFO message, but subclasses could override it
//	 * to properly handle NACKS.
//	 */
//	public void handleNackReceipt(Map<String, Object> jsonObject) {
//		String messageId = jsonObject.get("message_id").toString();
//		String from = jsonObject.get("from").toString();
//		logger.log(Level.INFO, "handleNackReceipt() from: " + from
//				+ ", messageId: " + messageId);
//	}
//
//	/**
//	 * Creates a JSON encoded GCM message.
//	 *
//	 * @param to
//	 *            RegistrationId of the target device (Required).
//	 * @param messageId
//	 *            Unique messageId for which CCS will send an "ack/nack"
//	 *            (Required).
//	 * @param payload
//	 *            Message content intended for the application. (Optional).
//	 * @param collapseKey
//	 *            GCM collapse_key parameter (Optional).
//	 * @param timeToLive
//	 *            GCM time_to_live parameter (Optional).
//	 * @param delayWhileIdle
//	 *            GCM delay_while_idle parameter (Optional).
//	 * @return JSON encoded GCM message.
//	 */
//	/*public static String createJsonMessage(String to, String messageId,
//			Map<String, String> payload, String collapseKey, Long timeToLive,
//			Boolean delayWhileIdle) {
//		Map<String, Object> message = new HashMap<String, Object>();
//		message.put("to", to);
//		if (collapseKey != null) {
//			message.put("collapse_key", collapseKey);
//		}
//		if (timeToLive != null) {
//			message.put("time_to_live", timeToLive);
//		}
//		if (delayWhileIdle != null && delayWhileIdle) {
//			message.put("delay_while_idle", true);
//		}
//		message.put("message_id", messageId);
//		message.put("data", payload);
//		return JSONValue.toJSONString(message);
//	}*/
//
//	/**
//	 * Creates a JSON encoded ACK message for an upstream message received from
//	 * an application.
//	 *
//	 * @param to
//	 *            RegistrationId of the device who sent the upstream message.
//	 * @param messageId
//	 *            messageId of the upstream message to be acknowledged to CCS.
//	 * @return JSON encoded ack.
//	 */
//	public static String createJsonAck(String to, String messageId) {
//		Map<String, Object> message = new HashMap<String, Object>();
//		message.put("message_type", "ack");
//		message.put("to", to);
//		message.put("message_id", messageId);
//		return JSONValue.toJSONString(message);
//	}
//
//	/**
//	 * Connects to GCM Cloud Connection Server using the supplied credentials.
//	 *
//	 * @param username
//	 *            GCM_SENDER_ID@gcm.googleapis.com
//	 * @param password
//	 *            API Key
//	 * @throws XMPPException
//	 */
//	public void connect(String username, String password) throws XMPPException {
//		config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
//		config.setSecurityMode(SecurityMode.enabled);
//		config.setReconnectionAllowed(true);
//		config.setRosterLoadedAtLogin(false);
//		config.setSendPresence(false);
//		config.setSocketFactory(SSLSocketFactory.getDefault());
//
//		// Debug Mode.
//		// config.setDebuggerEnabled(true);
//		// XMPPConnection.DEBUG_ENABLED = true;
//
//		connection = new XMPPConnection(config);
//		connection.connect();
//
//		connection.addConnectionListener(new ConnectionListener() {
//
//			@Override
//			public void reconnectionSuccessful() {
//				logger.info("Reconnecting..");
//			}
//
//			@Override
//			public void reconnectionFailed(Exception e) {
//				logger.log(Level.INFO, "Reconnection failed.. ", e);
//			}
//
//			@Override
//			public void reconnectingIn(int seconds) {
//				logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
//			}
//
//			@Override
//			public void connectionClosedOnError(Exception e) {
//				logger.log(Level.INFO, "Connection closed on error.");
//			}
//
//			@Override
//			public void connectionClosed() {
//				logger.info("Connection closed.");
//			}
//		});
//
//		// Handle incoming packets
//		connection.addPacketListener(new PacketListener() {
//
//			@Override
//			public void processPacket(Packet packet) {
//				logger.log(Level.INFO, "Received: " + packet.toXML());
//				Message incomingMessage = (Message) packet;
//				GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage
//						.getExtension(GCM_NAMESPACE);
//				String json = gcmPacket.getJson();
//				try {
//					@SuppressWarnings("unchecked")
//					Map<String, Object> jsonObject = (Map<String, Object>) JSONValue
//							.parseWithException(json);
//
//					// present for "ack"/"nack", null otherwise
//					Object messageType = jsonObject.get("message_type");
//
//					if (messageType == null) {
//						// Normal upstream data message
//						handleIncomingDataMessage(jsonObject);
//
//						// Send ACK to CCS
//						String messageId = jsonObject.get("message_id")
//								.toString();
//						String from = jsonObject.get("from").toString();
//						String ack = createJsonAck(from, messageId);
//						send(ack);
//					} else if ("ack".equals(messageType.toString())) {
//						// Process Ack
//						handleAckReceipt(jsonObject);
//					} else if ("nack".equals(messageType.toString())) {
//						// Process Nack
//						handleNackReceipt(jsonObject);
//					} else {
//						logger.log(Level.WARNING,
//								"Unrecognized message type (%s)",
//								messageType.toString());
//					}
//				} catch (ParseException e) {
//					logger.log(Level.SEVERE, "Error parsing JSON " + json, e);
//				} catch (Exception e) {
//					logger.log(Level.SEVERE, "Couldn't send echo.", e);
//				}
//			}
//		}, new PacketTypeFilter(Message.class));
//
//		// Log all outgoing packets
//		connection.addPacketInterceptor(new PacketInterceptor() {
//			@Override
//			public void interceptPacket(Packet packet) {
//				logger.log(Level.INFO, "Sent: {0}", packet.toXML());
//			}
//		}, new PacketTypeFilter(Message.class));
//
//		connection.login(username, password);
//	}

	public static void main(String[] args) {
		System.out.println("---------------------------------------------");
		System.out.println("Enter drücken, um die Anwendung zu schließen.");
		System.out.println("---------------------------------------------");

		Main app = new Main();

		try {
			System.in.read();
		} catch (Exception e) {
		} finally {
			exit(null);
		}

		/*try {

			try {
				ccsClient.connect(CSS_USERNAME + "@gcm.googleapis.com",
						CSS_PASSWORD);
			} catch (XMPPException e) {
				e.printStackTrace();
			}

			// Send a sample hello downstream message to a device.
			// String toRegId =
			// "APA91bGd6IEz6NUE7g3cOVwl3wYgwEdJ4T6OnqNRMVF44e-2ZBq7pursxmMhLf6OAp14yBqMITkDseLm8qJxY4EE0tswc4E9AinJdGLtOe7DrM8RPb5rqHGqoQy3hATn3S6c3px4os1ka1-WmfwTdtFmDGbVeku86Dznj-_E-52rXFfkwDaMyFk";
			// String toRegId =
			// "APA91bH2ZHtS1RNFYwWetjCReWdok5uky8Oe2ppAXw0B6fE-wwUiJ926yp8TDq90l2o3JlxrR9quSlYnn-QJTHkge5LTqQELztFKiDO90u5pRiDIvQBo2fVamJHo7dRq_pVAiYrmo5AfAGwknT8o534m8ltxEOPWsQ";
			String toRegId = "APA91bE5GOwn36c8XJzFC0Df0Xa6jNjNlTMAsQgu0JHSNegUK_tpLMVyaclD5n5cpr-BZywqRmKwyNnEHWICVl_NAheVGOiCYevUzv3beBP1fPwmfZKxNiE-p1kGbbtrDaIXnCX7KR8A8fI7KMXce5RN6c81n7x-KdI2pCQsJjyTTs4r5CsJRYI";
			String messageId = ccsClient.getRandomMessageId();

			GcmMessage message = new GcmMessage();
			message.setTo(toRegId);
			message.setMessageId(messageId);

			HashMap<String, Object> payload = new HashMap<String, Object>();
			payload.put("action", 1);
			payload.put("status", "successfull");

			message.setPayload(payload);

			if (message.isValid()) {
				ccsClient.send(message.toJson());
			} else {
				System.err.println("Message not valid");
			}

			System.out.println("Press any key to stop the service");
			System.in.read();
		} catch (Exception e) {
		} finally {
			System.out.println("Service killed");
		}*/

	}

	private static void exit(String message) {
		if (null == message) {
			System.err.println("Anwendung wurde beendet.");
			System.exit(0);
		} else {
			System.err.println("Anwendung auf Grund eines Fehlers beendet: " + message);
			System.exit(1);
		}
	}
}
