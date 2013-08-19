package com.delta.eventnotification;

import java.util.HashMap;
import java.util.List;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotiReceiver extends BroadcastReceiver {
	static int c = 0;
	int pos;

	List<HashMap<String, String>> events;
	String eName, eTime, eDate, eLoc, eDesc;

	@Override
	public void onReceive(Context context, Intent event) {
		// TODO Auto-generated method stub

		Log.e("reached", "msg");
		eName = event.getStringExtra("name");

		eLoc = event.getStringExtra("venue");

		NotificationCompat.Builder noti = new NotificationCompat.Builder(
				context).setContentTitle(eName)
				.setContentText("Venue : " + eLoc)
				.setSmallIcon(R.drawable.ic_launcher);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(c, noti.build());
		c++;
	}

}
