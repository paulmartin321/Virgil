package utilities;

public class ErrorData {
	private String Message;
	
	public ErrorData(String ErrorMessage) {
		this.Message = ErrorMessage;
	}
	
	public String GetMessage() {
		return this.Message;
	}
}
