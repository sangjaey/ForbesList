package project.forbeslist;
 

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
 
public class UploadActivity extends Activity {
	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Button uploadBtn = (Button) findViewById(R.id.button_u);
        Button snapBtn = (Button) findViewById(R.id.button1);
        uploadBtn.setOnClickListener(new OnClickListener(){
        	@Override
			public void onClick(View arg0) {
				boolean nonNullFlag = false;
				EditText tText = (EditText) findViewById(R.id.title_u);
				EditText aText = (EditText) findViewById(R.id.author_u);
				
                String title = tText.getText().toString();
                String author = aText.getText().toString();
                nonNullFlag = !(title.equals("") || author.equals(""));

        	    if(nonNullFlag){
        	    	//put in database
        	    	//TODO
        	    	
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
			    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,
			            Uri.fromFile(photo));
			    imageUri = Uri.fromFile(photo);
			    //attach File photo to db request and send to db.
			    startActivityForResult(intent, TAKE_PICTURE);
			}       	
        });
        
        
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