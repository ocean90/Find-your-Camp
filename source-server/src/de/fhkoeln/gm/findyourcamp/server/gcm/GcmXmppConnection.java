package de.fhkoeln.gm.findyourcamp.server.gcm;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.simple.JSONValue;

import de.fhkoeln.gm.findyourcamp.server.db.DbConnection;
import de.fhkoeln.gm.findyourcamp.server.utils.Logger;

public class GcmXmppConnection {

	public static final String GCM_SERVER = "gcm.googleapis.com";
	public static final int GCM_PORT = 5235;

	private static GcmXmppConnection instance;

	private XMPPConnection connection;
	private ConnectionConfiguration config;
	public String errorMessage = "";

	private GcmXmppConnection() {
		setConfig();
	}

	/**
	 * Singleton.
	 * Erzeugt genau eine Instanz des Objektes für diese Klasse.
	 *
	 * @return
	 */
	public static GcmXmppConnection getInstance() {
		if (GcmXmppConnection.instance == null) {
			GcmXmppConnection.instance = new GcmXmppConnection();
		}

		return GcmXmppConnection.instance;
	}

	private void setConfig() {
		config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
		config.setSecurityMode(SecurityMode.enabled);
		config.setReconnectionAllowed(true);
		config.setRosterLoadedAtLogin(false);
		config.setSendPresence(false);
		config.setSocketFactory(SSLSocketFactory.getDefault());
	}

	public boolean connect(String username, String password) {

		// Debug Mode.
		//config.setDebuggerEnabled(true);
		//XMPPConnection.DEBUG_ENABLED = true;

		connection = new XMPPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException e) {
			e.printStackTrace();

			switch ( e.getXMPPError().getCode() ) {
				case 504:
					this.errorMessage  = "Die Verbindung zum CCS konnte nicht aufgebaut werden.";
					break;
			}

			// Fehler: Verbindung fehlgeschlagen
			return false;
		}

		// Listener für eingehende Pakete.
		connection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Logger.log( "Nachricht empfangen:" + packet.toXML() );
				Message incomingMessage = (Message) packet;
				GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage
						.getExtension(GcmPacketExtension.GCM_NAMESPACE);

				String json = gcmPacket.getJson();
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> jsonObject = (Map<String, Object>) JSONValue
							.parseWithException(json);

					// Nachrichtentyp "ack"/"nack" oder null.
					Object messageType = jsonObject.get("message_type");

					if (null == messageType) {
						// Datennachricht
						handleIncomingDataMessage(jsonObject);

						// ACK an CCS senden
						String messageId = jsonObject.get("message_id")
								.toString();
						String from = jsonObject.get("from").toString();
						String ack = createJsonAck(from, messageId);
						sendPacket(ack);

					} else if ("ack".equals(messageType.toString())) {
						//  ACK
						handleAckReceipt(jsonObject);
					} else if ("nack".equals(messageType.toString())) {
						// NACK
						handleNackReceipt(jsonObject);
					} else {
						Logger.err("Unknown message type.");
						// Fehler: Unbekannter Typ.
					}
				} catch (Exception e) {
					e.printStackTrace();
					// Fehler: Allgemein.
				}
			}

			private void handleIncomingDataMessage(Map<String, Object> jsonObject) {
				MessageBroker mb = new MessageBroker(jsonObject);
				mb.handleRequest();
				// TODO MessageBroker
			}

			private void handleAckReceipt(Map<String, Object> jsonObject) {
				// TODO
			}

			private void handleNackReceipt(Map<String, Object> jsonObject) {
				// TODO
			}
		}, new PacketTypeFilter(Message.class));


		// Listener für ausgehende Pakete.
		connection.addPacketInterceptor(new PacketInterceptor() {
			@Override
			public void interceptPacket(Packet packet) {
				// TODO
			}
		}, new PacketTypeFilter(Message.class));


		try {
			connection.login(username, password);
		} catch (XMPPException e) {
			this.errorMessage = e.getMessage();

			// Fehler: Authentifizierung fehlgeschlagen.
			return false;
		}

		return true;
	}

	public void handleIncomingDataMessage(Map<String, Object> jsonObject) {
	}

	/**
	 * Creates a JSON encoded ACK message for an upstream message received from
	 * an application.
	 *
	 * @param to RegistrationId of the device who sent the upstream message.
	 * @param messageId  messageId of the upstream message to be acknowledged to CCS.
	 * @return JSON encoded ack.
	 */
	public String createJsonAck(String to, String messageId) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("message_type", "ack");
		message.put("to", to);
		message.put("message_id", messageId);
		return JSONValue.toJSONString(message);
	}

	/**
	 * Sendet eine Nachricht an ein Device. (Downstream)
	 */
	public void sendPacket(String jsonRequest) {
		Packet request = new GcmPacketExtension(jsonRequest).toPacket();
		connection.sendPacket(request);
	}
}
