package request;

import command.CommandParser;
import utilities.ErrorStore;

public class RequestFactory {
	public static RequestBase CreateRequest(utilities.Constants.CommandTypes CommandType,String CommandName,String CommandModule,String[] CommandArgs) {
		if (CommandModule.equalsIgnoreCase("packages.list"))
			if (PackagesListRequest.ValidArguments(CommandParser.CommandArgs))
				return new PackagesListRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;
		if (CommandModule.equalsIgnoreCase("package.info"))
			if (PackageInfoRequest.ValidArguments(CommandParser.CommandArgs))
				return new PackageInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;		
		if (CommandModule.equalsIgnoreCase("activity.info"))
			if (ActivityInfoRequest.ValidArguments(CommandParser.CommandArgs))
				return new ActivityInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;
		if (CommandModule.equalsIgnoreCase("context.startactivity"))
			if (StartActivityRequest.ValidArguments(CommandParser.CommandArgs))
				return new StartActivityRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;		
		if (CommandModule.equalsIgnoreCase("service.info"))
			if (ServiceInfoRequest.ValidArguments(CommandParser.CommandArgs))
				return new ServiceInfoRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;		
		if (CommandModule.equalsIgnoreCase("sendbroadcast"))
			if (SendBroadcastRequest.ValidArguments(CommandParser.CommandArgs))
				return new SendBroadcastRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;		
		if (CommandModule.equalsIgnoreCase("context.startservice"))
			if (StartServiceRequest.ValidArguments(CommandParser.CommandArgs))
				return new StartServiceRequest(CommandModule.toLowerCase(),CommandParser.CommandArgs);
			else
				return null;
		ErrorStore.AddError("Unknown command");
		return null;
	}
}
