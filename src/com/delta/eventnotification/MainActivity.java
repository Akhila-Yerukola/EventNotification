package com.delta.eventnotification;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import universalImageLoader.com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import universalImageLoader.com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import universalImageLoader.com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import universalImageLoader.com.nostra13.universalimageloader.core.DisplayImageOptions;
import universalImageLoader.com.nostra13.universalimageloader.core.ImageLoader;
import universalImageLoader.com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import universalImageLoader.com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import universalImageLoader.com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import universalImageLoader.com.nostra13.universalimageloader.utils.StorageUtils;

public class MainActivity extends Activity {

	LoadData objects;
	ImageLoader imageLoader;
	ImageLoaderConfiguration config;
	List<HashMap<String, String>> listOfEvents;
	DisplayImageOptions options;
	ListView notifList;
	GridView eventList;
	ArrayList<Double> lat = new ArrayList<Double>();
	ArrayList<Double> lng = new ArrayList<Double>();
	ArrayList<Integer> eid = new ArrayList<Integer>();
	ArrayList<Integer> uid = new ArrayList<Integer>();
	static ArrayList<String> flag = new ArrayList<String>();
	static ArrayList<Integer> flagForEvent = new ArrayList<Integer>();
	ArrayList<String> desc = new ArrayList<String>();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> pic = new ArrayList<String>();
	ArrayList<String> time = new ArrayList<String>();
	ArrayList<String> venue = new ArrayList<String>();
	ArrayList<String> edesc = new ArrayList<String>();
	ArrayList<String> ever = new ArrayList<String>();
	ArrayList<String> elat = new ArrayList<String>();
	ArrayList<String> elng = new ArrayList<String>();
	ArrayList<String> ename = new ArrayList<String>();
	ArrayList<String> edate = new ArrayList<String>();
	ArrayList<String> etime = new ArrayList<String>();
	ArrayList<String> evenue = new ArrayList<String>();
	ArrayList<Integer> ver = new ArrayList<Integer>();
	String str, check;
	SharedPreferences alarm, init;
	int position1, more = 0, posOfNoti, a = 0, pos;
	int pageNo = 0, length = 0;

	public static ProgressDialog progresstwig = null;

	EventDb event;
	FlagDb flagCheck;
	Integer[] icon = { R.drawable.fest, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };

	static class ViewHolder {
		ImageView image;
		TextView text;
		TextView datetext;
		TextView timetext;
		TextView venuetext;

	}

	PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		objects = new LoadData();

