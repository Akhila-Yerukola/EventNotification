package com.delta.eventnotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MainActivity extends Activity {
	LoadData objects;
	ImageLoader imageLoader;
	List<HashMap<String, String>> listOfEvents;
	DisplayImageOptions options;
	ListView eventList, notifList;
	Double[] lat= new Double[100];
	Double[] lng= new Double[100];
	Integer[] eid= new Integer[100];
	Integer[] uid= new Integer[100];
	static String[] flag = new String[100];
	String[] desc = new String[100];
	String[] name = new String[100];
	String[] date = new String[100];
	String[] pic = new String[100];
	String[] time = new String[100];
	String[] venue = new String[100];
	String[] edesc = new String[50];
	String[] ename = new String[50];
	String[] edate = new String[50];
	String[] etime = new String[50];
	String[] evenue = new String[50];
	String str,check;
	int position1, length=0, l;

	EventDb event;
	Integer[] icon = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher
			 };

	static class ViewHolder{
		ImageView icon;
		TextView nametext;
		TextView datetext;
		TextView timetext;
		TextView venuetext;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		objects = new LoadData();
		//http://10.0.2.2:8080
		objects.execute("http://10.0.2.2/NITTEvents/api/all.php?token=60ae136e5d49fbdf037fab5f1d805634");
		
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.common_signin_btn_icon_light)
		.showImageOnFail(R.drawable.common_signin_btn_text_normal_light)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		
		
		TabHost th = (TabHost) findViewById(R.id.tabhost);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Events");
		th.addTab(specs);
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Notifications");
		th.addTab(specs);
		event = new EventDb(MainActivity.this);
		listOfEvents = new ArrayList<HashMap<String, String>>();
		event.open();
		listOfEvents = event.getData();
		Log.e("length of notifications", Integer.toString(listOfEvents.size()));
		event.close();
		for (int i = 0; i < listOfEvents.size(); i++) {
			edesc[i] = listOfEvents.get(i).get("desc");
			ename[i] = listOfEvents.get(i).get("name");
			etime[i] = listOfEvents.get(i).get("time");
			edate[i] = listOfEvents.get(i).get("date");
			evenue[i] = listOfEvents.get(i).get("venue");
		}
		eventList = (ListView) findViewById(R.id.eventList);
		notifList = (ListView) findViewById(R.id.notiList);
		
