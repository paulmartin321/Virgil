package request;

import org.w3c.dom.NodeList;

import utilities.ErrorStore;

public class ServiceInfoRequest extends RequestBase {

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

