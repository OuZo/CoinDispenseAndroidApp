package com.policron.fnbcoindispence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.AsyncTask;
import android.util.Log;

public class PayParking extends AsyncTask<String, Integer, String> {
	
	private static final String TAG = "PayParking";
	private int responce_code = -1;
	private String break_down = "";
	Long id = 0l;

	@Override
	protected String doInBackground(String... params) {
		
		JSONObject obj = new JSONObject();
		obj.put("amount_due", params[1]);
		obj.put("captured_amount", params[2]);
		obj.put("user_id", "1");
		
		try {
		URL url = new URL("http://" + params[0] + "/cash_dispenses.json");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter wr = new OutputStreamWriter(
				conn.getOutputStream());
		wr.write(obj.toJSONString());
		wr.flush();
		wr.close();
		
		responce_code = conn.getResponseCode();
		Log.i(TAG, "response_code = " + responce_code);

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			Object object = JSONValue.parse(decodedString);
			JSONObject jsonObject = (JSONObject) object;
			id = (Long)jsonObject.get("id");
			
//			JSONArray array = (JSONArray)jsonObject;
//			int index = array.indexOf("id");
//			id = (String)array.get(index);
			
			Log.i(TAG,"dddddddd = " +  decodedString);
			Log.i(TAG,"IDIDIDIDIDID = " +  id);
		}
		in.close();
		
		conn.disconnect();
		if ((responce_code == 302) || 
				(responce_code == 200) ||
				(responce_code == 201)
				) {
			// read the response - which is the break down
			url = new URL("http://" + params[0] + "/cash_dispenses/" + id + ".json");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			InputStreamReader rd = new InputStreamReader(conn.getInputStream());
			char[] buffer = new char[256];
			int bytes = rd.read(buffer);
			break_down = new String(buffer);
			conn.disconnect();
//			amount_due = Double.parseDouble(s);
		} 		
		
		} catch (MalformedURLException e) {
			Log.d(TAG, "MalformedURLException: " + e);
			e.printStackTrace();
		} catch (ProtocolException e) {
			Log.d(TAG, "ProtocolException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "IOException: " + e);
			e.printStackTrace();
		}
		
		return break_down;
		
	} // doInBackground

} // PayParking
