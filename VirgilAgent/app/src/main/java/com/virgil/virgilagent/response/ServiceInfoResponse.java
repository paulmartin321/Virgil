package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import java.util.Map;

public class ServiceInfoResponse extends ResponseBase {
    private static String InfoTag = "service_info";
    private static String NameTag = "servicename";

    public ServiceInfoResponse(Map<String,String> Args) {
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
            String ServiceName = this.Args.get("s");
            if (ServiceName == null) {
                CreateErrorResponse("No service name");
                return true;
            }
            ComponentName componentName = new ComponentName(PackageName, ServiceName);
            ServiceInfo ServInfo;
            try {
                ServInfo = pm.getServiceInfo(componentName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                CreateErrorResponse("Service name not found");
                return true;
            }
            this.ResponseText = CreateElement(InfoTag, CreateElement(NameTag, ServInfo.name));
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }

}
