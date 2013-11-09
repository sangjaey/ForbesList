package project.forbeslist;
 
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
public class TabMainActivity extends TabActivity {
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_main);
 
		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
 
		// search tab
		Intent intentSearch = new Intent().setClass(this, SearchActivity.class);
		TabSpec tabSpecSearch = tabHost
		  .newTabSpec("Search")
		  .setContent(intentSearch);
 
		// Upload tab
		Intent intentUpload = new Intent().setClass(this, UploadActivity.class);
		TabSpec tabSpecUpload = tabHost
		  .newTabSpec("Upload")
		  .setContent(intentUpload);
 
		// Profile tab
		Intent intentProfile = new Intent().setClass(this, ProfileActivity.class);
		TabSpec tabSpecProfile = tabHost
		  .newTabSpec("Profile")
		  .setContent(intentProfile);
 

		// add all tabs 
		tabHost.addTab(tabSpecSearch);
		tabHost.addTab(tabSpecUpload);
		tabHost.addTab(tabSpecProfile);
 
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}
 
}