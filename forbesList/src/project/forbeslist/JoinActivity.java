package project.forbeslist;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudbase.CBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoinActivity extends Activity {
	Button button1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		addListenerOnButton();
	}

	public void addListenerOnButton() {
		final Context context = this;
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					// go to next activity with 3 values passed
				
				 	JSONObject in = null;

			    	MainActivity.myHelper.setPassword(Util.md5("1234"));
			    	try {
						 in= new JSONObject().put("name2", "sangwon2");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	MainActivity.myHelper.insertDocument(in, "test");
					Intent intent = new Intent(context, TabMainActivity.class);
					startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join, menu);
		return true;
	}

}
