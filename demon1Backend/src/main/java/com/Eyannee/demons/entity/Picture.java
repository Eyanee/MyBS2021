package com.Eyannee.demons.entity;

public class Picture {

    private String username;
    private String filename;
    private String picname;
    private String filepath;
    private String xmlpath;
    private String cocopath;
    private boolean isMarked;

    public String getUsername(){
        return username;
    }
    public String getFilename(){
        return filename;
    }
    public String getPicname(){
        return picname;
    }
    public String getFilepath(){
        return filepath;
    }
    public String getXmlpath(){
        return xmlpath;
    }
    public String getCocopath(){
        return cocopath;
    }
    public boolean getIsMarked(){
        return isMarked;
    }

    public void setUsername(String s){
        username=s;
    }
    public void setFilename(String s){
        filename=s;
    }
    public void  setPicname(String s){
        picname=s;
    }
    public void setFilepath(String s){
        filename=s;
    }
    public void setXmlpath(String s){
        xmlpath=s;
    }
    public void setCocopath(String s){
        cocopath=s;
    }
    public void setMarked(boolean s){
        isMarked=s;
    }

}
