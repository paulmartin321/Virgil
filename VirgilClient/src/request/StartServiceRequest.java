package request;

import org.w3c.dom.NodeList;

import utilities.ErrorStore;

public class StartServiceRequest extends RequestBase {

	public static boolean ValidArguments(String[] Args) {
		String[] ArgNames = {"-i","-c","-d","-p","-cls"};
		int i = 0;
		while (i < Args.length) {
			int a = 0;
			while (a < ArgNames.length)
				if (ArgNames[a].equalsIgnoreCase(Args[i]))
					break;
				else
					a++;
			if (a >= ArgNames.length) {
				ErrorStore.AddError("Unknown argument " + Args[i]);
				return false;
			}
			i = i + 2;
		}
		return true;
	}
	
	public StartServiceRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}
	
	public boolean LoadResponseText(String ResponseText) {
		if (!Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;
		StartActivityData NewData = new StartActivityData();	
		NodeList ActivityNodes = this.ResponseElement.getElementsByTagName("status");		
		NewData.SetStatus(ActivityNodes.item(0).getTextContent());		
		this.ResponseData.add(NewData);		
		return true;
	}
	
}

class StartServiceData {
	private String Status = "";

	public void SetStatus(String Status) {
		this.Status = Status;
	}
	
	public String toString() {
		return this.Status;
	}
}
