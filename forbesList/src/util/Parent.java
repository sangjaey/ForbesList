package util;
import java.util.ArrayList;

import Adaptors.DBBook;

public class Parent {
    private String mTitle;
    private ArrayList<String> mArrayChildren;
 
    public Parent(){
    	mArrayChildren = new ArrayList<String>(7);
    }
    
    public String getTitle() {
        return mTitle;
    }
 
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
 
    public ArrayList<String> getArrayChildren() {
        return mArrayChildren;
    }
 
    public void setArrayChildren(ArrayList<String> mArrayChildren) {
        this.mArrayChildren = mArrayChildren;
    }
    public void addChildByBook(DBBook book, String msg){

		mArrayChildren.add(0, "====  INFORMATION  ====");
		mArrayChildren.add(1, "Title: " + book.getTitle());
		mArrayChildren.add(2, "Author: " + book.getAuthor());
		mArrayChildren.add(3, msg);
		mArrayChildren.add(4, "Seller Email: " + book.getUploader());
		mArrayChildren.add(5, "Book Price: " + book.getPrice());
		mArrayChildren.add(6, "No image available");
    }
    public void addFileId(String fileId){
    	mArrayChildren.add(6, fileId);
    }
    public String getFileId(){
    	return mArrayChildren.get(6);
    }
}