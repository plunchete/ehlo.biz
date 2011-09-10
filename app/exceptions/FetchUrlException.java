package exceptions;

public class FetchUrlException extends Exception {
	
	private int responseCode;
	public FetchUrlException() {}
	
	public FetchUrlException(String msg, int responseCode) {
		super(msg);
		this.responseCode = responseCode;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
}
