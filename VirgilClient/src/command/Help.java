package command;

public class Help {
	private static String HelpText = "HELP" 
			+ "\n----"
			+ "\n  EXIT - Exits the console."
			+ "\n  CONNECT - Connects to the server."
			+ "\n  DISCONNECT - disconnects from the server."
			+ "\n  RUN [module] [options] - Runs a command."
			+ "\n    modules and options:"
			+ "\n        packages.list"
			+ "\n        package.info -p [packagename]"
			+ "\n        activity.info -p [packagename] -cls [activityname]"
			+ "\n        context.startactivity -i [intentname] -c [category] -d [data] -p [packagename] -cls [activityname] "
			+ "\n        context.startservice -i [intentname] -c [category] -d [data] -p [packagename] -cls [servicename] "
			+ "\n        service.info -p [packagename] -cls [servicename]"
			+ "\n        sendbroadcast -i [intentname] -c [category] -d [data]"
			+ "\n  RUNSCRIPT scriptfile [options] - Runs a script file of commands."
			+ "\n        options:"
			+ "\n        -o [filename]";
	public static String GetHelpText() {
		return HelpText;
	}
}
