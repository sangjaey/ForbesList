package dataBeans;

public class User {
	String email;
	String password;
	String name;
	String phonenumber;
	
	public User(){
		this.email=null;
		this.password=null;
		this.name=null;
		this.phonenumber=null;
	}
	
	public User(String email,String password,String name, String phonenumber){
		this.email=email;
		this.password=password;
		this.name=name;
		this.phonenumber = phonenumber;	
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhonenumber(){
		return phonenumber;
	}
}
