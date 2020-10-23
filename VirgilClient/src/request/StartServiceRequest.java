package request;

import org.w3c.dom.NodeList;

public class StartServiceRequest extends RequestBase {

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
