package Builders;

import java.util.List;

import Adaptors.DBUser;
import Adaptors.NewUser;

import com.cloudbase.CBHelperResponse;

import dataBeans.User;

public class BuildUser implements NewUser, DBUser{
	private User user;
	//Create user from input
	//Get user from response
	public User getUser(){
		return user;
	}
	public void buildUserFromInput(String email, String pass, String name, String phone){
		user = new User(email, pass, name, phone);
	}
	public void buildUserFromResponse(CBHelperResponse res){
		String pass = (String) ((com.google.gson.internal.StringMap) ((List) res
				.getData()).get(0)).get("password");
		String name = (String) ((com.google.gson.internal.StringMap) ((List) res
				.getData()).get(0)).get("name");
		String email = (String) ((com.google.gson.internal.StringMap) ((List) res
				.getData()).get(0)).get("email");
		String phone = (String) ((com.google.gson.internal.StringMap) ((List) res
				.getData()).get(0)).get("phonenumber");
		user = new User(email, pass, name, phone);
	}
	public String getPassword(){
		return user.getPassword();
	}
	public String getEmail(){
		return user.getEmail();
	}
	public String getPhonenumber(){
		return user.getPhonenumber();
	}
	public String getName(){
		return user.getName();
	}
}
