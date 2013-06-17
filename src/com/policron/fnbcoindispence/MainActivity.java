package com.policron.fnbcoindispence;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText et_Email, et_Password = null;
	private Button btnLogin = null;

	private TextView tvAmountDue = null;
	private EditText etCapture = null;
	private Button btnSumbit = null;

	private static final String URI = "192.168.1.105:3000";

	private RemoteLogin rl = null;
	private GetAmountDue gad = null;
	private String amount_due = "";

	private PayParking pp = null;

	private EditText etBreakDown = null;
	private Button btnReset = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	} // onCreate

	private void init() {
		et_Email = (EditText) findViewById(R.id.et_Email);
		et_Password = (EditText) findViewById(R.id.et_Password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
	} // init

	private void initCapture() {
		tvAmountDue = (TextView) findViewById(R.id.tvAmountDue);
		etCapture = (EditText) findViewById(R.id.etCapture);
		btnSumbit = (Button) findViewById(R.id.btnSumbit);
		btnSumbit.setOnClickListener(this);
	} // initCapture

	private void initBreakDown() {
		etBreakDown = (EditText) findViewById(R.id.etBreakDown);
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(this);
	} // initBreakDown

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnLogin:
			Editable email = et_Email.getText();
			Editable password = et_Password.getText();
			if (((email != null) && (email.length() > 0))
					&& ((email != null) && (email.length() > 0))) {
				rl = new RemoteLogin();
				rl.execute(URI, email.toString(), password.toString());
				
				showCapture();
			} // if
			break;
		case R.id.btnSumbit:
			sumbitLogic();
			break;
		case R.id.btnReset:
			showCapture();
			break;
		} // switch
	} // onClick
	
	private void showCapture() {
		try {
			int responce = rl.get(); 
			if (responce == 406) {
				setContentView(R.layout.capture);
				initCapture();

				gad = new GetAmountDue();
				gad.execute(URI);
				amount_due = "" + gad.get();
				tvAmountDue.setText("Amount due: R " + amount_due);

			} // if 406
//			if (responce == 401) {
			else {
				Toast.makeText(this, "Invalid login details", 1000).show();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	} // showCapture
	
	private void sumbitLogic() {
		Editable captured_amount = etCapture.getText();
		pp = new PayParking();
		pp.execute(URI, amount_due, captured_amount.toString());

		setContentView(R.layout.denomination);
		initBreakDown();

		try {
			etBreakDown.setText(pp.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	} // sumbitLogic

} // MainActivity
