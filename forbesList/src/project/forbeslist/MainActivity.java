// Sungjaelee

package project.forbeslist;

import com.cloudbase.*;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;

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
	Button joinButton,button1;
	
	public static CBHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnButton2();
        JSONObject in = new JSONObject();;
    	myHelper = new CBHelper(
    	        "forbeslist",
    	        "cac21deef7dd81d8af7506cd257173d2", 
    	        this);
    	myHelper.setPassword(md5("1234"));
    	try {
    		in.put("name", "sangwon"); 
    		in.put("pw","123123");
			 //in= new JSONObject().put("name", "sangwon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	myHelper.insertDocument(in, "test",this);

    	
    	// search
    	CBSearchCondition cond = new CBSearchCondition("nameValuePairs.name", CBSearchConditionOperator.CBOperatorEqual, "sangwon");
        cond.setLimit(3);

        myHelper.searchDocument("test",cond,this);
    	
    
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

		if(arg1.getData() instanceof List){
			System.out.println("@@@@@@@@@@@@@@@@@@@"+ ((List)arg1.getData()).size());
			System.out.println("*******************"+((List)arg1.getData()).get(0).toString());
		}
			else
			System.out.println("###################"+arg1.getData());
			
			
	}
    
}
