package project.forbeslist;

import java.util.List;

import Adaptors.NewUser;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;

public class JoinActivity extends Activity implements CBHelperResponder{
	Button button1;
	private boolean validEmail;
	String email;
	String name;
	String phonenumber;
	String password;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		validEmail = false;
		setContentView(R.layout.activity_join);
		addListenerOnButton();
	}

	public void addListenerOnButton() {
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// go to next activity with 3 values passed
				EditText emailText = (EditText)findViewById(R.id.editText1);
				EditText nameText = (EditText)findViewById(R.id.editText2);
				EditText phoneText = (EditText)findViewById(R.id.editText3);
				EditText passwordText = (EditText)findViewById(R.id.editText4);

				email = emailText.getText().toString();
				name = nameText.getText().toString();
				phonenumber = phoneText.getText().toString();
				password = passwordText.getText().toString();

				if(email.isEmpty()|| name.isEmpty()|| phonenumber.isEmpty()||password.isEmpty()){
					Toast toast = Toast.makeText(getApplicationContext(),
							"You must fill all the fields",
							Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				MainActivity.UserDao.read(email,"email" ,JoinActivity.this);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join, menu);
		return true;
	}

	@Override
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {
		if (arg1.getData() instanceof List) {
			List results = (List) arg1.getData();
			if (results.size() == 0) {
				validEmail = true;
			}
		} /*else{
			System.out.println("###################" + arg1.getData());			
		}*/

		if(!validEmail){
			TextView error_text = (TextView)findViewById(R.id.register_error);
			error_text.setText("Email Already In use");
			return;
		}
		NewUser newUser = new BuildUser();
		newUser.buildUserFromInput(email, password, name, phonenumber);

		MainActivity.UserDao.insert(newUser.getUser());
		Intent intent = new Intent(context, TabMainActivity.class);
		intent.putExtra("userName", email);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		validEmail = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		validEmail = false;
	}
}
