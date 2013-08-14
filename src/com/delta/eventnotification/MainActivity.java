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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {
	LoadData objects;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
