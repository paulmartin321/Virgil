package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;

import java.util.Map;

public class StartServiceResponse extends ResponseBase {
    private static String  StartTag = "startservice";
    private static String  StatusTag = "status";

    public StartServiceResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {
        try {
            BuildIntent();
            ComponentName StartedComponent = InvokerContext.startService(this.StartIntent);
            if (StartedComponent == null)
                this.ResponseText = CreateElement(StartTag,CreateElement(StatusTag,"Service Does No Exist"));
            else
                this.ResponseText = CreateElement(StartTag,CreateElement(StatusTag,"Service Started: " + StartedComponent.getPackageName() + " " + StartedComponent.getClassName()));
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }

}
