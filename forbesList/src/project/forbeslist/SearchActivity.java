package project.forbeslist;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
 
public class SearchActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        TextView textview = new TextView(this);
        textview.setText("This is search tab");
        setContentView(R.layout.activity_search);
    }
}