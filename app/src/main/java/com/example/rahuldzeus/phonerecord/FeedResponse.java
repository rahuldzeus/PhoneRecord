package com.example.rahuldzeus.phonerecord;

public class FeedResponse {
    private String username,fileAddress,message;
    FeedResponse(String username, String fileAddress, String message){
        this.username=username;
        this.fileAddress=fileAddress;
        this.message=message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
