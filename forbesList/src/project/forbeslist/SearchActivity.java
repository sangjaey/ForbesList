package project.forbeslist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.MyCustomAdapter;
import util.Parent;
import Adaptors.DBBook;
import Builders.BuildBook;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.CBHelperResponse;
import com.cloudbase.CBQueuedRequest;
import com.google.gson.internal.StringMap;

public class SearchActivity extends Activity implements CBHelperResponder {

	private ArrayList<Parent> arrayParents = new ArrayList<Parent>();
	private ExpandableListView mExpandableList;
	private MyCustomAdapter adap;
	String userEmail;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		userEmail = getIntent().getExtras().getString("userName");
		Button searchBtn = (Button) findViewById(R.id.button_s);
		mExpandableList = (ExpandableListView) findViewById(R.id.bookList);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // TODO Auto-generated method stub
				boolean nonNullFlag = false;
				arrayParents = new ArrayList<Parent>();
				EditText tText = (EditText) findViewById(R.id.title_s);
		//		EditText aText = (EditText) findViewById(R.id.author_s);
				String title = tText.getText().toString();
			//	String author = aText.getText().toString();
				nonNullFlag = !(title.equals(""));
				if (nonNullFlag) {
					MainActivity.BookDao.read(title, "title",
							SearchActivity.this);
				} else {
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
				for (int i = 0; i < arrayParents.size(); i++) {
					Parent p = arrayParents.get(i);
					
					// if file id equals strUri parsed..
					String fileid = p.getFileId();
					System.out.println("fileid: " + fileid);
					System.out.println("uri: " + strUri.substring(46, 78));
					if (fileid != null
							&& fileid.equals(strUri.substring(46, 78))) {
						System.out.println("DL received for imaging");
						p.addFileId(strUri);
						arrayParents.set(i, p);
						adap.notifyDataSetChanged();
					}
				}
			}
		}

		// TODO Auto-generated method stub

		else if (arg1.getData() instanceof List) {
			
			List<?> results = (List<?>) arg1.getData();

			for (int i = 0; i < results.size(); i++) {
				System.out.println("LOOP START");
				
				//Build Book
				DBBook dbBook = new BuildBook();
				dbBook.buildBookFromResponse(arg1, i);
								
				//Get Geocode
				String geoLocation = getGeoLocation(dbBook);
				
				//Build Parent
				Parent parent = new Parent();
				parent.setTitle(dbBook.getTitle() + ", by " + dbBook.getAuthor());
				parent.addChildByBook(dbBook, geoLocation);
				
				//Look for files
				ArrayList<?> arr = (ArrayList<?>) (((StringMap<?>) ((List<?>) arg1
						.getData()).get(i)).get("cb_files"));				
				if(arr!=null){
					String file_id = (arr.get(0)).toString().substring(9, 41);
					parent.addFileId(file_id);
					if (file_id != null) {
						System.out.println("fileid: " + file_id);
						MainActivity.BookDao.readFile(file_id,
								SearchActivity.this);
						System.out.println("DL command issued");
					}
				}
				
				//add new parent to parent array
				arrayParents.add(parent);
				System.out.println("LOOP END");
			}
			// sets the adapter that provides data to the list.
			System.out.println("Adapter Set");
			adap = new MyCustomAdapter(SearchActivity.this, arrayParents);
			adap.notifyDataSetChanged();
			mExpandableList.setAdapter(adap);
		}

		else {
			System.out.println("###################" + arg1.getData());
		}

	}
	private String getGeoLocation(DBBook dbBook){
		String msg;
		if (dbBook.getLat() == 0.0 && dbBook.getLon() == 0.0)
			msg = "Location: Not Specified";
		else{
			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(this, Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(dbBook.getLat(), dbBook.getLon(), 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getAddressLine(1);
			msg = address+", "+city;
		}
		return msg;
	}
}