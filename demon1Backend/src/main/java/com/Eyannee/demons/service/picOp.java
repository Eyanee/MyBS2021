package com.Eyannee.demons.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class picOp {


    public boolean transferAndSave(String base64Str,String picname,String videoname,String username) throws IOException {

        if(base64Str==null){
            System.out.println("The base64str is null");
            return false;
        }
        OutputStream out=null;
        String folderPath="D:/VueCode/MyBS2021/MyPics/"+username+"/"+videoname;
        File file1 =new File(folderPath);
        //如果文件夹不存在则创建
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
        }
        String filePath=folderPath+"/"+picname;
        //检查文件是否存在
        File file2=new File(filePath);
        if(file2.exists()){
            System.out.println("pic already exist");
            return false;
        }
        Base64.Decoder decoder = Base64.getMimeDecoder();

        out = new FileOutputStream(filePath);
        int pos=base64Str.indexOf("base64,");
        String temp=base64Str.substring(pos+7);
        byte[] b=decoder.decode(temp);
        out.write(b);
        out.close();
        return true;
    }
}