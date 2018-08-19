package com.imagine.bd.hayvenapp.Model;

public class ResponseData {
    private String apiType="";
    private String isSignin="";
    private String response="";


    public ResponseData(String apiType, String isSignin, String response) {
        this.apiType = apiType;
        this.isSignin = isSignin;
        this.response = response;
    }

    public String getIsSignin() {
        return isSignin;
    }

    public void setIsSignin(String isSignin) {
        this.isSignin = isSignin;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
