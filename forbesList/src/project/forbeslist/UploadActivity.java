package project.forbeslist;
 

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.cloudbase.*;
public class UploadActivity extends Activity {
	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	private File photo;
	private CBHelperResponder r;
	private LocationManager mlocManager;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Button uploadBtn = (Button) findViewById(R.id.button_u);
        Button snapBtn = (Button) findViewById(R.id.button1);
        uploadBtn.setOnClickListener(new OnClickListener(){
        	@Override
			public void onClick(View arg0) {
        		MainActivity.myHelper.setUseLocation(true);
				Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location!=null) {
					MainActivity.myHelper.setCurrentLocation(location);
					Toast.makeText(getBaseContext(), "Current loc: lon: "+ location.getLongitude() + "lat: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
				}
				else 					
					Toast.makeText(getBaseContext(), "No loc detected", Toast.LENGTH_SHORT).show();

				
				boolean nonNullFlag = false;
				EditText tText = (EditText) findViewById(R.id.title_u);
				EditText aText = (EditText) findViewById(R.id.author_u);
				
                String title = tText.getText().toString();
                String author = aText.getText().toString();
                nonNullFlag = !(title.equals("") || author.equals(""));
                Book in = null;
   	    	 	Bitmap bitmap = null;
        	    if(nonNullFlag){
        	    	//put in database

        	    	/*in = new JSONObject();
					 in.put("title", title);
					 in.put("author", author);
					 in.put("location", location);*/

        	    	 try {
                         bitmap = android.provider.MediaStore.Images.Media
                         .getBitmap(getContentResolver(), imageUri);
                        
                    } catch (Exception e) {
                    }
					 in = new Book(title, author, location, bitmap);
					 if (photo!=null){
						 ArrayList<File> files = new ArrayList<File>();
					     files.add(photo); 
					     MainActivity.myHelper.insertDocument(in, "book", files, r);
					 }
					 else MainActivity.myHelper.insertDocument(in, "book");
        	    	
        	    	//toast to say upload done.
        	    	Context context = getApplicationContext();
                	CharSequence text = "Upload Complete";
                	int duration = Toast.LENGTH_SHORT;

                	Toast toast = Toast.makeText(context, text, duration);
                	toast.show();
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
        
        snapBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,
			            Uri.fromFile(photo));
			    imageUri = Uri.fromFile(photo);
			    startActivityForResult(intent, TAKE_PICTURE);
			}       	
        });
        
        LocationListener mlocListener = new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location)
			{
			if (location != null)
			{
				Toast.makeText(getBaseContext(),
				"New location latitude [" +
				location.getLatitude() +
				"] longitude [" + 						location.getLongitude()+"]",
				Toast.LENGTH_SHORT).show();
			}
			}


			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(),"No provider available",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(),"Provider enabled!",Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(),"GPS status changed",Toast.LENGTH_SHORT).show();
			}
		};



		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 1, mlocListener);
        
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 1:
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                ContentResolver cr = getContentResolver();
                Bitmap bitmap;
                try {
                     bitmap = android.provider.MediaStore.Images.Media
                     .getBitmap(cr, selectedImage);

                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(this, selectedImage.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                    Log.e("Camera", e.toString());
                }
            }
        }
    }

    
}