package project.forbeslist;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import location.LocationTrak;
import Adaptors.NewBook;
import Builders.BuildBook;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
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

import com.cloudbase.CBHelperResponder;

//import dataBeans.Book;
public class UploadActivity extends Activity {
	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	private File photo;
	private CBHelperResponder r;
	private LocationManager mlocManager;
	private String uploader;
	public void onCreate(Bundle savedInstanceState) {
		uploader = getIntent().getExtras().getString("userName");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		File file = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
		if(file.exists()){
			file.delete();
		}

		Button uploadBtn = (Button) findViewById(R.id.button_u);
		Button snapBtn = (Button) findViewById(R.id.button1);

		uploadBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//MainActivity.myHelper.setUseLocation(true);
				MainActivity.BookDao.setUseLocation(true);
				Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location!=null) {
					//MainActivity.myHelper.setCurrentLocation(location);
					MainActivity.BookDao.setCurrentLocation(location);
					Toast.makeText(getBaseContext(), "Current loc: lon: "+ location.getLongitude() + "lat: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
				}
				else {					
					Toast.makeText(getBaseContext(), "No loc detected", Toast.LENGTH_SHORT).show();
				}

				boolean nonNullFlag = false;
				EditText tText = (EditText) findViewById(R.id.title_u);
				EditText aText = (EditText) findViewById(R.id.author_u);
				EditText pText = (EditText) findViewById(R.id.price_u);

				String title = tText.getText().toString();
				String author = aText.getText().toString();
				String price = pText.getText().toString();
				nonNullFlag = !(title.equals("") || author.equals("")||price.equals(""));

				if(nonNullFlag){
					NewBook newBook = new BuildBook();
					newBook.buildBookFromInput(title, author, location, price, uploader);
					if (photo!=null){
						ArrayList<File> files = new ArrayList<File>();
						files.add(photo); 
						MainActivity.BookDao.insert(newBook.getBook(), files, r);
					} else{
						MainActivity.BookDao.insert(newBook.getBook());
					}
					
					//toast to say upload done.
					Context context = getApplicationContext();
					CharSequence text = "Upload Complete";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else{
					Context context = getApplicationContext();
					CharSequence text = "Fill in both fields please";
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}
		});

		snapBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photo));
				imageUri = Uri.fromFile(photo);

				startActivityForResult(intent, TAKE_PICTURE);
			}       	
		});
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 1, LocationTrak.mlocListener);
	}
	private Bitmap checkifImageRotated() {
		ExifInterface exif;
		try {
			exif = new ExifInterface(Environment.getExternalStorageDirectory()+"/Pic.jpg");
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int rotate = 0;
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270 :
				rotate = -90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180 :
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90 :
				rotate = 90;
				break;
			}
			if (rotate != 0) {
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/Pic.jpg");
				Matrix matrix = new Matrix();
				matrix.setRotate(rotate);
				Bitmap bmpRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
				return bmpRotated;
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
//				ContentResolver cr = getContentResolver();
				Bitmap bitmap;
				try {
					//bitmap = android.provider.MediaStore.Images.Media
							//.getBitmap(cr, selectedImage);
					bitmap = checkifImageRotated();
					bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStorageDirectory()+"/Pic.jpg"));
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