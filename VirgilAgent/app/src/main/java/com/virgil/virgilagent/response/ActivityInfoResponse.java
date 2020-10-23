package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import java.util.Map;

public class ActivityInfoResponse extends ResponseBase {
    private static String InfoTag = "package_info";
    private static String NameTag = "activityname";

    public ActivityInfoResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {
        try {
            PackageManager pm = InvokerContext.getPackageManager();
            String PackageName = this.Args.get("p");
            if (PackageName == null) {
                CreateErrorResponse("No package name");
                return true;
            }
            String ActivityName = this.Args.get("a");
            if (ActivityName == null) {
                CreateErrorResponse("No activity name");
                return true;
            }
            ComponentName componentName = new ComponentName(PackageName, ActivityName);
            ActivityInfo ActInfo;
            try {
                ActInfo = pm.getActivityInfo(componentName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                CreateErrorResponse("Activity name not found");
                return true;
            }
            this.ResponseText = CreateElement(InfoTag, CreateElement(NameTag, ActInfo.name));
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }
}
