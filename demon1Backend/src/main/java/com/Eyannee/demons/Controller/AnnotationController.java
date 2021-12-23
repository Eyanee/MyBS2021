package com.Eyannee.demons.Controller;

import com.Eyannee.demons.service.XmlFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AnnotationController {

    @Autowired
    private XmlFile myXmlService = new XmlFile();

    public boolean getAnnotationData(String username,String filename,String picname,
                                     String height,String width,String[] xmin,String[] xmax,String[] ymin,
                                     String[] ymax,String[] tagName,String[] tag) throws IOException {
        boolean res;
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
