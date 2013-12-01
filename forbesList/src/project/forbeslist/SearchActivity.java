package project.forbeslist;
 
import java.util.ArrayList;
import java.util.List;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;
import com.google.gson.internal.StringMap;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
		
		if (arg1.getFunction().equals("download")) {
			System.out.println("You want dl?");
			 if (arg1.getDownloadedFile() != null) {
				 System.out.println("DL complete");
				 Uri imageUri = Uri.fromFile(arg1.getDownloadedFile());
					String strUri = "###" + imageUri.toString();
					arrayChildren.add(strUri);
                 
         }
		}
		
		
		// TODO Auto-generated method stub
		
		else if (arg1.getData() instanceof List) {
			Double loc_lat=0.0;
			Double loc_lon=0.0;
			List<?> results = (List<?>) arg1.getData();
			
			for(int i=0;i<results.size();i++){
				String title = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("title");
				String author = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("author");
				
				if(((StringMap<?>)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("cb_location"))==null){
					loc_lat=0.0;
					loc_lon=0.0;
				}
				
				else{
					loc_lat = (Double) ((StringMap<?>)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("cb_location")).get("lat");
					loc_lon = (Double) ((StringMap<?>)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("cb_location")).get("lng");
				}
				ArrayList<?> a = (ArrayList<?>) (((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("cb_files"));

				
				
				//TODO: fetch image info here if you can
				Parent parent = new Parent();
				parent.setTitle(title + ", by "+ author);
				arrayChildren = new ArrayList<String>();
				arrayChildren.add("Title: " + title);
				arrayChildren.add("Author: " + author);
				if(loc_lat==0.0 && loc_lon==0.0) arrayChildren.add("Location: Not Specified");
				else arrayChildren.add("Location: " + loc_lat + "," + loc_lon);
				
				if(a==null){
					;
				}
				else{
					String file_id=(a.get(0)).toString().substring(9,41);
					if (file_id!=null){
						System.out.println("fileid: " + file_id);
						MainActivity.myHelper.downloadFile(file_id, SearchActivity.this);
					}
				}
				
				
				parent.setArrayChildren(arrayChildren);
				arrayParents.add(parent);
				arrayChildren = new ArrayList<String>();
			}
			//sets the adapter that provides data to the list.
	        mExpandableList.setAdapter(new MyCustomAdapter(SearchActivity.this,arrayParents));
			
		} else{
			System.out.println("###################" + arg1.getData());			
		}

	}
    
}