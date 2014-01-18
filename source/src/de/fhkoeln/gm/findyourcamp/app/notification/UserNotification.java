package de.fhkoeln.gm.findyourcamp.app.notification;

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

	public UserNotification(Context context) {
		this.appContext = context;

		notificationManager = (NotificationManager)
				 context.getSystemService(Context.NOTIFICATION_SERVICE);

		pendingIntent = PendingIntent.getActivity(appContext, 0, new Intent(appContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void show(String title, String text, String ticker) {

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
        .setContentTitle(title)
        .setContentText(text)
        .setTicker(ticker)
        .setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);

		notificationManager.notify(0, notificationBuilder.build());
	}
}
