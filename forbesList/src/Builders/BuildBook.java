package Builders;

import java.util.List;

import Adaptors.DBBook;
import Adaptors.NewBook;
import android.location.Location;

import com.cloudbase.CBHelperResponse;
import com.google.gson.internal.StringMap;

import dataBeans.Book;

public class BuildBook implements NewBook, DBBook{
	private Book book;
	private double loc_lat;
	private double loc_lon;
	//Create user by input
	//Get user from response
	public Book getBook(){
		return book;
	}
	public void buildBookFromInput(String title, String author, Location loc, String price, String uploader){
		book = new Book(title, author, loc, price);
		book.setUploader(uploader);
	}
	public void buildBookFromResponse(CBHelperResponse arg1, int i){
		String title = (String) ((StringMap<?>) ((List<?>) arg1
				.getData()).get(i)).get("title");
		String author = (String) ((StringMap<?>) ((List<?>) arg1
				.getData()).get(i)).get("author");
		String price = (String) ((StringMap<?>) ((List<?>) arg1
				.getData()).get(i)).get("price");
		String email = (String) ((StringMap<?>) ((List<?>) arg1
				.getData()).get(i)).get("uploader");

		loc_lat = 0.0;
		loc_lon = 0.0;
		
		if (((StringMap<?>) ((StringMap<?>) ((List<?>) arg1.getData())
				.get(i)).get("cb_location")) != null) {
			loc_lat = (Double) ((StringMap<?>) ((StringMap<?>) ((List<?>) arg1
					.getData()).get(i)).get("cb_location")).get("lat");
			loc_lon = (Double) ((StringMap<?>) ((StringMap<?>) ((List<?>) arg1
					.getData()).get(i)).get("cb_location")).get("lng");
		}
		book = new Book(title, author, price);
		book.setUploader(email);
//		Location loc = new Location();
	}
	public String getTitle(){
		return book.getTitle();
	}
	public String getUploader(){
		return book.getUploader();
	}
	public String getPrice(){
		return book.getPrice();
	}
	public Location getLocation(){
		return book.getLoc();
	}
	public String getAuthor(){
		return book.getAuthor();
	}
	public double getLon(){
		return loc_lon;
	}
	public double getLat(){
		return loc_lat;
	}
}
