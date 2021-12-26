package com.Eyannee.demons.Controller;

import com.Eyannee.demons.entity.Picture;
import com.Eyannee.demons.entity.upPicInfo;
import com.Eyannee.demons.service.picOp;
import com.Eyannee.demons.service.userFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class PictureController {

    @Autowired
    public userFileService fileService=new userFileService();
    @Autowired
    public picOp picService=new picOp();
    //返回值待修改
    @RequestMapping(value = "/getPics" ,method= RequestMethod.GET)
    public List<String> getAllPics(String username,String filename) throws IOException {

        String filepath;
        List<String> base64Str,allpicpath;
        //向数据库查询路径，取出路径
        allpicpath=fileService.forPicPath(username,filename);

        //传入路径，查询图片并整合为base64字符串
        base64Str=picService.transferAndGet(allpicpath);

        //base64字符串传入前端；
        return base64Str;
    }

    @RequestMapping(value = "/getFilePicsData" ,method= RequestMethod.GET)
    public List<upPicInfo> getFilePicsData(String username, String filename) throws IOException {
        String filepath;
        List<String> base64Str;
        List<String>allpath = new LinkedList<>();
        List<upPicInfo>allpicpath;
        List<upPicInfo> all=new LinkedList<>();
        //向数据库查询路径，取出路径,同时要取出mark信息
        allpicpath=fileService.forupInfo(username,filename);
        for(int i=0;i<allpicpath.size();i++){
            String t= allpicpath.get(i).getFilepath();
            allpath.add(t);
        }
        //传入路径，查询图片并整合为base64字符串
        base64Str=picService.transferAndGet(allpath);
        for(int i=0;i<allpicpath.size();i++){
            String tStr= allpicpath.get(i).getFilepath();
            int pos=tStr.lastIndexOf("\\");
            String thisFilename=tStr.substring(pos+1);
            allpicpath.get(i).setFilename(thisFilename);
            allpicpath.get(i).setBase64str(base64Str.get(i));
        }
        return allpicpath;
    }
}
