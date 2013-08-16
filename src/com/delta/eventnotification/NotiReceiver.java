package com.delta.eventnotification;

import java.util.HashMap;
import java.util.List;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotiReceiver extends BroadcastReceiver {
	static int c = 0;
	int pos;
	EventDb data;
	List<HashMap<String, String>> events;

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		data.open();
		events = data.getData();
		data.close();
		pos = arg1.getIntExtra("position", 0);
		NotificationCompat.Builder noti = new NotificationCompat.Builder(
				context).setContentTitle(events.get(pos).get("name"))
				.setContentText("Venue : " + events.get(pos).get("venue"))
				.setSmallIcon(R.drawable.ic_launcher);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(c, noti.build());
		c++;
	}

}
