package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import request.*;

public class Session {
	private Socket SessionSocket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean IsConnected = false;
	private String ErrorMessage = "";
	
	public boolean GetIsConnected() {
		return IsConnected;
	}
	public String GetErrorMessage() {
		return ErrorMessage;
	}
	public boolean Connect() {
		try {
			ErrorMessage = "";
			SessionSocket = new Socket(Constants.Host,Constants.Port);
	         out = new PrintWriter(SessionSocket.getOutputStream(),true);
	         in = new BufferedReader( new InputStreamReader(SessionSocket.getInputStream()));
	         this.IsConnected = true;
		} catch (UnknownHostException e) {
			ErrorMessage = e.getMessage();
			return false;
		} catch (IOException e) {
			ErrorMessage = e.getMessage();
			return false;
		}
		return true;
	}
	public boolean Execute(RequestBase CurrentRequest) {
		try {
			ErrorMessage = "";
			out.println(CurrentRequest.RequestText());
			String ResponseText = in.readLine();
			if (ResponseText == null) {
				ErrorMessage = "null response";
				return false;
			}
			else
				CurrentRequest.LoadResponseText(ResponseText);			
			return true;
		} catch (IOException e) {
			ErrorMessage = e.getMessage();
			return false;
		}
	}
	public boolean Close() {
		try {
			ErrorMessage = "";
			out.println("CLOSE");
			SessionSocket.close();
			in = null;
			out = null;
			SessionSocket = null;
			this.IsConnected = false;
			return true;
		} catch (IOException e) {
			ErrorMessage = e.getMessage();
			return false;
		}	
	}
}
