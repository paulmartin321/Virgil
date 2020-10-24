package request;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utilities.ErrorStore;

import org.w3c.dom.Node;

public class PackageInfoRequest extends RequestBase {

	public static boolean ValidArguments(String[] Args) {
		String[] ArgNames = {"-p"};
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
	
	public PackageInfoRequest(String RequestName,String[] Args) {
		super(RequestName,Args);
	}
	
	public boolean LoadResponseText(String ResponseText) {		
		if (!Initialize(ResponseText))
			return false;
		if (ErrorResponse())
			return true;		
		PackageInfoData NewData = new PackageInfoData();		
		NodeList PackageNodes = this.ResponseElement.getElementsByTagName("packagename");		
		NewData.SetPackageName(PackageNodes.item(0).getTextContent());		
		PackageNodes = this.ResponseElement.getElementsByTagName("publicsourcedir");
		NewData.SetPublicSourceDir(PackageNodes.item(0).getTextContent());
		PackageNodes = this.ResponseElement.getElementsByTagName("datadir");
		NewData.SetDataDir(PackageNodes.item(0).getTextContent());
		PackageNodes = this.ResponseElement.getElementsByTagName("sourcedir");
		NewData.SetSourceDir(PackageNodes.item(0).getTextContent());		
		NewData.LoadSubData(this.ResponseElement);
		this.ResponseData.add(NewData);		
		return true;
	}
}

class ResponseLoaderBase {
	
	public PackageItemInfo CreateItemInfo() {
		return null;
	}
	
	public void LoadResponseNode(Node ResponseNode) {
		Node ActivityInfoNode = ResponseNode.getFirstChild();
		while (ActivityInfoNode != null) {
			Node ActivityFieldNode = ActivityInfoNode.getFirstChild();			
			PackageItemInfo NewInfo = CreateItemInfo();			
			Class ActivityInfoCls = NewInfo.getClass();						
			while (ActivityFieldNode != null) {				
				try {
					Field Fld = ActivityInfoCls.getField(ActivityFieldNode.getNodeName());
					Fld.set(NewInfo, ActivityFieldNode.getTextContent());
				} catch (IllegalArgumentException e) {
						;
				} catch (IllegalAccessException e) {
						;
				} catch (DOMException e) {
						;
				} catch (NoSuchFieldException e) {
					;
				} catch (SecurityException e) {
					;
				}
				ActivityFieldNode = ActivityFieldNode.getNextSibling();
			}			
			ActivityInfoNode = ActivityInfoNode.getNextSibling();
		}
	}	
}
class PackageInfoData {
	private String packagename = "";
	private String publicSourceDir = "";
	private String dataDir = "";
	private String sourceDir = "";
	public List<ActivityInfo> activities = new ArrayList<ActivityInfo>();
	public List<PermissionInfo> permissions = new ArrayList<PermissionInfo>();
	public List<ProviderInfo> providers = new ArrayList<ProviderInfo>();
	public List<ActivityInfo> receivers = new ArrayList<ActivityInfo>();
	public List<ServiceInfo> services = new ArrayList<ServiceInfo>();
	
	public void SetPackageName(String PackageName) {
		this.packagename = PackageName;
	}
	public void SetPublicSourceDir(String PublicSourceDir) {
		this.publicSourceDir = PublicSourceDir;
	}
	public void SetDataDir(String DataDir) {
		this.dataDir = DataDir;
	}
	public void SetSourceDir(String SourceDir) {
		this.sourceDir = SourceDir;
	}
	public PackageInfoData() {
	}
	
	class ActivitiesLoader extends ResponseLoaderBase {
		private PackageInfoData ParentObject;
		
		public ActivitiesLoader(PackageInfoData ParentObject) {
			this.ParentObject = ParentObject;
		}
		public PackageItemInfo CreateItemInfo() {
			ActivityInfo NewInfo = new ActivityInfo();
			ParentObject.activities.add(NewInfo);
			return NewInfo;
		}
	}
	class PermissionsLoader extends ResponseLoaderBase {
		private PackageInfoData ParentObject;
		
