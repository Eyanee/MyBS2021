package com.Eyannee.demons.Controller;


import com.Eyannee.demons.entity.PublishInfo;
import com.Eyannee.demons.entity.Userfile;
import com.Eyannee.demons.entity.uploadData;
import com.Eyannee.demons.entity.userReceived;
import com.Eyannee.demons.service.picOp;
import com.Eyannee.demons.service.userFileService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
public class uploadController {
    private Map<String, Integer> myCounter = new HashMap<String, Integer>();
    @Autowired
    private userFileService myService=new userFileService();
    @RequestMapping(value = "/uploadPics" ,method= RequestMethod.POST)
    public String uploadPic(@RequestParam("file") MultipartFile file,String username, String filename) throws IOException {
        System.out.println("asdf");
        if (file.isEmpty()) {
            return "empty file";
        }

        String fileName = file.getOriginalFilename();
        String fileName1 = fileName.split("\\.")[1];
        //String name = UUIDUtils.getUUID(); // 随机的uuid
        String folderPath = "D:/VueCode/MyBS2021/Myfiles";
        //检查filepath是否存在

        //如果文件夹不存在则创建
        //数据库创建新的userfile项目
        String picfilepath=folderPath+"/"+username+"/"+filename+"/pics";
        String xmlfilepath=folderPath+"/"+username+"/"+filename+"/"+"xml";
        String cocofilepath=folderPath+"/"+username+"/"+filename+"/"+"coco";
        File file1 =new File(picfilepath);
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
            myService.insertUserFile(username,filename,picfilepath,xmlfilepath,cocofilepath);
            file1=new File(xmlfilepath);
            file1.mkdirs();
            file1=new File(cocofilepath);
            file1.mkdirs();
        }

        String filePath=picfilepath+"/"+fileName;
        //检查文件是否存在
        File file2=new File(filePath);
        if(file2.exists()){
            return "file_name already exits";
        }
        //建立新的图片数据库条目
        String filepath=filePath;
        String picname=fileName;
        int pos=fileName.lastIndexOf('.');
        String temp=fileName.substring(0,pos);
        String xmlmarkpath=xmlfilepath+"/"+temp+".xml";
        String cocomarkpath=cocofilepath+"/"+temp+".coco";
        myService.insertNewPic(username,filename,picname,filepath,xmlmarkpath,cocomarkpath);


        File dest = new File(filepath);
        file.transferTo(dest);
        return "success";
    }

    @RequestMapping(value = "/uploadVideo" ,method= RequestMethod.POST)
    public String uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
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
        String folderPath = "D:/VueCode/MyBS2021/demo1Frontend/src/assets/video";
        //检查filepath是否存在
        //如果不存在则新建path
        String filePath=folderPath+"/"+fileName;
        //检查文件是否存在
        File file2=new File(filePath);
        if(file2.exists()){
            return "file_name already exits";
        }
        File dest = new File(filePath);
        file.transferTo(dest);
        return "success";
    }

    @RequestMapping(value = "/videoPics" ,method= RequestMethod.POST)
    public void uploadVideo(String base64Str, String username, String videoname) throws IOException {
        //System.out.println(base64Str);
        //count图片名称计数\
        picOp myOp=new picOp();

        //进行操作
        Integer t;
        String Path="D:/VueCode/MyBS2021/MyPics/"+username;
        String filename=videoname;
        String picfilepath=Path+"/"+videoname;
        String xmlfilepath=Path+"/"+videoname+"/xml";
        String cocofilepath=Path+"/"+videoname+"/coco";
        File file1;
        if(myCounter.get("videoname")==null){
            file1=new File(picfilepath);
            if(file1.isDirectory()){
                filename=filename+"video";
                picfilepath=picfilepath+"video";
                xmlfilepath=xmlfilepath+"video";
                cocofilepath=xmlfilepath+"video";
            }
            file1.mkdirs();
            myCounter.put("videoname",1); //若无序号则置1
            //创建新的用户项目条目
            myService.insertUserFile(username,filename,picfilepath,xmlfilepath,cocofilepath);
            t=1;


            file1=new File(xmlfilepath);
            file1.mkdirs();
            file1=new File(cocofilepath);
            file1.mkdirs();
        }
        else{
            t=myCounter.get("videoname");
            t=t+1;
            myCounter.put("videoname",t);//若有序号则++
        }
        String picname=t+".jpg";
        //Base64转换
        boolean res;
        res=myOp.transferAndSave(base64Str,picname,videoname,username);
        //存储并建立新的数据库条目
        String filepath=picfilepath+"/"+picname;
        String xmlmarkpath=xmlfilepath+"/"+t+".xml";
        String cocomarkpath=cocofilepath+"/"+t+".coco";
        myService.insertNewPic(username,filename,picname,filepath,xmlmarkpath,cocomarkpath);

        System.out.println("transefer and save "+res);
        return;
    }

    @RequestMapping(value = "/getAllPost" ,method= RequestMethod.GET)
    public List<PublishInfo> getAllPost(){

        List<PublishInfo> allPublishInfo = myService.getAllPost();

        //测试怎么返回
        return allPublishInfo;
    }
    @RequestMapping(value = "/getAllRnS" ,method= RequestMethod.GET) //all receive but not submit
    public List<userReceived> getAllRnS(String username){

        List<userReceived> all = myService.getAllRns(username);

        //测试怎么返回
        return all;//全返回但只有一部分数据有用
    }

    @RequestMapping(value = "/setReceive",method = RequestMethod.POST)
    public boolean setReceive(String username,String filename,String receivePerson){
        boolean res;
        res=myService.receivefile(username,filename,receivePerson);
        return res;
    }

    @RequestMapping(value = "/getUnpost")
    public List<String> getUnpost(String username){

        List<String> allUnpost=myService.getUnPost(username);
        return allUnpost;
    }

    @RequestMapping(value = "/release",method = RequestMethod.POST)
    public boolean ReleaseNew(String username,String filename,String des){
        boolean res;
        res=myService.setPublish(username,filename,des," ");
        return res;
    }
}

