package com.virgil.virgilagent.response;

import com.virgil.virgilagent.request.Request;

public class ResponseFactory {
    private static String ErrorMessage = "";

    public static String GetErrorMessage() {
        return ErrorMessage;
    }

    public static ResponseBase CreateResponse(Request CurrentRequest) {
        try {
            ErrorMessage = "";
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("packages.list"))
                return new PackagesListResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("package.info"))
                return new PackageInfoResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("activity.info"))
                return new ActivityInfoResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("context.startactivity"))
                return new StartActivityResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("context.startservice"))
                return new StartServiceResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("service.info"))
                return new ServiceInfoResponse(CurrentRequest.GetRequestArgs());
            if (CurrentRequest.GetRequestName().equalsIgnoreCase("sendbroadcast"))
                return new SendBroadcastResponse(CurrentRequest.GetRequestArgs());
            return null;
        }
        catch (Exception e) {
            ErrorMessage = e.getMessage();
            return null;
        }
    }
}
