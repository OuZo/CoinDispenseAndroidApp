package com.policron.fnbcoindispence;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class GetAmountDue extends AsyncTask<String, Double, Double> {

	private static final String TAG = "GetAmountDues";
	private int responce_code = -1;
	private double amount_due = -1;
	
	@Override
	protected Double doInBackground(String... params) {
		try {
			URL url = new URL("http://" + params[0] + "/cash_dispenses/new.json");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			InputStreamReader rd = new InputStreamReader(conn.getInputStream());
			char[] buffer = new char[256];
			int bytes = rd.read(buffer);
			String s = new String(buffer);
			amount_due = Double.parseDouble(s);

			responce_code = conn.getResponseCode();
			
			Log.i(TAG, "response_code = " + responce_code);

			conn.disconnect();
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

		return amount_due;

	} // doInBackground
	
	protected Double onPostExecute() {
		return amount_due;
    } // onPostExecute

} // GetAmountDue
