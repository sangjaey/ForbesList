package DataBeans;

import android.location.Location;

public class Book {
	String title;
	String author;
	Location loc;
	String uploader;
	String price;
	public Book(String title, String author, Location loc, String price){
		this.title=title;
		this.author=author;
		this.loc =loc;
		this.price = price;
		
	}
	
	public Book(String title, String author,String price){
		this.title=title;
		this.author=author;
		this.price=price;
	}
	
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price=price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location loc) {
		this.loc = loc;
	}

	
	public String getUploader(){
		return uploader;
	}
	public void setUploader(String uploader){
		this.uploader = uploader;
	}
	
	
}
