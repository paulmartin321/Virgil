package com.virgil.virgilagent.response;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Map;

public class ResponseBase {

    protected String ResponseText = "";
    protected Map<String,String> Args;
    protected Intent StartIntent = null;

    public ResponseBase(Map<String,String> Args) {
        this.Args = Args;
    }

    public boolean Execute(Context InvokerContext) {
        return false;
    }

    public String GetResponseText() {
        return ResponseText;
    }

    protected String CreateElement(String ElementTag,String ElementContents) {
        return "<" + ElementTag + ">" + ElementContents + "</" + ElementTag + ">";
    }
    protected void CreateErrorResponse(String ErrorMessage) {
        this.ResponseText = CreateElement("error", CreateElement("message",ErrorMessage));
    }

    protected void BuildIntent() {
        try {
            this.StartIntent = new Intent();
            ComponentName componentName = null;
            String IntentAction = this.Args.get("i");
            String PackageName = this.Args.get("p");
            String ClassName = this.Args.get("cls");
            String Category = this.Args.get("c");
            String DataStr = this.Args.get("d");
            String TypeStr = this.Args.get("t");
            if (PackageName != null && ClassName != null)
                componentName = new ComponentName(PackageName,ClassName);
            if (IntentAction != null)
                this.StartIntent.setAction(IntentAction);
            if (DataStr != null) {
                Uri StartData = Uri.parse(DataStr);
                this.StartIntent.setData(StartData);
            }
            if (componentName != null)
                this.StartIntent.setComponent(componentName);
            if (Category != null)
                this.StartIntent.addCategory(Category);
            if (TypeStr != null)
                this.StartIntent.setType(TypeStr);
        }
        catch (Exception e) {
            return;
        }
    }
}