//		ArrayAdapter<String> adapter = new MyCustomAdapter(MainActivity.this,
//				R.layout.row, name);
//		eventList.setAdapter(adapter);
//		
//			ArrayAdapter<String> adapter1 = new MyCustomAdapter1(MainActivity.this,
//					R.layout.row, ename);
//			notifList.setAdapter(adapter1);
//			Log.e("hema", "akhila");
		
		registerForContextMenu(eventList);
		registerForContextMenu(notifList);

		eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, Details.class);
				//details.putExtra("position", position);
				intent.putExtra("name", name[position]);
				intent.putExtra("desc", desc[position]);
				intent.putExtra("venue", venue[position]);
				intent.putExtra("date", date[position]);
				intent.putExtra("time", time[position]);
				Log.e("clicked position", Integer.toString(position));
				startActivity(intent);

			}
		});

		eventList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						position1 = arg2;
						//flag[arg2]="true";
						return false;

					}
				});

		notifList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						str = name[arg2];
						return false;

					}
				});

		notifList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent details = new Intent(MainActivity.this, Map.class);
				details.putExtra("position", position);
				startActivity(details);

			}
		});
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.eventList) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.details, menu);
		} else if (v.getId() == R.id.notiList) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.details1, menu);
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		// String[] names = getResources().getStringArray(R.array.names);
		switch (item.getItemId()) {
		case R.id.createNot:
			if(flag[position1]=="false"){
				flag[position1]="true";
				Intent intent = new Intent(MainActivity.this, Notification.class);
				intent.putExtra("name", name[position1]);
				intent.putExtra("desc", desc[position1]);
				intent.putExtra("venue", venue[position1]);
				intent.putExtra("date", date[position1]);
				intent.putExtra("time", time[position1]);

				startActivity(intent);
				
			}
			else{
				Toast.makeText(MainActivity.this, "Event has been already added!", Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
		case R.id.delete:
			Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
			AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);
			alertBox.setMessage("Do you really want to delete?");

			alertBox.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							event.open();
							event.delete(str);
							event.close();

							Toast.makeText(getApplicationContext(),
									"Data Deleted Successfully",
									Toast.LENGTH_LONG).show();

							// SQLView.this.onCreate(savedInstanceState);

							Intent i = new Intent(getBaseContext(),
									MainActivity.class);

							startActivity(i);

						}
					});

			alertBox.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(getApplicationContext(),
									"Deletion Canceled..", Toast.LENGTH_LONG)
									.show();
						}
					});

			alertBox.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public class MyCustomAdapter1 extends ArrayAdapter<String> {
		public int cnt =0;
		public MyCustomAdapter1(Context context, int textViewResourceId,
				String[] name) {
			super(context, textViewResourceId, name);
			Log.d("ADAPT", "Constructing");
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Inflate the layout, mainlvitem.xml, in each row.
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			View row1 = inflater.inflate(R.layout.row, parent, false);
			Log.d("ADAPT", Integer.toString(cnt++));

			// Declare and define the TextView, "item." This is where
			// the name of each item will appear.
//			TextView item = (TextView) row1.findViewById(R.id.Name);
//			item.setText(ename[position]);
//
//			TextView item1 = (TextView) row1.findViewById(R.id.Date);
//			item1.setText(edate[position]);
//			
//			TextView item2 = (TextView) row1.findViewById(R.id.Time);
//			item2.setText(etime[position]);
//
//			TextView item3 = (TextView) row1.findViewById(R.id.Venue);
//			item3.setText(evenue[position]);

//			ImageView iconview = (ImageView) row1.findViewById(R.id.icon);
			//iconview.setImageResource(icon[position]);
			ViewHolder holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.nametext = (TextView) convertView.findViewById(R.id.Name);
			holder.datetext = (TextView) convertView.findViewById(R.id.Date);
			holder.timetext = (TextView) convertView.findViewById(R.id.Time);
			convertView.setTag(holder);
			imageLoader.displayImage(pic[position], holder.icon, options);
			//Log.e("count",Integer.toString(position));
			return row1;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listOfEvents.size();
		}
	}

	public class MyCustomAdapter extends ArrayAdapter<String> {
		public MyCustomAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Inflate the layout, mainlvitem.xml, in each row.
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			View row1 = inflater.inflate(R.layout.row, parent, false);

			// Declare and define the TextView, "item." This is where
			// the name of each item will appear.
//			TextView item = (TextView) row1.findViewById(R.id.Name);
//			Log.e("name in adapter", name[position]);
//			item.setText(name[position]);
//
//			TextView item1 = (TextView) row1.findViewById(R.id.Date);
//			item1.setText(date[position]);
//			TextView item2 = (TextView) row1.findViewById(R.id.Time);
//			item2.setText(time[position]);
//
//			TextView item3 = (TextView) row1.findViewById(R.id.Venue);
			//item3.setText(venue[position]);

			// Declare and define the TextView, "icon." This is where
			// the icon in each row will appear.
			//ImageView iconview = (ImageView) row1.findViewById(R.id.icon);
			ViewHolder holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.nametext = (TextView) convertView.findViewById(R.id.Name);
			holder.datetext = (TextView) convertView.findViewById(R.id.Date);
			holder.timetext = (TextView) convertView.findViewById(R.id.Time);
			convertView.setTag(holder);
			imageLoader.displayImage(pic[position], holder.icon, options);
			
	//		iconview.setImageResource(icon[position]);
			//Log.e("count11",Integer.toString(position));
			return row1;
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return length;
		}
	}
		

	


	public class LoadData extends AsyncTask<String, Void, String> {

		private static final String TAG_CONTACTS = "data";
		List docList = new ArrayList();
		private static final String TAG_NAME = "ename";
		private static final String TAG_DATE = "edate";
		private static final String TAG_TIME = "etime";
		private static final String TAG_DESC = "edesc";
		private static final String TAG_VENUE = "evenue";
		private static final String TAG_LAT = "lat";
		private static final String TAG_PIC = "pic";
		private static final String TAG_LNG = "lng";
		private static final String TAG_EID = "eid";
		private static final String TAG_UID = "uid";
		JSONArray contacts = null;

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		protected void onPreExecute(String json) {
			// TODO Auto-generated method stub

			System.out.println("pre-execute");

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String url = urls[0];
			try {
				// defaultHttpClient

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				System.out.println("sb : " + sb);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("post-execute");
			try {
				System.out.println("post-execute");
				jObj = new JSONObject(json);

				contacts = jObj.getJSONArray("data");
				int i;
				for (i = 0; i < contacts.length(); i++) {
					JSONObject obj = contacts.getJSONObject(i);
					String ename = obj.getString(TAG_NAME);
					String edate = obj.getString(TAG_DATE);
					String etime = obj.getString(TAG_TIME);
					String edesc = obj.getString(TAG_DESC);
					String evenue = obj.getString(TAG_VENUE);
					String epic = obj.getString(TAG_PIC);
					Integer eeid = obj.getInt(TAG_EID);
					Integer euid = obj.getInt(TAG_UID);
					Double elat= obj.getDouble(TAG_LAT);
					Double elng= obj.getDouble(TAG_LNG);
					name[i]=ename;
					date[i]=edate;
					time[i]=etime;
					desc[i]=edesc;
					flag[i]="false";
					venue[i]=evenue;
					eid[i]=eeid;
					uid[i]=euid;
					lat[i]=elat;
					lng[i]=elng;
					pic[i]=epic;
				
					
					Log.e("name",name[i]);
			}
				length=i;
				Log.e("length of event list", Integer.toString(length));
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			ArrayAdapter<String> adapter = new MyCustomAdapter(MainActivity.this,
					R.layout.row, name);
			eventList.setAdapter(adapter);
			
				ArrayAdapter<String> adapter1 = new MyCustomAdapter1(MainActivity.this,
						R.layout.row, ename);
				notifList.setAdapter(adapter1);
				Log.e("hema", "akhila");

			
		}

	}
}
