package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Map;

public class SendBroadcastResponse extends ResponseBase {
    private static String  StartTag = "sendbroadcast";
    private static String  StatusTag = "status";

    public SendBroadcastResponse(Map<String,String> Args) {
        super(Args);
    }

    public boolean Execute(Context InvokerContext) {
        try {
            Intent StartIntent;
            String IntentAction = this.Args.get("i");
            if (IntentAction == null) {
                CreateErrorResponse("Intent action missing");
                return true;
            }
            StartIntent = new Intent(IntentAction);
            InvokerContext.sendBroadcast(StartIntent);
            this.ResponseText = CreateElement(StartTag, CreateElement(StatusTag, "Broadcast Sent"));
            return true;
        }
        catch (Exception e) {
            CreateErrorResponse(e.getMessage());
            return true;
        }
    }
}
