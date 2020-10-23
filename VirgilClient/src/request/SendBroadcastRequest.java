package request;

import org.w3c.dom.NodeList;

public class SendBroadcastRequest extends RequestBase {

	public SendBroadcastRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}
	
	public boolean LoadResponseText(String ResponseText) {
		if (!Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;
		SendBroadcastData NewData = new SendBroadcastData();	
		NodeList ActivityNodes = this.ResponseElement.getElementsByTagName("status");		
		NewData.SetStatus(ActivityNodes.item(0).getTextContent());		
		this.ResponseData.add(NewData);		
		return true;
	}
	
}

class SendBroadcastData {
	private String Status = "";

	public void SetStatus(String Status) {
		this.Status = Status;
	}
	
	public String toString() {
		return this.Status;
	}
}
