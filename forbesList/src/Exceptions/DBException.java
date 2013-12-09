package Exceptions;

public class DBException extends Exception{
	private static final long serialVersionUID = 1L;
	//	private final String ERROR_MESSAGE 
	private String error;
	public DBException(String className){
		super();
		error = "Invalid Response From Database! : When handling query in "+className;
	}
	public String getErrorMessage(){
		return error;
	}
}
