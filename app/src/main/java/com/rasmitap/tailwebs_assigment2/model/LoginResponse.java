package com.rasmitap.tailwebs_assigment2.model;

public class LoginResponse {

    private LoginData Data;

    private String IsSuccess;

    private String Message;

    public LoginData getData() {
        return Data;
    }

    public void setData(LoginData Data) {
        this.Data = Data;
    }

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    @Override
    public String toString() {
        return "ClassPojo [Data = " + Data + ", IsSuccess = " + IsSuccess + ", Message = " + Message + "]";
    }

}