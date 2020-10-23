package request;

import org.w3c.dom.NodeList;

public class ServiceInfoRequest extends RequestBase {

	public ServiceInfoRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}

	public boolean LoadResponseText(String ResponseText) {
		if (!Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;
		ServiceInfoData NewData = new ServiceInfoData();		
		NodeList ActivityNodes = this.ResponseElement.getElementsByTagName("servicename");		
		NewData.SetServiceName(ActivityNodes.item(0).getTextContent());		
		this.ResponseData.add(NewData);		
		return true;
	}
	
}

class ServiceInfoData {
	private String servicename = "";

	public void SetServiceName(String ServiceName) {
		this.servicename = ServiceName;
	}
	
	public String toString() {
		return this.servicename;
	}
}

