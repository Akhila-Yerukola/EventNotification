package com.delta.eventnotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import universalImageLoader.com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Notification extends Activity implements OnClickListener,
		OnItemSelectedListener {

	TextView name, venue, dateNtime;
	Button save, cancel;
	EventDb data;
	String eName, eTime, eDate, eLoc, eDesc, timeToBeSet;
	Integer ePic;
	String[] choices = { "1 hour before", "2 hours before" };
	Spinner spinner;
	List events = new ArrayList();
	int length, hr, min, date, month, year ;
	Double lat, lng;
	static int c = 0;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);

		data = new EventDb(this);
		name = (TextView) findViewById(R.id.tvName);
		venue = (TextView) findViewById(R.id.tvVenue);
		dateNtime = (TextView) findViewById(R.id.tvDnT);
		save = (Button) findViewById(R.id.bSave);
		cancel = (Button) findViewById(R.id.bCancel);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, choices);
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(adapter);
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.logo)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).build();
		Intent event = getIntent();

		eName = event.getStringExtra("name");
		eTime = event.getStringExtra("time");
		eDate = event.getStringExtra("date");
		eLoc = event.getStringExtra("venue");
		eDesc = event.getStringExtra("desc");
		lat=event.getDoubleExtra("lat", 0);
		lng=event.getDoubleExtra("lng", 0);
		//pic=event.getIntExtra("pic", 0);
		ePic= event.getIntExtra("pic", R.drawable.ic_launcher);
		// data.open();
		// Cursor cursor = data.getDetails(eventName);
		// data.close();
		// if (cursor != null) {
		//
		// while (cursor.moveToFirst()) {
		//
		// eName = (cursor.getString(cursor.getColumnIndex("_name")));
		// eTime = (cursor.getString(cursor.getColumnIndex("_time")));
		// eDate = (cursor.getString(cursor.getColumnIndex("_date")));
		// eLoc = (cursor.getString(cursor.getColumnIndex("_venue")));
		//
		// }
		// }
		name.setText(eName);
		hr = Integer.parseInt(eTime.substring(0, 2));
		min = Integer.parseInt(eTime.substring(3, 5));
		date = Integer.parseInt(eDate.substring(8));
		month = Integer.parseInt(eDate.substring(5, 7));
		year = Integer.parseInt(eDate.substring(0,4));

		venue.setText(eLoc);
		dateNtime.setText(eDate + "  " + eTime);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Notification.this, MainActivity.class);
		switch (v.getId()) {
		case R.id.bSave:
			boolean work = true;
			try {
				data.open();
				data.createEntry(eName, eDate, eTime, eDesc, eLoc);
				data.close();
			} catch (Exception e) {
				work = false;
				Toast.makeText(this, "Event Additon failed!",
						Toast.LENGTH_SHORT).show();
			}
			if (work == true) {

				data.open();
				events = data.getData();
				data.close();
				length = events.size();

				Intent intent = new Intent(Notification.this,
						NotiReceiver.class);
				intent.putExtra("name", eName);
				intent.putExtra("venue", eLoc);
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("pic", ePic);

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						Notification.this, c++, intent,
						Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.e("hour",
						Integer.toString(hr - Integer.parseInt(timeToBeSet)));
				Log.e("min", Integer.toString(min));
				Log.e("date", Integer.toString(date));
				Log.e("month", Integer.toString(month));
				Log.e("year", Integer.toString(year));
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, date);
				calendar.set(Calendar.MONTH, month-1);
				calendar.set(Calendar.YEAR, 2013);
				//calendar.set(Calendar.HOUR_OF_DAY, 11);
				 calendar.set(Calendar.HOUR_OF_DAY,
				 hr-Integer.parseInt(timeToBeSet));
				calendar.set(Calendar.MINUTE, min);
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pendingIntent);
				Toast.makeText(this, "Event Added!", Toast.LENGTH_SHORT).show();
			}

			// Should add notification alarm setter here!
			startActivity(i);

			break;

		case R.id.bCancel:
			Toast.makeText(this, "Event Addition Cancelled!",
					Toast.LENGTH_SHORT).show();
			startActivity(i);
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		int s = spinner.getSelectedItemPosition();
		switch (s) {
		case 0:
			timeToBeSet = "1";
			break;
		case 1:
			timeToBeSet = "2";
			break;

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		timeToBeSet = "1";

	}

}
