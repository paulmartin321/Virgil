package request;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PackagesListRequest extends RequestBase {
	
	public static boolean ValidArguments(String[] Args) {
		return true;
	}
	
	public PackagesListRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}
	
	public boolean LoadResponseText(String ResponseText) {
		if (!this.Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;
		NodeList PackagesNode = this.ResponseElement.getElementsByTagName("package");
		for (int n = 0; n < PackagesNode.getLength(); n++) {
			Node NameNode = PackagesNode.item(n).getFirstChild();
			PackagesListData NewData = new PackagesListData();
			NewData.SetPackageName(NameNode.getTextContent());
			this.ResponseData.add(NewData);
		}
		return true;
	}
}

class PackagesListData {
	private String packagename;
	
	public void SetPackageName(String PackageName) {
		this.packagename = PackageName;
	}
	
	public PackagesListData() {
	}
	
	public String toString() {
		return this.packagename;
	}
}