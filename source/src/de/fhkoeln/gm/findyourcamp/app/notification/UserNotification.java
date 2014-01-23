package de.fhkoeln.gm.findyourcamp.app.notification;

import java.util.Random;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import de.fhkoeln.gm.findyourcamp.app.MainActivity;
import de.fhkoeln.gm.findyourcamp.app.R;

public class UserNotification {

	private Context appContext;
	private NotificationManager notificationManager;
	private PendingIntent pendingIntent;
	private Random generator;

	public UserNotification( Context context ) {
		this.appContext = context;

		generator = new Random();

		notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );

		pendingIntent = PendingIntent.getActivity( appContext, 0, new Intent( appContext, MainActivity.class ),
				PendingIntent.FLAG_UPDATE_CURRENT );
	}

	public void show( String title, String text, String ticker ) {

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( appContext )
				.setContentTitle( title ).setContentText( text ).setTicker( ticker )
				.setSmallIcon( R.drawable.ic_stat_gcm ).setContentIntent( pendingIntent ).setAutoCancel( true );

		notificationManager.notify( generator.nextInt(), notificationBuilder.build() );
	}
}
