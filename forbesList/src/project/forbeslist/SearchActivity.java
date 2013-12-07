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
	
	private ArrayList<Parent> arrayParents =new ArrayList<Parent>();
	private ExpandableListView mExpandableList;
	private MyCustomAdapter adap;
	String userEmail;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
		userEmail = getIntent().getExtras().getString("userName");
        Button searchBtn = (Button) findViewById(R.id.button_s);
	    mExpandableList = (ExpandableListView)findViewById(R.id.bookList);			 
        searchBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				// TODO Auto-generated method stub
				boolean nonNullFlag = false;
				arrayParents =new ArrayList<Parent>();
				EditText tText = (EditText) findViewById(R.id.title_s);
				EditText aText = (EditText) findViewById(R.id.author_s);
				String title = tText.getText().toString();
                String author = aText.getText().toString();
                nonNullFlag = !(title.equals("") || author.equals(""));
        	    if(nonNullFlag){
        	    	//search from db. search by title/search by author
        	    	/*CBSearchCondition cond = new CBSearchCondition(
    						"title",
    						CBSearchConditionOperator.CBOperatorEqual, title);
    				MainActivity.myHelper.searchDocument("book", cond, SearchActivity.this);
        	    	*/
        	    	MainActivity.BookDao.read(title,"title", SearchActivity.this);
        	    }
        	    else{
        	    	Context context = getApplicationContext();
                	CharSequence text = "Fill in both fields please";
                	int duration = Toast.LENGTH_LONG;
                	
                	Toast toast = Toast.makeText(context, text, duration);
                	toast.show();
        	    }
			}
        });
    }
    
	@Override
	public void handleResponse(CBQueuedRequest arg0, CBHelperResponse arg1) {
		
		if (arg1.getFunction().equals("download")) {
			System.out.println("DL handler starting");
			 if (arg1.getDownloadedFile() != null) {
				 System.out.println("DL complete");
				 Uri imageUri = Uri.fromFile(arg1.getDownloadedFile());
					String strUri = "###" + imageUri.toString();
					for (int i=0; i<arrayParents.size(); i++){
						Parent p = arrayParents.get(i);
						ArrayList<String> c = p.getArrayChildren();
						
						//if file id equals strUri parsed..
						String fileid = c.get(0);
						System.out.println("fileid: "+fileid);
						System.out.println("uri: " +strUri.substring(46,78));
						if (fileid!=null && fileid.equals(strUri.substring(46,78))){
							System.out.println("DL received for imaging");
							if (c.size()<7) c.add(strUri);
							else c.add(6,strUri);
							p.setArrayChildren(c);
							arrayParents.set(i,p);
							adap.notifyDataSetChanged();
						}
					}
         }
		}
		
		
		// TODO Auto-generated method stub
		
		else if (arg1.getData() instanceof List) {
			Double loc_lat=0.0;
			Double loc_lon=0.0;
			List<?> results = (List<?>) arg1.getData();
			
			ArrayList<String> arrayChildren;
			for(int i=0;i<results.size();i++){
				System.out.println("LOOP START");
				arrayChildren = new ArrayList<String>();
				String title = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("title");
				String author = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("author");
				String price = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("price");
				String email = (String)((StringMap<?>)((List<?>)arg1.getData()).get(i)).get("uploader");
				
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
				String msg;
				if(loc_lat==0.0 && loc_lon==0.0) msg = "Location: Not Specified";
				else msg = "Location: "  + loc_lat + "," + loc_lon;
			
				if (arrayChildren.size()<2) arrayChildren.add("Title: " + title);
				else arrayChildren.add(1,"Title: " + title);
				if (arrayChildren.size()<3) arrayChildren.add("Author: " + author);
				else arrayChildren.add(2,"Author: " + author);
				if (arrayChildren.size()<4) arrayChildren.add(msg);
				else arrayChildren.add(3,msg);
				if (arrayChildren.size()<5) arrayChildren.add("Seller Email: "+email);
				else arrayChildren.add(4,"Seller Email: "+email);
				if (arrayChildren.size()<6) arrayChildren.add("Book Price: "+price);
				else arrayChildren.add(5,"Book Price: "+price);
				
				
				if(a==null){
					arrayChildren.add(0, "No snapshot");
					arrayChildren.add("No image available");
				}
				else{
					String file_id=(a.get(0)).toString().substring(9,41);
					arrayChildren.add(0,file_id);
					if (file_id!=null){
						System.out.println("fileid: " + file_id);
						//MainActivity.myHelper.downloadFile(file_id, SearchActivity.this);
						MainActivity.BookDao.readFile(file_id, SearchActivity.this);
						System.out.println("DL command issued");
					}
				}
				
				
				parent.setArrayChildren(arrayChildren);
				arrayParents.add(parent);
				arrayChildren = new ArrayList<String>();
				System.out.println("LOOP END");

			}
			//sets the adapter that provides data to the list.
			System.out.println("Adapter Set");
        	adap=new MyCustomAdapter(SearchActivity.this,arrayParents);
        	adap.notifyDataSetChanged();
	        mExpandableList.setAdapter(adap);
		} 
		
		
		else{
			System.out.println("###################" + arg1.getData());			
		}
		

	}
    
}