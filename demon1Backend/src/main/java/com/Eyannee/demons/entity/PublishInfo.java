package com.Eyannee.demons.entity;

public class PublishInfo {
    private String  username;
    private String filename;
    private String desInfo;
    private String receivePerson;
    private boolean isReceived;
    private boolean isSubmit;


    public String getUsername(){
        return username;
    }
    public String getFilename(){
        return filename;
    }
    public String  getDesInfo(){
        return desInfo;
    }
    public String getReceivePerson(){
        return receivePerson;
    }
    public boolean getIsReceived(){
        return isReceived;
    }
    public boolean getIsSubmit(){
        return isSubmit;
    }

    public void setUsername(String s){
        username=s;
    }
    public void setFilename(String s){
        filename=s;
    }
    public void setReceivedPerson(String s){
        receivePerson=s;
    }
    public void setReceived(boolean s){
        isReceived=s;
    }
    public void setDesInfo(String s){
        desInfo=s;
    }
    public void setSubmit(boolean s){
        isSubmit=s;
    }
}