		// alarm = getSharedPreferences("AlarmTracker", MODE_PRIVATE);
		// Log.e("alarm check before", alarm.getString("check", "No"));
		// if (alarm.getString("check", "No") == "No") {
		// Log.e("check for alarm to be set 12 hours", "Entered!!");
		// Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
		//
		// pendingIntent = PendingIntent.getService(MainActivity.this, 0,
		// myIntent, 0);
		//
		// AlarmManager alarmManager = (AlarmManager)
		// getSystemService(ALARM_SERVICE);
		// alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// Calendar.getInstance().getTimeInMillis(),
		// 12 * 60 * 60 * 1000, pendingIntent);
		//
		// }
		//
		// SharedPreferences.Editor editor = alarm.edit();
		// editor.putString("check", "Set");
		// editor.commit();
		// http://10.0.2.2:8080
		// objects.execute("http://10.0.2.2/NITTEvents/api/all.php?token=60ae136e5d49fbdf037fab5f1d805634&page="
		// + (pageNo++) + "&ipp=5");
		length = 0;
		pageNo = 0;
		objects.execute("http://10.0.2.2/NITTEvents/api/all.php?token=60ae136e5d49fbdf037fab5f1d805634&page="
				+ (pageNo++) + "&ipp=5");
		// objects.execute("http://www.pragyan.org/festember/events/api/all.php?token=94a08da1fecbb6e8b46990538c7b50b2&page="
		// + (pageNo++) + "&ipp=5");
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).build();
		File cacheDir = StorageUtils.getCacheDirectory(this);
		config = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(480, 800)
				.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(new BaseImageDownloader(this))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
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
		flagCheck = new FlagDb(MainActivity.this);

		eventList = (GridView) findViewById(R.id.grid);
		notifList = (ListView) findViewById(R.id.notiList);

		// ArrayAdapter<String> adapter = new MyCustomAdapter(MainActivity.this,
		// R.layout.row, name);
		// eventList.setAdapter(adapter);
		//
		// ArrayAdapter<String> adapter1 = new
		// MyCustomAdapter1(MainActivity.this,
		// R.layout.row, ename);
		// notifList.setAdapter(adapter1);
		// Log.e("hema", "akhila");

		registerForContextMenu(eventList);
		registerForContextMenu(notifList);

		eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position < length) {
					Intent intent = new Intent(MainActivity.this, Details.class);
					// details.putExtra("position", position);
					intent.putExtra("name", name.get(position));
					intent.putExtra("desc", desc.get(position));
					intent.putExtra("venue", venue.get(position));
					intent.putExtra("date", date.get(position));
					intent.putExtra("time", time.get(position));
					intent.putExtra("pic", pic.get(position));
					Log.e("clicked position", Integer.toString(position));
					startActivity(intent);
				}
				if (position == length) {
					LoadData objects2 = new LoadData();
					// http://10.0.2.2:8080
					objects2.execute("http://10.0.2.2/NITTEvents/api/all.php?token=60ae136e5d49fbdf037fab5f1d805634&page="
							+ (pageNo++) + "&ipp=5");

					// objects1.execute("http://www.pragyan.org/festember/events/api/all.php?token=94a08da1fecbb6e8b46990538c7b50b2&page="
					// + (pageNo++) + "&ipp=5");
				}

			}
		});

		eventList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						// flagForEvent.add(a++, arg2);
						position1 = arg2;
						Log.e("Postion being clicked",
								Integer.toString(position1));
						// flag[arg2]="true";
						return false;

					}
				});

		notifList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						pos = arg2;
						// str = name.get(arg2);
						return false;

					}
				});

		notifList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent details = new Intent(MainActivity.this, Map.class);
				details.putExtra("lat", elat.get(position));
				details.putExtra("lng", elng.get(position));
				startActivity(details);

			}
		});
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.grid) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.details, menu);
		} else if (v.getId() == R.id.notiList) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.details1, menu);
		}

	}

	void check() {

		Intent intent = new Intent(MainActivity.this, Notification.class);
		intent.putExtra("name", name.get(position1));
		intent.putExtra("desc", desc.get(position1));
		intent.putExtra("venue", venue.get(position1));
		intent.putExtra("date", date.get(position1));
		intent.putExtra("time", time.get(position1));
		intent.putExtra("lat", lat.get(position1));
		intent.putExtra("lng", lng.get(position1));
		intent.putExtra("eid", eid.get(position1));
		intent.putExtra("ver", ver.get(position1));
		intent.putExtra("status", "set");
		// intent.putExtra("pos", position1);
		// intent.putExtra("pic", pic.get(position1));

		startActivity(intent);

	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Calendar current = Calendar.getInstance();
		int yr = current.get(Calendar.YEAR);
		int mon = current.get(Calendar.MONTH);
		int day = current.get(Calendar.DAY_OF_MONTH);
		int date1 = Integer.parseInt(date.get(position1).substring(8));
		int month1 = Integer.parseInt(date.get(position1).substring(5, 7));
		int year1 = Integer.parseInt(date.get(position1).substring(0, 4));

		// String[] names = getResources().getStringArray(R.array.names);
		switch (item.getItemId()) {
		case R.id.createNot:
			flagCheck.open();
			String res = flagCheck.getIds();
			String[] list = res.split(";");
			flagCheck.close();
			int f = 0;
			for (int i = 0; i < list.length; i++) {
				if (Integer.toString(eid.get(position1)).equals(list[i])) {
					f = 1;
					break;
				}
			}

			if (f == 0) {
				// Log.e("flag of event", flag.get(position1));
				flagCheck.open();
				flagCheck.createEntry(Integer.toString(eid.get(position1)));
				flagCheck.close();
				if (yr == year1) {
					if (mon == month1 - 1) {
						if (day <= date1) {
							check();
						}
					} else if (mon < month1 - 1) {
						check();
					}
				} else if (yr < year1) {
					check();
				} else
					Toast.makeText(this, "Date already passed!",
							Toast.LENGTH_SHORT).show();
			} else if (f == 1) {
				Toast.makeText(MainActivity.this,
						"Event has been already added!", Toast.LENGTH_SHORT)
						.show();
			}

			return true;
		case R.id.delete:
			Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
			AlertDialog.Builder alertBox = new AlertDialog.Builder(
					MainActivity.this);
			alertBox.setMessage("Do you really want to delete?");

			alertBox.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							event.open();
							for (int i = 0; i < name.size(); i++) {
								if (name.get(i).toUpperCase()
										.equals(ename.get(pos))) {
									posOfNoti = i;

									break;
								}
							}
							Log.d("data", name.get(posOfNoti));
							event.delete(name.get(posOfNoti));
							event.close();

							Toast.makeText(getApplicationContext(),
									"Data Deleted Successfully",
									Toast.LENGTH_LONG).show();

							for (int i = 0; i < name.size(); i++) {
								if (name.get(i).toUpperCase()
										.equals(ename.get(pos))) {
									posOfNoti = i;

									break;
								}
							}

							// SQLView.this.onCreate(savedInstanceState);
							cancelAlarm(posOfNoti);
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

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.load, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case R.id.load:

			LoadData objects1 = new LoadData();
			// http://10.0.2.2:8080
			length = 0;
			objects1.execute("http://10.0.2.2/NITTEvents/api/all.php?token=60ae136e5d49fbdf037fab5f1d805634&page=0&ipp=5");

			// objects1.execute("http://www.pragyan.org/festember/events/api/all.php?token=94a08da1fecbb6e8b46990538c7b50b2&page="
			// + (pageNo++) + "&ipp=5");

			break;

		}

		return false;
	}

	public class MyCustomAdapter1 extends ArrayAdapter<String> {
		public int cnt = 0;

		public MyCustomAdapter1(Context context, int textViewResourceId,
				ArrayList<String> ename) {
			super(context, textViewResourceId, ename);
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
			TextView item = (TextView) row1.findViewById(R.id.Name);
			item.setText(ename.get(position));
			//
			TextView item1 = (TextView) row1.findViewById(R.id.Date);
			item1.setText(edate.get(position));
			//
			TextView item2 = (TextView) row1.findViewById(R.id.Time);
			item2.setText(etime.get(position));
			//
			TextView item3 = (TextView) row1.findViewById(R.id.Venue);
			item3.setText(evenue.get(position));

			ImageView iconview = (ImageView) row1.findViewById(R.id.Icon);
			// imageLoader.init(config);
			imageLoader.displayImage(pic.get(position), iconview, options);
			// iconview.setImageResource(R.drawable.common_signin_btn_icon_dark);
			// ViewHolder holder = new ViewHolder();
			// holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			// holder.nametext = (TextView) convertView.findViewById(R.id.Name);
			// holder.datetext = (TextView) convertView.findViewById(R.id.Date);
			// holder.timetext = (TextView) convertView.findViewById(R.id.Time);
			// convertView.setTag(holder);
			// imageLoader.displayImage(pic[position], holder.icon, options);
			// Log.e("count",Integer.toString(position));
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
				ArrayList<String> name) {
			super(context, textViewResourceId, name);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Inflate the layout, mainlvitem.xml, in each row.
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			View row1 = inflater.inflate(R.layout.grid, parent, false);

			TextView item = (TextView) row1.findViewById(R.id.Name);
			Log.e("name in adapter", name.get(position));
			item.setText(name.get(position));
			ImageView iconView = (ImageView) row1.findViewById(R.id.Pic);
			// iconView.setImageResource(R.drawable.common_signin_btn_icon_dark);

			// View view = convertView;
			// final ViewHolder holder;
			// if (convertView == null) {
			// view = getLayoutInflater().inflate(R.layout.grid, parent, false);
			// holder = new ViewHolder();
			// holder.text = (TextView) view.findViewById(R.id.Name);
			// holder.image = (ImageView) view.findViewById(R.id.Pic);
			// view.setTag(holder);
			// } else {
			// holder = (ViewHolder) view.getTag();
			// }

			// holder.text.setText(name.get(position));

			System.out.println(pic.get(position));
			if (position != length) {
				imageLoader.init(config);
				imageLoader.displayImage(pic.get(position), iconView, options);
			}
			if (position == length && more != 1)
				iconView.setImageResource(R.drawable.add_new);

			return row1;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return length + 1;
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
		private static final String TAG_VER = "ver";
		JSONArray contacts = null;

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progresstwig = ProgressDialog.show(MainActivity.this, "",
					"Loading...");
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
			progresstwig.dismiss();
			try {
				System.out.println("post-execute");
				jObj = new JSONObject(json);

				contacts = jObj.getJSONArray("data");
				int i, c = 0;
				for (i = length; i < length + contacts.length(); i++) {
					JSONObject obj = contacts.getJSONObject(c++);
					String ename = obj.getString(TAG_NAME);
					String edate = obj.getString(TAG_DATE);
					String etime = obj.getString(TAG_TIME);
					String edesc = obj.getString(TAG_DESC);
					String evenue = obj.getString(TAG_VENUE);
					String epic = obj.getString(TAG_PIC);
					Integer eeid = obj.getInt(TAG_EID);
					Integer euid = obj.getInt(TAG_UID);
					Integer ever = obj.getInt(TAG_VER);
					Double elat = obj.getDouble(TAG_LAT);
					Double elng = obj.getDouble(TAG_LNG);
					name.add(i, ename);
					date.add(i, edate);
					time.add(i, etime);
					desc.add(i, edesc);
					ver.add(i, ever);
					venue.add(i, evenue);
					eid.add(i, eeid);
					uid.add(i, euid);
					lat.add(i, elat);
					lng.add(i, elng);
					pic.add(i, epic);

					Log.e("name", name.get(i));
				}
				if (contacts.length() > 0) {
					length = i;
					name.add(i, "More");
					pic.add(i, Integer.toString(R.drawable.logo));
				} else if (contacts.length() == 0 && more != 1) {
					more = 1;
					length = i - 1;
				}
				Log.e("length of event list", Integer.toString(length));
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			listOfEvents = new ArrayList<HashMap<String, String>>();
			event.open();
			listOfEvents = event.getData();
			Log.e("length of notifications",
					Integer.toString(listOfEvents.size()));
			event.close();

			for (int i = 0; i < listOfEvents.size(); i++) {
				edesc.add(i, listOfEvents.get(i).get("desc"));
				ename.add(i, listOfEvents.get(i).get("name"));
				etime.add(i, listOfEvents.get(i).get("time"));
				edate.add(i, listOfEvents.get(i).get("date"));
				evenue.add(i, listOfEvents.get(i).get("venue"));
				ever.add(i, listOfEvents.get(i).get("ver"));
				elat.add(i, listOfEvents.get(i).get("lat"));
				elng.add(i, listOfEvents.get(i).get("lng"));
				Log.e("ver", ever.get(i));
				Log.e("time", etime.get(i));
			}
			for (int i = 0; i < listOfEvents.size(); i++) {
				for (int j = 0; j < length + 1; j++) {
//					Log.d("INside Loop", ename.get(i) + ";"
//							+ name.get(j).toUpperCase() + ";" + ever.get(i)
//							+ ";" + ver.get(j));
					if (ename.get(i).equals(name.get(j).toUpperCase())
							&& !ever.get(i)
									.equals(Integer.toString(ver.get(j)))) {
						event.open();
						event.update(ename.get(i),
								Integer.toString(ver.get(j)), time.get(j),
								Double.toString(lat.get(j)),
								Double.toString(lng.get(j)));
						event.close();
						if (!etime.get(i).equals(time.get(j))) {
							cancelAlarm(j);
							resetAlarm(j);
						}
						Log.e("Ver in loop", Integer.toString(ver.get(j)));
						ever.add(i, Integer.toString(ver.get(j)));
						elat.add(i, Double.toString(lat.get(j)));
						elng.add(i, Double.toString(lng.get(j)));
						etime.add(i, time.get(j));

					}
				}

			}

			ArrayAdapter<String> adapter = new MyCustomAdapter(
					MainActivity.this, R.layout.grid, name);
			eventList.setAdapter(adapter);

			ArrayAdapter<String> adapter1 = new MyCustomAdapter1(
					MainActivity.this, R.layout.row, ename);
			notifList.setAdapter(adapter1);
			Log.e("hema ", "akhila");

		}

	}

	public void resetAlarm(int j) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, NotiReceiver.class);
		intent.putExtra("name", name.get(j));
		intent.putExtra("venue", venue.get(j));
		intent.putExtra("lat", lat.get(j));
		intent.putExtra("lng", lng.get(j));
		// intent.putExtra("pic", ePic);
		intent.putExtra("eid", eid.get(j));
		int hr = Integer.parseInt(time.get(j).substring(0, 2));
		int min = Integer.parseInt(time.get(j).substring(3, 5));
		int dates = Integer.parseInt(date.get(j).substring(8));
		int month = Integer.parseInt(date.get(j).substring(5, 7));
		int year = Integer.parseInt(date.get(j).substring(0, 4));

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainActivity.this, eid.get(j), intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		Log.e("hour", Integer.toString(hr - 1));
		Log.e("min", Integer.toString(min));
		Log.e("date", Integer.toString(dates));
		Log.e("month", Integer.toString(month));
		Log.e("year", Integer.toString(year));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, dates);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		// calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.HOUR_OF_DAY, hr - 1);
		calendar.set(Calendar.MINUTE, min);

		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingIntent);
		Log.d("in reset", "reset");

	}

	public void cancelAlarm(int j) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, NotiReceiver.class);
		intent.putExtra("name", name.get(j));
		intent.putExtra("venue", venue.get(j));
		intent.putExtra("lat", lat.get(j));
		intent.putExtra("lng", lng.get(j));
		// intent.putExtra("pic", ePic);
		intent.putExtra("eid", eid.get(j));
		int hr = Integer.parseInt(time.get(j).substring(0, 2));
		int min = Integer.parseInt(time.get(j).substring(3, 5));
		int dates = Integer.parseInt(date.get(j).substring(8));
		int month = Integer.parseInt(date.get(j).substring(5, 7));
		int year = Integer.parseInt(date.get(j).substring(0, 4));

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainActivity.this, eid.get(j), intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		Log.e("hour", Integer.toString(hr - 1));
		Log.e("min", Integer.toString(min));
		Log.e("date", Integer.toString(dates));
		Log.e("month", Integer.toString(month));
		Log.e("year", Integer.toString(year));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, dates);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		// calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.HOUR_OF_DAY, hr - 1);
		calendar.set(Calendar.MINUTE, min);

		alarmManager.cancel(pendingIntent);
		Log.d("in cancel alarm", "cancelled");
	}
}
