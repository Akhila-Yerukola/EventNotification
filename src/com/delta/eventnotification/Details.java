package com.delta.eventnotification;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class Details extends Activity {
	int position;
	TextView name, date, location, desc, time;
	EventDb data;
	List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Intent getDetails = getIntent();
		// position = getDetails.getIntExtra("position", 0);
		Intent event = getIntent();

		name = (TextView) findViewById(R.id.tvName);
		desc = (TextView) findViewById(R.id.tvDesc);
		time = (TextView) findViewById(R.id.tvTime);
		date = (TextView) findViewById(R.id.tvDate);
		location = (TextView) findViewById(R.id.tvVenue);
		name.setText(event.getStringExtra("name"));
		date.setText(event.getStringExtra("date"));
		time.setText(event.getStringExtra("time"));
		location.setText(event.getStringExtra("venue"));
		desc.setText(event.getStringExtra("desc"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
