package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Map;

public class StartActivityResponse extends ResponseBase {
    private static String  StartTag = "startactivity";
    private static String  StatusTag = "status";

    public StartActivityResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {
        try {
            BuildIntent();
            InvokerContext.startActivity(this.StartIntent);
            this.ResponseText = CreateElement(StartTag,CreateElement(StatusTag,"Activity Started"));
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }
}
