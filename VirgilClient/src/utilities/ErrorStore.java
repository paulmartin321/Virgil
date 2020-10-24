package utilities;

import java.util.ArrayList;

public class ErrorStore {

	private static ArrayList<ErrorData> Errors = new ArrayList<ErrorData>();
	
	public static void AddError(String ErrorMessage) {
		Errors.add(new ErrorData(ErrorMessage));
	}
	
	public static String PopErrors() {
		String ErrorString = "";
		for (int e = Errors.size() - 1; e >= 0 ; e--) {
			ErrorString += "\r\n" + Errors.get(e).GetMessage();			
		}
		Errors.clear();
		return ErrorString;
	}
	
}
