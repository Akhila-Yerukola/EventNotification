package com.delta.eventnotification;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	String eName, eTime, eDate, eLoc, eDesc,timeToBeSet;
	String[] choices = { "30 minutes before", "1 hour before", "2 hours before" };
	Spinner spinner;

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
		Intent event = getIntent();

		eName = event.getStringExtra("name");
		eTime = event.getStringExtra("time");
		eDate = event.getStringExtra("date");
		eLoc = event.getStringExtra("venue");
		eDesc = event.getStringExtra("desc");
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
			timeToBeSet = "30";
			break;
		case 1:
			timeToBeSet = "1";
			break;
		case 2:
			timeToBeSet = "2";
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		timeToBeSet = "30";

	}

}
