package com.Eyannee.demons.entity;

public class Userfile {
    private String username;
    private String filename;
    private String picfilepath;
    private boolean isPubished;
    private boolean isReceived;
    private boolean isFinished;
    private boolean isConfirmed;
    private String xmlfilepath;
    private String cocofilepath;

    public String getUsername(){
        return username;
    }
    public String getFilename(String filename){
        return this.filename;
    }
    public String getPicfilepath(){
        return picfilepath;
    }
    public boolean getIsPublished(){
        return isPubished;
    }
    public boolean getIsReceived(){
        return isReceived;
    }
    public boolean getisFinished(){
        return isFinished;
    }
    public boolean getIsConfirmed(){
        return isConfirmed;
    }
    public String getXmlfilepath(){
        return xmlfilepath;
    }
    public String getCocofilepath(){
        return cocofilepath;
    }
    public void setUsername(String s){
        username=s;
    }
    public void setFilename(String s){
        filename=s;
    }
    public void setPicfilepath(String s){
        picfilepath=s;
    }
    public void setPubished(boolean t){
        isPubished=t;
    }
    public void setReceived(boolean t){
        isReceived=t;
    }
    public void setFinished(boolean t){
        isFinished=t;
    }
    public void setConfirmed(boolean t){
        isConfirmed=t;
    }
    public void setXmlfilepath(String s){
        xmlfilepath=s;
    }
    public void setCocofilepath(String s){
        cocofilepath=s;
    }


}
