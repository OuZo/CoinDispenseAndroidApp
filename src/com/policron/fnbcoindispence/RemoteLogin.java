package com.policron.fnbcoindispence;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class RemoteLogin extends AsyncTask<String, Integer, Integer> {

	private static final String TAG = "RemoteLogin";
	private String jsonText = "";
	private int responce_code = -1;
	
	@Override
	protected Integer doInBackground(String... arg0) {
		
		jsonText = "{\"user\":{\"email\":\"" + arg0[1] + "\"," 
                + "\"password\":\"" + arg0[2] + "\"}}";
		
		try {
		URL url = new URL("http://" + arg0[0] + "/users/sign_in.json");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter wr = new OutputStreamWriter(
				conn.getOutputStream());
		wr.write(jsonText);
		wr.flush();
		wr.close();
		
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
		
		return responce_code;
	} // doInBackground
	
	protected Integer onPostExecute() {
		return responce_code;
    } // onPostExecute

	
} // RemoteConnect
