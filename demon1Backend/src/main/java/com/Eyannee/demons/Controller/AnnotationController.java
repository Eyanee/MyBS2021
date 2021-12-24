package com.Eyannee.demons.Controller;

import com.Eyannee.demons.service.XmlFile;
import com.Eyannee.demons.service.userFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class AnnotationController {

    @Autowired
    private XmlFile myXmlService = new XmlFile();
    @Autowired
    private userFileService picService=new userFileService();

    @RequestMapping(value = "/annotation",method = RequestMethod.POST)
    public boolean getAnnotationData(String username, String filename, String picname,
                                     String height, String width,@RequestParam("xmin[]")String[] xmin,
                                     @RequestParam("xmax[]")String[] xmax, @RequestParam("ymin[]")String[] ymin,
                                     @RequestParam("ymax[]")String[] ymax, @RequestParam("tagName[]")String[] tagName,
                                     @RequestParam("tag[]")String[] tag) throws IOException {
        //先更新数据库相关信息
        picService.setMarked(username,filename,picname);
        //写文件操作
        boolean res;
        System.out.println("whatever");
        res=myXmlService.writeXML(username,filename,picname,height,width,xmin,xmax,ymin,ymax,tagName);
        if(!res){
            System.out.println("write xml failed!");
            return false;
        }
        int length=tag.length;
        String[] segmentation = new String[length];
        String[] area= new String[length];
        String[] bbox= new String[length];
        int x1,x2;
        for(int i=0;i<length;i++){
            segmentation[i]="["+xmin[i]+","+ymax[i]+","+xmax[i]+
                    ","+ymax[i]+","+xmin[i]+","+ymin[i]+","+xmax[i]+","+ymin[i]+"]";
            x1=Integer.parseInt(xmax[i])-Integer.parseInt(xmin[i]);
            x2=Integer.parseInt(ymax[i])-Integer.parseInt(ymin[i]);
            area[i]=String.valueOf(x1*x2);
            bbox[i]="["+xmin[i]+","+ymin[i]+","+x1+","+x2+"]";
        }
        res=myXmlService.writeCoCo(username,filename,picname,height,width,xmin,xmax,ymin,ymax,tagName,tag,segmentation,
                area,bbox);
        return res;
    }
}
