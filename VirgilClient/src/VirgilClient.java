import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import command.CommandParser;
import command.Help;
import command.RunScript;
import request.*;
import utilities.*;
/*
		ProcessCommandLine("connect",System.out);
		ProcessCommandLine("run service.info -p com.pinterest -cls com.pinterest.account.AuthenticatorService",System.out);
		ProcessCommandLine("run package.info -p com.testy",System.out);
		ProcessCommandLine("run context.startservice -p com.testy -cls com.testy.DataService",System.out);
		ProcessCommandLine("run sendbroadcast -i com.testy.CUSTOM_INTENT",System.out);
		ProcessCommandLine("run context.startactivity -p com.pinterest -cls com.pinterest.activity.webhook.WebhookActivity",System.out);
		ProcessCommandLine("run context.startactivity -p com.pinterest -cls com.pinterest.sdk.PinterestOauthActivity",System.out);		
		ProcessCommandLine("disconnect",System.out);
		ProcessCommandLine("exit",System.out);		
 */

public class VirgilClient {
	
	private static Session CurrentSession = new utilities.Session();
	private static boolean DoExit = false;
	
	public static void main(String[] args) {
		
		ProcessCommandLine("connect",System.out);
		ProcessCommandLine("run context.startactivity -p com.pinterest -cls com.pinterest.activity.webhook.WebhookActivity -i android.intent.action.VIEW -c android.intent.category.DEFAULT -d pinterest://http://xxx",System.out);		
		ProcessCommandLine("disconnect",System.out);
		ProcessCommandLine("exit",System.out);				
		return;

		/*
		while (!DoExit) {
			System.out.print(utilities.Constants.Prompt);
			String input = System.console().readLine();
			ProcessCommandLine(input,System.out);
		}
		*/
	}
	public static void ProcessCommandLine(String CommandLine,PrintStream OutputStream) {
		RequestBase CurrentRequest;
		if (!CommandParser.Parse(CommandLine)) {
			OutputStream.println("Error parsing: " + CommandLine);			
		}
		else {
			switch (CommandParser.CommandType) {
			case HELP:
				OutputStream.println(Help.GetHelpText());
				break;
			case EXIT:
				OutputStream.println("Command: " + CommandLine);
				OutputStream.println("Exiting...");									
				if (CurrentSession.GetIsConnected())
					CurrentSession.Close();
				DoExit = true;
				break;
			case CONNECT:
				System.out.println("Command: " + CommandLine);
				if (CurrentSession.Connect())
					OutputStream.println("Connected");
				else
					OutputStream.println(CurrentSession.GetErrorMessage());
				break;
			case DISCONNECT:
				OutputStream.println("Command: " + CommandLine);
				if (CurrentSession.GetIsConnected())
					CurrentSession.Close();
				break;
			case RUN:
				OutputStream.println("Command: " + CommandLine);
				if (!CurrentSession.GetIsConnected())
					OutputStream.println("Client must be connected before this command can be used.");
				else {
					CurrentRequest = RequestFactory.CreateRequest(CommandParser.CommandType,CommandParser.CommandName,CommandParser.CommandModule,CommandParser.CommandArgs);
					if (CurrentSession.Execute(CurrentRequest))					
						for (Object pack : CurrentRequest.GetResponseData())					
							OutputStream.println(pack.toString());
					else
						OutputStream.println(CurrentSession.GetErrorMessage());									
				}
				break;
			case RUNSCRIPT:
				RunScript CurrentScript = new RunScript(CommandParser.CommandModule,CommandParser.CommandArgs);
				ProcessScriptFile(CurrentScript);
				break;
			default:
				OutputStream.println("Unknown command: " + CommandLine);
			}
		}
	}
	public static void ProcessScriptFile(RunScript CurrentScript) {
		String ScriptFileName = CurrentScript.GetScriptFilename();
		if (ScriptFileName == "") {
			System.out.println("no script file name");
			return;			
		}
		if (ScriptFileName.charAt(0) != '/' && !ScriptFileName.substring(0, 2).equalsIgnoreCase("c:")) {
			String CurrentDir = System.getProperty("user.dir");
			ScriptFileName = CurrentDir + "\\" + ScriptFileName;
		}
		FileReader fr;
		File ScriptFile = new File(ScriptFileName);
		try {					
			fr = new FileReader(ScriptFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error opening input file: " + e.getMessage());
			return;
		}
		BufferedReader br = new BufferedReader(fr);				
		PrintStream OutputStream = null;
		if (CurrentScript.GetOutputFilename() != "")
			try {
				String OutputFileName = CurrentScript.GetOutputFilename();
				if (OutputFileName.charAt(0) != '/' && !OutputFileName.substring(0, 2).equalsIgnoreCase("c:")) {
					String CurrentDir = System.getProperty("user.dir");
					OutputFileName = CurrentDir + "\\" + OutputFileName;
				}
				OutputStream = new PrintStream(OutputFileName);
			} catch (FileNotFoundException e1) {
				System.out.println("Error opening output file: " + e1.getMessage());
			}
		try {
			String ScriptLine;
			while ((ScriptLine = br.readLine()) != null) {
				if (OutputStream == null)
					ProcessCommandLine(ScriptLine,System.out);
				else
					ProcessCommandLine(ScriptLine,OutputStream);
				if (DoExit)
					break;
			}
		}
		catch (IOException e) {
			System.out.println("Error reading input file: " + e.getMessage());
		}
		if (OutputStream != null)
			OutputStream.close();
		try {
			fr.close();
		} catch (IOException e) {
			return;
		}
	}
}
