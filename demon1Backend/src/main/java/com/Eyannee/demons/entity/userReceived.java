package com.Eyannee.demons.entity;

public class userReceived {

    private String username;
    private String filename;
    private String publisher;
    private String picfilepath;
    private boolean isFinished;
    private boolean isSubmit;

    public String getUsername(){
        return username;
    }
    public String getFilename(){
        return filename;
    }
    public String getPublisher(){
        return  publisher;
    }
    public String getPicfilepath(){
        return picfilepath;
    }
//    public String getXmlfilepath(){
//        return xmlfilepath;
//    }
//    public String getCocofilepath(){
//        return cocofilepath;
//    }
    public boolean getIsFinished(){
        return isFinished;
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
    public void setPublisher(String s){
        publisher=s;
    }
    public void setPicfilepath(String s){
        picfilepath=s;
    }
//    public void setXmlfilepath(String s){
//        xmlfilepath=s;
//    }
//    public void setCocofilepath(String s){
//        cocofilepath=s;
//    }
    public void setFinished(boolean s){
        isFinished=s;
    }
    public void setSubmit(boolean s){
        isSubmit=s;
    }

}
