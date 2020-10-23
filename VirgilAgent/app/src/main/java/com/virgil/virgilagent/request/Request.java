package com.virgil.virgilagent.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {
    private String RequestName;
    private Map<String,String> RequestArgs = new HashMap<String, String>();

    public String GetRequestName() {
        return this.RequestName;
    }
    public Map<String,String> GetRequestArgs() {
        return this.RequestArgs;
    }

    public Request(String RequestName) {
        this.RequestName = RequestName;
    }

    public boolean AddArg(String ArgName, String ArgValue) {
        try {
            this.RequestArgs.put(ArgName,ArgValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
