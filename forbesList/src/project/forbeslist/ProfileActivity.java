package project.forbeslist;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
 
public class ProfileActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        TextView textview = new TextView(this);
        textview.setText("This is profile tab");
        setContentView(R.layout.activity_profile);
    }
}