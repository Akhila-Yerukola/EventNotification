package com.delta.eventnotification;

import java.util.HashMap;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class NotiReceiver extends BroadcastReceiver {
	static int c = 0;
	int pos, eid;
	Double lat, lng;
	Integer pic;
	List<HashMap<String, String>> events;
	String eName, eTime, eDate, eLoc, eDesc;
	NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent event) {
		// TODO Auto-generated method stub

		Log.e("reached", "msg");
		eName = event.getStringExtra("name");
		lat = event.getDoubleExtra("lat", 0);
		lng = event.getDoubleExtra("lng", 0);
		pic = event.getIntExtra("pic", R.drawable.ic_launcher);
		eLoc = event.getStringExtra("venue");
		eid = event.getIntExtra("eid", c);

		NotificationCompat.Builder noti = new NotificationCompat.Builder(
				context).setContentTitle(eName)
				.setContentText("Venue : " + eLoc);
				//.setSmallIcon(pic);

		Intent resultIntent = new Intent(context, Map.class);
		resultIntent.putExtra("lat", lat);
		resultIntent.putExtra("lng", lng);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		stackBuilder.addParentStack(Map.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_ONE_SHOT);
		noti.setContentIntent(resultPendingIntent);

		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(eid, noti.build());

		c++;
	}

}
