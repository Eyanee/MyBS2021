package com.Eyannee.demons.entity;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;

    public int getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }

    public void setId(int t){
        id=t;
    }
    public void setName(String ss){
        name=ss;
    }
    public void setPassword(String ss){
        password=ss;
    }
    public void setEmail(String ss){
        email=ss;
    }
    public void setPhone(String ss){
        phone=ss;
    }
}
