package command;

import utilities.Constants;
import utilities.Constants.CommandTypes;

public class CommandParser {
	
	public static utilities.Constants.CommandTypes CommandType;
	public static String CommandName;
	public static String CommandModule;
	public static String[] CommandArgs;
	
	public static boolean Parse(String CommandLine) {
		String[] TempParts = CommandLine.split("\s+", 3);
		if (TempParts.length == 0) {
			CommandName = "";
			CommandModule = "";
			CommandArgs = new String[0];
			return false;
		}		
		CommandName = TempParts[0].toUpperCase();
		try {
			CommandType = Constants.CommandTypes.valueOf(CommandName);			
		}
		catch (IllegalArgumentException e) {
			return false;
		}		
		if (TempParts.length == 1) {
			CommandModule = "";
			CommandArgs = new String[0];
			return true;
		}
		CommandModule = TempParts[1];
		if (TempParts.length == 2) {
			CommandArgs = new String[0];
			return true;			
		}
		//TODO: make sure CommandArgs is -letter text -letter text etc
		CommandArgs = TempParts[2].split("\s+");
		return true;
	}
}
