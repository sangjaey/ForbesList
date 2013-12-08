package Adaptors;

import android.location.Location;

import com.cloudbase.CBHelperResponse;

import dataBeans.Book;

public interface DBBook {
	public Book getBook();
	public void buildBookFromResponse(CBHelperResponse res, int i);
	
	public String getTitle();
	public String getUploader();
	public String getPrice();
	public Location getLocation();
	public String getAuthor();
	public double getLon();
	public double getLat();
}
