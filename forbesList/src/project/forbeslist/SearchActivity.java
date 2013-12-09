package project.forbeslist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.MyCustomAdapter;
import util.Parent;
import Adaptors.DBBook;
import Builders.BuildBook;
import Exceptions.DBException;
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
			public void onClick(View v) {
				boolean nonNullFlag = false;
				arrayParents = new ArrayList<Parent>();
				EditText tText = (EditText) findViewById(R.id.title_s);
				String title = tText.getText().toString();
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

			if (arg1.getDownloadedFile() != null) {
				Uri imageUri = Uri.fromFile(arg1.getDownloadedFile());
				String strUri = "###" + imageUri.toString();
				for (int i = 0; i < arrayParents.size(); i++) {
					Parent p = arrayParents.get(i);
					// if file id equals strUri parsed..
					String fileid = p.getFileId();
					if (fileid != null
							&& fileid.equals(strUri.substring(46, 78))) {
						p.addFileId(strUri);
						arrayParents.set(i, p);
						adap.notifyDataSetChanged();
					}
				}
			}
		} else if (arg1.getData() instanceof List) {
			
			List<?> results = (List<?>) arg1.getData();

			for (int i = 0; i < results.size(); i++) {
				
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
						MainActivity.BookDao.readFile(file_id,
								SearchActivity.this);
					}
				}
				//add new parent to parent array
				arrayParents.add(parent);
			}
			// sets the adapter that provides data to the list.
			adap = new MyCustomAdapter(SearchActivity.this, arrayParents);
			adap.notifyDataSetChanged();
			mExpandableList.setAdapter(adap);
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
				e.printStackTrace();
			}

			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getAddressLine(1);
			msg = address+", "+city;
		}
		return msg;
	}
}