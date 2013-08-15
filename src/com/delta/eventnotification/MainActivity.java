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
import android.content.Context;
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
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	LoadData objects;
	ListView eventList,notifList;
	String[] name = {"Event1","Event2"};
	String[] date = {"14.08.2013","15.08.2013"};
	String[] time = {"5 PM", "6 pM"};
	String[] venue = {"LHC", "Admin"};
	String str;
	EventDb event;
	Integer[] icon = {R.drawable.ic_launcher,R.drawable.ic_launcher};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		eventList=(ListView)findViewById(R.id.eventList);
		notifList=(ListView)findViewById(R.id.notiList);
		
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
		objects = new LoadData();
		objects.execute("http://10.0.2.2:8080");
		ArrayAdapter<String> adapter = new MyCustomAdapter(this, R.layout.row, name);
	    eventList.setAdapter(adapter);
	    ArrayAdapter<String> adapter1 = new MyCustomAdapter1(this, R.layout.rownoti, name);
	    notifList.setAdapter(adapter1);
	    registerForContextMenu(eventList);
	    registerForContextMenu(notifList);
	    
	    notifList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {  

  		  @Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
  			 str= name[arg2];
			return false;
		
  		  }
  		  });  
  	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      if(v.getId()==R.id.eventList){
	    	  MenuInflater inflater = getMenuInflater();
		      inflater.inflate(R.menu.details, menu);
	      }
	      else if(v.getId()==R.id.notiList){
	    	  MenuInflater inflater = getMenuInflater();
		      inflater.inflate(R.menu.details1, menu);
	      }
	      
	    }
	public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      event=new EventDb(this);
	      //String[] names = getResources().getStringArray(R.array.names);
	      switch(item.getItemId()) {
	      case R.id.createNot:
	            Intent intent= new Intent(MainActivity.this,Notification.class);
	            startActivity(intent);
	            return true;
	      case R.id.delete:
	    	  Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
	    	  event.open();
	    	  event.delete(str);
	    	  event.close();
	    	  return true;
	      default:
	            return super.onContextItemSelected(item);
	      }
	      }
	
	public class MyCustomAdapter extends ArrayAdapter<String> 
    {
        public MyCustomAdapter(Context context, int textViewResourceId, String[] objects) 
        {
            super(context, textViewResourceId, objects);
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Inflate the layout, mainlvitem.xml, in each row.
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row1 = inflater.inflate(R.layout.row, parent, false);
 
            // Declare and define the TextView, "item." This is where
            // the name of each item will appear.
            TextView item = (TextView)row1.findViewById(R.id.Name);
            item.setText(name[position]);
            
            TextView item1 = (TextView)row1.findViewById(R.id.Date);
            item1.setText(date[position]);
            
            TextView item2 = (TextView)row1.findViewById(R.id.Time);
            item2.setText(time[position]);
            
            TextView item3 = (TextView)row1.findViewById(R.id.Venue);
            item3.setText(venue[position]);
 
            // Declare and define the TextView, "icon." This is where
            // the icon in each row will appear.
            ImageView iconview=(ImageView)row1.findViewById(R.id.icon);
            iconview.setImageResource(icon[position]);
 
            return row1;
        }
    }
	
	public class MyCustomAdapter1 extends ArrayAdapter<String> 
    {
        public MyCustomAdapter1(Context context, int textViewResourceId, String[] objects) 
        {
            super(context, textViewResourceId, objects);
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Inflate the layout, mainlvitem.xml, in each row.
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row1 = inflater.inflate(R.layout.rownoti, parent, false);
 
            // Declare and define the TextView, "item." This is where
            // the name of each item will appear.
            TextView item = (TextView)row1.findViewById(R.id.Name);
            item.setText(name[position]);
            
            TextView item1 = (TextView)row1.findViewById(R.id.Date);
            item1.setText(date[position]);
            
            TextView item3 = (TextView)row1.findViewById(R.id.Venue);
            item3.setText(venue[position]);
 
            // Declare and define the TextView, "icon." This is where
            // the icon in each row will appear.
            ImageView iconview=(ImageView)row1.findViewById(R.id.icon);
            iconview.setImageResource(icon[position]);
 
            return row1;
        }
    }
	public class LoadData extends AsyncTask<String, Void, String> {

		private static final String TAG_CONTACTS = "events";
		List docList = new ArrayList();
		private static final String TAG_NAME = "Name";
		private static final String TAG_DATE = "date";
		private static final String TAG_TIME = "time";
		private static final String TAG_DESC = "desc";
		private static final String TAG_VENUE = "venue";
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

				contacts = jObj.getJSONArray("contacts");

				for (int i = 0; i < contacts.length(); i++) {
					JSONObject obj = contacts.getJSONObject(i);
					String name = obj.getString(TAG_NAME);
					String date = obj.getString(TAG_DATE);
					String time = obj.getString(TAG_TIME);
					String desc = obj.getString(TAG_DESC);
					String venue = obj.getString(TAG_VENUE);

					HashMap temp = new HashMap();
					temp.put("Name", name);
					temp.put("Date", date);
					temp.put("Time", time);
					temp.put("Desc", desc);
					temp.put("Venue", venue);

					docList.add(temp);

				}
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

//			LoadData obj = new LoadData();
//			obj.execute("http://10.0.2.2:8080");

		}

	}
}

