package com.delta.eventnotification;

import java.util.HashMap;
import java.util.List;

import universalImageLoader.com.nostra13.universalimageloader.core.DisplayImageOptions;
import universalImageLoader.com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends Activity {
	int position;
	TextView name, date, location, desc, time;
	EventDb data;
	List<HashMap<String, String>> list;
	ImageView pic ;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Intent getDetails = getIntent();
		imageLoader = ImageLoader.getInstance();
		// position = getDetails.getIntExtra("position", 0);
		Intent event = getIntent();
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.logo)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).build();
		pic = (ImageView) findViewById(R.id.imageView1);
		name = (TextView) findViewById(R.id.tvName);
		desc = (TextView) findViewById(R.id.tvDesc);
		time = (TextView) findViewById(R.id.tvTime);
		date = (TextView) findViewById(R.id.tvDate);
		location = (TextView) findViewById(R.id.tvVenue);
		imageLoader.displayImage(event.getStringExtra("pic"), pic, options);
		//pic.setImageResource(event.getIntExtra("pic", R.drawable.ic_launcher));
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
