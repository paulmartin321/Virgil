package request;

import command.CommandParser;

public class RequestFactory {
	public static RequestBase CreateRequest(utilities.Constants.CommandTypes CommandType,String CommandName,String CommandModule,String[] CommandArgs) {
		if (CommandModule.equalsIgnoreCase("packages.list"))
			return new PackagesListRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("package.info"))
			return new PackageInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("activity.info"))
			return new ActivityInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("context.startactivity"))
			return new StartActivityRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("service.info"))
			return new ServiceInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("sendbroadcast"))
			return new SendBroadcastRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		if (CommandModule.equalsIgnoreCase("context.startservice"))
			return new StartServiceRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
		return null;
	}
}