		public PermissionsLoader(PackageInfoData ParentObject) {
			this.ParentObject = ParentObject;
		}
		public PackageItemInfo CreateItemInfo() {
			PermissionInfo NewInfo = new PermissionInfo();
			ParentObject.permissions.add(NewInfo);
			return NewInfo;
		}
	}
	class ProvidersLoader extends ResponseLoaderBase {
		private PackageInfoData ParentObject;
		
		public ProvidersLoader(PackageInfoData ParentObject) {
			this.ParentObject = ParentObject;
		}
		public PackageItemInfo CreateItemInfo() {
			ProviderInfo NewInfo = new ProviderInfo();
			ParentObject.providers.add(NewInfo);
			return NewInfo;
		}
	}
	class ReceiversLoader extends ResponseLoaderBase {
		private PackageInfoData ParentObject;
		
		public ReceiversLoader(PackageInfoData ParentObject) {
			this.ParentObject = ParentObject;
		}
		public PackageItemInfo CreateItemInfo() {
			ActivityInfo NewInfo = new ActivityInfo();
			ParentObject.receivers.add(NewInfo);
			return NewInfo;
		}
	}
	class ServicesLoader extends ResponseLoaderBase {
		private PackageInfoData ParentObject;
		
		public ServicesLoader(PackageInfoData ParentObject) {
			this.ParentObject = ParentObject;
		}
		public PackageItemInfo CreateItemInfo() {
			ServiceInfo NewInfo = new ServiceInfo();
			ParentObject.services.add(NewInfo);
			return NewInfo;
		}
	}
	
	public void LoadSubData(Element PackageEle) {
		ActivitiesLoader ActLoader = new ActivitiesLoader(this);
		Node RootNode = PackageEle.getElementsByTagName("activities").item(0);
		ActLoader.LoadResponseNode(RootNode);
		PermissionsLoader PermLoader = new PermissionsLoader(this);
		RootNode = PackageEle.getElementsByTagName("permissions").item(0);
		PermLoader.LoadResponseNode(RootNode);		
		ProvidersLoader ProvLoader = new ProvidersLoader(this);
		RootNode = PackageEle.getElementsByTagName("providers").item(0);
		ProvLoader.LoadResponseNode(RootNode);		
		ReceiversLoader RecLoader = new ReceiversLoader(this);
		RootNode = PackageEle.getElementsByTagName("receivers").item(0);
		RecLoader.LoadResponseNode(RootNode);		
		ServicesLoader ServLoader = new ServicesLoader(this);
		RootNode = PackageEle.getElementsByTagName("services").item(0);
		ServLoader.LoadResponseNode(RootNode);		
		return;
	}
	
	public String toString() {
		String RetVal = this.packagename;
		RetVal += "\n" + "publicSourceDir: " + this.publicSourceDir;
		RetVal += "\n" + "sourceDir: " + this.sourceDir;
		RetVal += "\n" + "dataDir: " + this.dataDir;
		RetVal += "\nActivities";
		for (ActivityInfo ActInfo : this.activities)
			RetVal += "\n" + ActInfo.toString();
		RetVal += "\nPermissions";
		for (PermissionInfo PerInfo : this.permissions)
			RetVal += "\n" + PerInfo.toString();
		RetVal += "\nProviders";
		for (ProviderInfo PeroInfo : this.providers)
			RetVal += "\n" + PeroInfo.toString();
		RetVal += "\nReceivers";
		for (ActivityInfo RecInfo : this.receivers)
			RetVal += "\n" + RecInfo.toString();
		RetVal += "\nservices";
		for (ServiceInfo ServInfo : this.services)
			RetVal += "\n" + ServInfo.toString();
		return RetVal;
	}
}

class PackageItemInfo {
	public String name;
	
	public PackageItemInfo() {
		
	}	
	public String toString() {
		return this.name;
	}
}
class ActivityInfo extends PackageItemInfo {
	
	public ActivityInfo() {
		super();
	}
	
}
class PermissionInfo extends PackageItemInfo {
	
	public PermissionInfo() {
		super();		
	}
		
}
class ProviderInfo extends PackageItemInfo {

	public ProviderInfo() {
		super();		
	}
	
}
class ServiceInfo extends PackageItemInfo {
	
	public ServiceInfo() {
		super();		
	}
	
}