package project.forbeslist;


import android.graphics.Bitmap;
import android.location.Location;

public class Book {
	String title;
	String author;
	Location loc;
	Bitmap photo;
	
	
	public Book(String title, String author, Location loc, Bitmap photo){
		this.title=title;
		this.author=author;
		this.loc =loc;
		this.photo = photo;
	}	
	
	public Book(String title, String author, Location loc){
		this.title=title;
		this.author=author;
		this.loc =loc;
		
	}
	
	public Book(String title, String author){
		this.title=title;
		this.author=author;
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
	public Bitmap getPhoto(){
		return photo;
	}
	
	public void setPhoto(Bitmap photo){
		this.photo = photo;
	}
	
	
	
}
