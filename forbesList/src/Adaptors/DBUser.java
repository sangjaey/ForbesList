package Adaptors;

import com.cloudbase.CBHelperResponse;

import dataBeans.User;

public interface DBUser {
	public User getUser();
	public void buildUserFromResponse(CBHelperResponse res);
	public String getPassword();
	public String getEmail();
	public String getName();
	public String getPhonenumber();
}
