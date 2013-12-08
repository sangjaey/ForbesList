package Adaptors;

import android.location.Location;
import dataBeans.Book;

public interface NewBook {
	public Book getBook();
	public void buildBookFromInput(String title, String author, Location loc, String price, String uploader);
}
