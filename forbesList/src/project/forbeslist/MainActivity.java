// Sungjaelee

package project.forbeslist;

import java.util.List;

import location.LocationTrak;
import Adaptors.DBUser;
import Builders.BuildUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;

import dbLayout.BookDAO;
import dbLayout.UserDAO;

public class MainActivity extends Activity implements CBHelperResponder {
	Button joinButton, button1;
	public static UserDAO UserDao;
	public static BookDAO BookDao;
	String pw, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent locationService = new Intent(this, LocationTrak.class);
		startService(locationService);

		setContentView(R.layout.activity_main);
		addRegisterButton();
		addLoginButton();

		UserDao = new UserDAO();
		UserDao.init(this);
		BookDao = new BookDAO();
		BookDao.init(this);
	}

	// move to register button
	public void addRegisterButton() {
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
	public void addLoginButton() {

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText emailText = (EditText) findViewById(R.id.email);
				EditText passwordText = (EditText) findViewById(R.id.pw);

				email = emailText.getText().toString();
				pw = passwordText.getText().toString();

				UserDao.read(email, "email",MainActivity.this);

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
				DBUser dbUser = new BuildUser();
				dbUser.buildUserFromResponse(arg1);
				String pass = dbUser.getPassword();
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
		} else {
			System.out.println("###################" + arg1.getData());
		}
	}
}
