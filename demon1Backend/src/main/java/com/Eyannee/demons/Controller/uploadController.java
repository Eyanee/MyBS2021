package com.Eyannee.demons.Controller;


import ch.qos.logback.core.util.FileUtil;
import com.Eyannee.demons.entity.*;
import com.Eyannee.demons.service.picOp;
import com.Eyannee.demons.service.userFileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        String s=System.getProperty("user.dir");
        int pos=s.lastIndexOf('\\');
        s=s.substring(0,pos);
        String folderPath =s+"\\Myfiles"  ;//"D:/VueCode/MyBS2021/Myfiles";
        //检查filepath是否存在

        //如果文件夹不存在则创建
        //数据库创建新的userfile项目
        String picfilepath=folderPath+"\\"+username+"\\"+filename+"\\pics";
        String xmlfilepath=folderPath+"\\"+username+"\\"+filename+"\\"+"xmlfile";
        String cocofilepath=folderPath+"\\"+username+"\\"+filename+"\\"+"cocofile";
        File file1 =new File(picfilepath);
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
            myService.insertUserFile(username,filename,picfilepath,xmlfilepath,cocofilepath);
            file1=new File(xmlfilepath);
            file1.mkdirs();
            file1=new File(cocofilepath);
            file1.mkdirs();
        }

        String filePath=picfilepath+"\\"+fileName;
        //检查文件是否存在
        File file2=new File(filePath);
        if(file2.exists()){
            return "file_name already exits";
        }
        //建立新的图片数据库条目
        String filepath=filePath;
        String picname=fileName;
        int p=fileName.lastIndexOf('.');
        String temp=fileName.substring(0,p);
        String xmlmarkpath=xmlfilepath+"\\"+temp+".xml";
        String cocomarkpath=cocofilepath+"\\"+temp+".coco";
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

        String s=System.getProperty("user.dir");
        int p=s.lastIndexOf('\\');
        s=s.substring(0,p);
        String folderPath =s+"\\Myfiles"  ;
        //String folderPath = "D:/VueCode/MyBS2021/demo1Frontend/src/assets/video";
        //检查filepath是否存在
        //如果不存在则新建path
        String filePath=folderPath+"\\"+fileName;
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
    public void dealVideoPics(@RequestParam("base64Str[]") String[] base64Str, String username, String videoname) throws IOException {
        for(int i=0;i<base64Str.length;i++){
            uploadVideo(base64Str[i],username,videoname);
        }
    }

    public void uploadVideo(String base64Str, String username, String videoname) throws IOException {
        //System.out.println(base64Str);
        //count图片名称计数\
        picOp myOp=new picOp();

        //进行操作
        Integer t;
        String s=System.getProperty("user.dir");
        int pos=s.lastIndexOf('\\');
        s=s.substring(0,pos);
        String folderPath =s+"\\Myfiles"  ;
        String Path=folderPath+"\\"+username;
        String filename=videoname;
        int p=filename.lastIndexOf('.');
        filename=filename.substring(0,p);
        String picfilepath=Path+"\\"+filename+"\\pics";
        String xmlfilepath=Path+"\\"+filename+"\\xmlfile";
        String cocofilepath=Path+"\\"+filename+"\\cocofile";
        File file1;
        if(myCounter.get(videoname)==null){
            file1=new File(picfilepath);
            file1.mkdirs();
            myCounter.put(videoname,1); //若无序号则置1
            //创建新的用户项目条目
            myService.insertUserFile(username,filename,picfilepath,xmlfilepath,cocofilepath);
            t=1;


            file1=new File(xmlfilepath);
            file1.mkdirs();
            file1=new File(cocofilepath);
            file1.mkdirs();
        }
        else{
            t=myCounter.get(videoname);
            t=t+1;
            myCounter.put(videoname,t);//若有序号则++
        }
        String picname=t+".jpg";
        //Base64转换
        boolean res;
        res=myOp.transferAndSave(base64Str,picname,filename,username);
        //存储并建立新的数据库条目
        String filepath=picfilepath+"\\"+picname;
        String xmlmarkpath=xmlfilepath+"\\"+t+".xml";
        String cocomarkpath=cocofilepath+"\\"+t+".coco";
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
        res=myService.receivefile(receivePerson,filename,username);
        return res;
    }

    @RequestMapping(value = "/getUnpost")
    public List<String> getUnpost(String username){

        List<String> allUnpost=myService.getUnPost(username);
        return allUnpost;
    }

    @RequestMapping(value = "/getnoSubmit",method = RequestMethod.GET)
    public List<PublishInfo> getnoSubmit(String username){

        List<PublishInfo> allnoSubmit=myService.getnoSubmit(username);
        return allnoSubmit;
    }

    @RequestMapping(value = "/setSubmit",method = RequestMethod.POST)
    public boolean setSubmit(String username,String filename,String publishername){
        boolean res;
        res=myService.setSubmit(username, filename,publishername);
        return res;
    }
    @RequestMapping(value = "/getMyPost",method = RequestMethod.GET)
    public List<PublishInfo> getMyPost(String username){

        List<PublishInfo> allnoSubmit=myService.getMyPost(username);
        return allnoSubmit;
    }

    @RequestMapping(value = "/release",method = RequestMethod.POST)
    public boolean ReleaseNew(String username,String filename,String des){
        boolean res;
        res=myService.setPublish(username,filename,des," ");
        return res;
    }
    @RequestMapping(value = "/deletePic",method = RequestMethod.POST)
    public boolean deletePic(String picname,String filename,String username){
        boolean res;
        //只在数据库中删除
        res=myService.deletepic(username,filename,picname);
        return res;
    }
    @RequestMapping(value = "/getMarkedPics",method = RequestMethod.GET)
    public List<Picture> getMarkedPics(String username){
        boolean res;
        //只在数据库中删除
        List<Picture>temp;
        temp=myService.getMarkedPics(username);
        return temp;
    }

    @GetMapping("/downloadxml/{user}/{file}/{pic}")
    public void downloadXML(@PathVariable("user") String username, @PathVariable("file") String filename,
                             @PathVariable("pic") String picname,
                             HttpServletRequest request,
                             HttpServletResponse response) throws FileNotFoundException {


        //1.获取文件绝对路径
        String xmlpath=myService.getxmlpath(username,filename,picname);
        //2.通过绝对路径定义File
        File f=new File(xmlpath);
        //3.调用FileUtil下载文件
        downloadFile(request,response,xmlpath,false);
    }

    @GetMapping("/downloadcoco/{user}/{file}/{pic}")
    public void downloadCoco(@PathVariable("user") String username, @PathVariable("file") String filename,
                             @PathVariable("pic") String picname,
                             HttpServletRequest request,
                             HttpServletResponse response) throws FileNotFoundException {


        //1.获取文件绝对路径
        String cocopath=myService.getcocopath(username,filename,picname);
        //2.通过绝对路径定义File
        File f=new File(cocopath);
        //3.调用FileUtil下载文件
        downloadFile(request,response,cocopath,false);
    }
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String filepath, boolean deleteOnExit) throws FileNotFoundException {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        File file=new File(filepath);
        fis = new FileInputStream(file);
        try {
            //response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    if (deleteOnExit) {
                        //file.deleteOnExit();
                        //file.delete();
                        System.out.println("已删除");
                    }
                } catch (IOException e) {
                    //log.error(e.getMessage(), e);
                }
            }
        }
    }

}

