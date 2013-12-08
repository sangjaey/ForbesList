package Adaptors;

import dataBeans.User;

public interface NewUser {
	public User getUser();
	public void buildUserFromInput(String email, String pass, String name, String phone);
}
