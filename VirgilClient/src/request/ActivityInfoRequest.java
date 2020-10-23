package request;

import org.w3c.dom.NodeList;

public class ActivityInfoRequest extends RequestBase {

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

