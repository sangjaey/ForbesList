package project.forbeslist;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class SearchActivity extends Activity implements CBHelperResponder {
	
	private ExpandableListView mExpandableList;
	private ArrayList<Parent> arrayParents;
	private ArrayList<String> arrayChildren;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button searchBtn = (Button) findViewById(R.id.button_s);
	    mExpandableList = (ExpandableListView)findViewById(R.id.bookList);			 
        searchBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean nonNullFlag = false;
				EditText tText = (EditText) findViewById(R.id.title_s);
				EditText aText = (EditText) findViewById(R.id.author_s);
				String title = tText.getText().toString();
                String author = aText.getText().toString();
                nonNullFlag = !(title.equals("") || author.equals(""));
        	    if(nonNullFlag){
        	    	
        	        arrayParents = new ArrayList<Parent>();
        	        arrayChildren = new ArrayList<String>();
        	    	//search from db. search by title/search by author
        	    	CBSearchCondition cond = new CBSearchCondition(
    						"title",
    						CBSearchConditionOperator.CBOperatorEqual, title);
    				MainActivity.myHelper.searchDocument("book", cond, SearchActivity.this);
        	    	
        	    }
        	    else{
        	    	Context context = getApplicationContext();
                	CharSequence text = "Fill in both fields please";
                	int duration = Toast.LENGTH_SHORT;

                	Toast toast = Toast.makeText(context, text, duration);
                	toast.show();
        	    }
			}
        });
    }
    
	@Override
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {
		
		// TODO Auto-generated method stub
		
		if (arg1.getData() instanceof List) {
			
			List results = (List) arg1.getData();
			for(int i=0;i<results.size();i++){
				String title = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(i)).get("title");
				String author = (String)((com.google.gson.internal.StringMap)((List)arg1.getData()).get(i)).get("author");
				Double loc_lat = (Double) ((com.google.gson.internal.StringMap)((List)arg1.getData()).get(i)).get("cb_location.lat");
				Double loc_lon = (Double) ((com.google.gson.internal.StringMap)((List)arg1.getData()).get(i)).get("cb_location.lng");
				//TODO: fetch image info here if you can
				Parent parent = new Parent();
				parent.setTitle(title + ", by "+ author);
				arrayChildren = new ArrayList<String>();
				arrayChildren.add("Title: " + title);
				arrayChildren.add("Author: " + author);
				arrayChildren.add("Location: " + loc_lat + "," + loc_lon);
				parent.setArrayChildren(arrayChildren);
				arrayParents.add(parent);

			}
			//sets the adapter that provides data to the list.
	        mExpandableList.setAdapter(new MyCustomAdapter(SearchActivity.this,arrayParents));
			
		} else{
			System.out.println("###################" + arg1.getData());			
		}

	}
    
}