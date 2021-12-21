package com.Eyannee.demons.Controller;


import com.Eyannee.demons.entity.uploadData;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class uploadController {

    @RequestMapping(value = "/uploadPics" ,method= RequestMethod.POST)
    public String uploadPic(@RequestParam("file") MultipartFile file,String username, String filename) throws IOException {
        System.out.println("asdf");
        if (file.isEmpty()) {
            return "empty file";
        }

        String fileName = file.getOriginalFilename();
        String fileName1 = fileName.split("\\.")[1];
        //String name = UUIDUtils.getUUID(); // 随机的uuid
        String folderPath = "D:/VueCode/MyBS2021/MyPics";
        //检查filepath是否存在
        File file1 =new File(folderPath);
        //如果文件夹不存在则创建
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
        }
        //如果不存在则新建path
        String filePath=folderPath+"/"+fileName;
        //检查文件是否存在
        File file2=new File("D:/hashdog/a.text");
        if(!file2.exists()){
            return "file_name already exits";
        }

            File dest = new File(filePath);
        file.transferTo(dest);
        return "success";
    }

    @RequestMapping(value = "/uploadVideo" ,method= RequestMethod.POST)
    public String uploadVideo(@RequestParam("file") MultipartFile file,String username, String filename) throws IOException {
        System.out.println("asdf");
        if (file.isEmpty()) {
            return "empty file";
        }
        String fileName = file.getOriginalFilename();
        String fileName1 = fileName.split("\\.")[1];
        //String name = UUIDUtils.getUUID(); // 随机的uuid
        //检查文件后缀是否为.mp4
        int pos=fileName.lastIndexOf('.');
        String temp=fileName.substring(pos);
        if(!temp.equals(".mp4")){
            return "error file type";
        }
        String folderPath = "D:/VueCode/MyBS2021/MyVideo";
        //检查filepath是否存在
        //如果不存在则新建path
        String filePath=folderPath+"/"+fileName;
        //检查文件是否存在
        File dest = new File(filePath);
        file.transferTo(dest);
        //进行视频取帧

        return "success";
    }
}

