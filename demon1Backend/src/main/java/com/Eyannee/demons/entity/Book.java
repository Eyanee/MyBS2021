package com.Eyannee.demons.entity;



/**
 * @author oyc
 * @Description:书籍实体类
 * @date 2018/7/8 22:51
 */


public class Book {
    private String Bno;
    private String Category;
    private String Title;
    private String Press;
    private int year;
    private String author;
    private float price;
    private int total;
    private int stock;

    public String getBno(){
        return Bno;
    }
    public String getCategory(){
        return Category;
    }
    public String getTitle(){
        return Title;
    }
    public String getPress(){
        return Press;
    }
    public int getYear(){
        return year;
    }
    public String getAuthor(){
        return author;
    }
    public float getPrice(){
        return price;
    }
    public int getTotal(){
        return total;
    }
    public int getStock(){
        return stock;
    }
    public void setBno(String ss){
        Bno=ss;
    }
    public void setCategory(String ss){
        Category=ss;
    }
    public void setTitle(String ss){
        Title=ss;
    }
    public void setPress(String ss){
        Press=ss;
    }
    public  void setYear(int t){
        year=t;
    }
    public void setAuthor(String ss){
        author=ss;
    }
    public void setPrice(float t){
        price=t;
    }
    public void setTotal(int t){
        total=t;
    }
    public void setStock(int t){
        stock=t;
    }

}
