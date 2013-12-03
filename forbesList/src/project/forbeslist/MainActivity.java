// Sungjaelee

package project.forbeslist;

import com.cloudbase.*;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;

import android.widget.Toast;

public class MainActivity extends Activity implements CBHelperResponder {
	Button joinButton, button1;
	public static CBHelper myHelper;
	String pw, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		addListenerOnButton2();
		myHelper = new CBHelper("forbeslist",
				"cac21deef7dd81d8af7506cd257173d2", this);
		myHelper.setPassword(md5("1234"));
	}

	// move to register button
	public void addListenerOnButton() {
		final Context context = this;
		joinButton = (Button) findViewById(R.id.register);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// go to next activity with 3 values passed
				Intent intent = new Intent(context, JoinActivity.class);
				startActivity(intent);
			}
		});
	}

	// try to log in
	public void addListenerOnButton2() {
		final Context context = this;

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText emailText = (EditText) findViewById(R.id.email);
				EditText passwordText = (EditText) findViewById(R.id.pw);

				email = emailText.getText().toString();
				pw = passwordText.getText().toString();

				CBSearchCondition cond = new CBSearchCondition("email",
						CBSearchConditionOperator.CBOperatorEqual, email);
				cond.setLimit(3);
				myHelper.searchDocument("test", cond, MainActivity.this);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {

		if (arg1.getData() instanceof List) {
			if (((List) arg1.getData()).size() != 0) {
				String s = (((List) arg1.getData()).get(0)).toString();
				String pass = (String) ((com.google.gson.internal.StringMap) ((List) arg1
						.getData()).get(0)).get("password");
				if (pw.equals(pass)) {
					Intent intent = new Intent(this, TabMainActivity.class);
					intent.putExtra("userName", email);
					startActivity(intent);
				}
			} else {

				Toast toast = Toast.makeText(getApplicationContext(),
						"email and password doesnt match", Toast.LENGTH_LONG);
				toast.show();

			}

		} else
			System.out.println("###################" + arg1.getData());
	}

	private static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(String.format("%02x", messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
