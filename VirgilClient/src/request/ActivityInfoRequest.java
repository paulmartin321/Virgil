package request;

import org.w3c.dom.NodeList;

import utilities.ErrorStore;

public class ActivityInfoRequest extends RequestBase {

	public static boolean ValidArguments(String[] Args) {
		String[] ArgNames = {"-p","-cls"};
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
	
	public ActivityInfoRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}
	
	public boolean LoadResponseText(String ResponseText) {
		if (!Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;
		ActivityInfoData NewData = new ActivityInfoData();		
		NodeList ActivityNodes = this.ResponseElement.getElementsByTagName("activityname");		
		NewData.SetActivityName(ActivityNodes.item(0).getTextContent());		
		this.ResponseData.add(NewData);		
		return true;
	}
	
}

class ActivityInfoData {
	private String activityname = "";

	public void SetActivityName(String ActivityName) {
		this.activityname = ActivityName;
	}
	
	public String toString() {
		return this.activityname;
	}
}

