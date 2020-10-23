package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import java.util.Map;

public class PackageInfoResponse extends ResponseBase {
    private static String PackageInfoTag = "package_info";
    private static String PackageNameTag = "packagename";
    private static String PublicSourceDirTag = "publicsourcedir";
    private static String DataDirTag = "datadir";
    private static String SourceDirTag = "sourcedir";
    private static String ActivitiesTag = "activities";
    private static String ActivityInfoTag = "activityinfo";
    private static String NameTag = "name";
    private static String PermissionsTag = "permissions";
    private static String PermissionInfoTag = "permissioninfo";
    private static String ProvidersTag = "providers";
    private static String ProviderinfoTag = "providerinfo";
    private static String ReceiversTag = "receivers";
    private static String ServicesTag = "services";
    private static String ServiceInfoTag = "serviceinfo";

    public PackageInfoResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {

        try {
            PackageManager pm = InvokerContext.getPackageManager();
            PackageInfo PackInfo = null;
            String PackageName = this.Args.get("p");
            if (PackageName == null) {
                CreateErrorResponse("No package name");
                return true;
            }
            try {
                PackInfo = pm.getPackageInfo(PackageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES);
            } catch (PackageManager.NameNotFoundException e) {
                CreateErrorResponse("Name Not Found: " + e.getMessage());
                return true;
            }
            String PublicSourceDir = "";
            String DataDir = "";
            String SourceDir = "";
            ApplicationInfo AppInfo = null;
            try {
                AppInfo = pm.getApplicationInfo(PackageName, 0);
                PublicSourceDir = AppInfo.publicSourceDir;
                DataDir = AppInfo.dataDir;
                SourceDir = AppInfo.sourceDir;
            } catch (PackageManager.NameNotFoundException e) {
                ;
            }
            String PackageData = CreateElement(PackageNameTag, PackInfo.packageName);
            PackageData += CreateElement(PublicSourceDirTag, PublicSourceDir);
            PackageData += CreateElement(DataDirTag, DataDir);
            PackageData += CreateElement(SourceDirTag, SourceDir);
            String ListElements = "";
            if (PackInfo.activities != null)
                for (ActivityInfo ActInfo : PackInfo.activities) {
                    ListElements += CreateElement(ActivityInfoTag, CreateElement(NameTag, ActInfo.name));
                }
            String ActivitiesElement = CreateElement(ActivitiesTag, ListElements);
            ListElements = "";
            if (PackInfo.permissions != null)
                for (PermissionInfo PermInfo : PackInfo.permissions) {
                    ListElements += CreateElement(PermissionInfoTag, CreateElement(NameTag, PermInfo.name));
                }
            String PermissionsElement = CreateElement(PermissionsTag, ListElements);
            ListElements = "";
            if (PackInfo.providers != null)
                for (ProviderInfo ProvInfo : PackInfo.providers) {
                    ListElements += CreateElement(ProviderinfoTag, CreateElement(NameTag, ProvInfo.name));
                }
            String ProvidersElement = CreateElement(ProvidersTag, ListElements);
            ListElements = "";
            if (PackInfo.receivers != null)
                for (ActivityInfo RecInfo : PackInfo.receivers) {
                    ListElements += CreateElement(ActivityInfoTag, CreateElement(NameTag, RecInfo.name));
                }
            String ReceiversElement = CreateElement(ReceiversTag, ListElements);
            ListElements = "";
            if (PackInfo.services != null)
                for (ServiceInfo ServInfo : PackInfo.services) {
                    ListElements += CreateElement(ServiceInfoTag, CreateElement(NameTag, ServInfo.name));
                }
            String ServicesElement = CreateElement(ServicesTag, ListElements);
            this.ResponseText = CreateElement(PackageInfoTag, PackageData + ActivitiesElement + PermissionsElement + ProvidersElement + ReceiversElement + ServicesElement);
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return  true;
        }
    }

}
