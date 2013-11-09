package project.forbeslist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button joinButton,button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnButton2();
    }


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
	
	public void addListenerOnButton2() {
		final Context context = this;
		joinButton = (Button) findViewById(R.id.button1);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					// go to next activity with 3 values passed
					Intent intent = new Intent(context, TabMainActivity.class);
					startActivity(intent);
			}
		});
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
