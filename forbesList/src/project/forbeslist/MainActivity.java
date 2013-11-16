package project.forbeslist;

import com.cloudbase.*;
import com.google.gson.JsonObject;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class MainActivity extends Activity implements CBHelperResponder {
	Button joinButton,button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnButton2();
        JSONObject in = null;
    	CBHelper myHelper = new CBHelper(
    	        "forbeslist",
    	        "cac21deef7dd81d8af7506cd257173d2", 
    	        this);
    	myHelper.setPassword(md5("1234"));
    	String objectToInsert = "hi";
    	try {
			 in= new JSONObject().put("name", "sangwon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	myHelper.insertDocument(in, "test",this);
    	
    
    }
    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                    hexString.append(String.format("%02x", messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
	@Override
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {
		// TODO Auto-generated method stub
		
	}
    
}
