package project.forbeslist;

import java.util.List;

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

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;

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
				
				if(email.isEmpty()||
						name.isEmpty()||
						phonenumber.isEmpty()||
						password.isEmpty()){
					return;
				}
				
				CBSearchCondition cond = new CBSearchCondition(
						"email",
						CBSearchConditionOperator.CBOperatorEqual, email);
				MainActivity.myHelper.searchDocument("test", cond, JoinActivity.this);

				
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
		
		// TODO Auto-generated method stub
		if(arg1==null){
			System.out.println("NULL RETURNED");
			validEmail = true;
		}

		if(!arg1.isSuccess())
			System.out.println("NOt SUCCESSFULL");

		
		if (arg1.getData() instanceof List) {
			List results = (List) arg1.getData();
			if (results.size() == 0) {
				validEmail = true;
			}
			for(int i=0;i<results.size();i++){
				String email = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(i)).get("email");
				System.out.println("Printing emails     "+i+":" + email);
			}
			
		} else{
			System.out.println("###################" + arg1.getData());			
		}
		
		if(!validEmail){
			TextView error_text = (TextView)findViewById(R.id.register_error);
			error_text.setText("Email Already In use");
			return;
		}
		User newUser = new User(email, password, name,phonenumber);
		MainActivity.myHelper.insertDocument(newUser, "test");
		Intent intent = new Intent(context, TabMainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		validEmail = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		validEmail = false;
	}
	
	

}
