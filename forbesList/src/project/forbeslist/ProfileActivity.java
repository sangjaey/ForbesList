package project.forbeslist;
 
import java.util.List;

import Adaptors.DBUser;
import Builders.BuildUser;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;
 
public class ProfileActivity extends Activity implements CBHelperResponder{
    private String name;
    private String email;
    private String phonenumber;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        TextView textview = new TextView(this);
        textview.setText("This is profile tab");
        setContentView(R.layout.activity_profile);
        

        
		String userEmail = getIntent().getExtras().getString("userName");
		System.out.println("ashdfjkasdjfkajskl"+userEmail);
		/*CBSearchCondition cond = new CBSearchCondition(
				"email",
				CBSearchConditionOperator.CBOperatorEqual, userEmail);
		MainActivity.myHelper.searchDocument("test", cond, ProfileActivity.this);*/
		MainActivity.UserDao.read(userEmail, "email",ProfileActivity.this);
    }
	
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {
		DBUser dbUser = new BuildUser();
		dbUser.buildUserFromResponse(arg1);
		TextView emailView = (TextView) findViewById(R.id.email_p);
		TextView phoneView = (TextView) findViewById(R.id.phone_p);
		TextView nameView = (TextView) findViewById(R.id.name_p);
		
		email = dbUser.getEmail();
		name = dbUser.getName();
		phonenumber = dbUser.getPhonenumber();
		
//		email = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(0)).get("email");
//		name = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(0)).get("name");
//		phonenumber = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(0)).get("phonenumber");
		
		nameView.setText(name);
		emailView.setText(email);
		phoneView.setText(phonenumber);
	}	
}