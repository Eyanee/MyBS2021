package com.Eyannee.demons.entity;

public class upPicInfo {
    private String filename;
    private String base64str;
    private String filepath;
    private boolean isMarked;

    public String getFilename(){
        return filename;
    }
    public String getFilepath(){
        return filepath;
    }
    public String getBase64str(){
        return base64str;
    }
    public boolean getisMarked(){
        return isMarked;
    }
    public void setFilename(String s){
        filename=s;
    }
    public void setBase64str(String s){
        base64str=s;
    }
    public void setMarked(boolean s){
        isMarked=s;
    }
    public void setFilepath(String s){
        filepath=s;
    }
}
