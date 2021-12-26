package com.Eyannee.demons.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class XmlFile {

    public boolean writeXML(String username,String filename,String picname,
                            String height,String width,String[] xmin,String[] xmax,String[] ymin,
                            String[] ymax,String[] tags) throws IOException {
        Document doc = DocumentHelper.createDocument();

        Document document = DocumentHelper.createDocument();
        Element annotation = document.addElement("annotation");
        Element user=annotation.addElement("username").addText(username);
        Element file=annotation.addElement("file").addText(filename);
        Element pic=annotation.addElement("picname").addText(picname);
        Element size=annotation.addElement("size");
        Element thiswidth=size.addElement("width").addText(width);
        Element thisheight=size.addElement("height").addText(height);
        for(int i=0;i<tags.length;i++){
            Element tempObj=annotation.addElement("object");
            Element bndbox=tempObj.addElement("bndbox");
            Element Xmin=bndbox.addElement("xmin").addElement(xmin[i]);
            Element Xmax=bndbox.addElement("xmax").addText(xmax[i]);
            Element Ymin=bndbox.addElement("ymin").addText(ymin[i]);
            Element Ymax=bndbox.addElement("ymax").addText(ymax[i]);

        }
        OutputFormat format = new OutputFormat();
        format.setIndentSize(2);  // 行缩进
        format.setNewlines(true); // 一个结点为一行
        format.setTrimText(true); // 去重空格
        format.setPadText(true);
        format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行

        // 输出xml文件
        String s=System.getProperty("user.dir");
        int pos=s.lastIndexOf('\\');
        s=s.substring(0,pos);
        String folderPath =s+"\\Myfiles"  ;
        String xmlfolderpath=folderPath+"\\"+username+"\\"+filename+"\\xmlfile";
        File file1=new File(xmlfolderpath);
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
        }
        int p=picname.lastIndexOf('.');
        String temp=picname.substring(0,p);
        String xmlfilepath=xmlfolderpath+"\\"+temp+".xml";
        File file2=new File(xmlfilepath);
        XMLWriter writer = new XMLWriter(new FileOutputStream(file2), format);

        writer.write(document);
        System.out.println("dom4j CreateDom4j success!");
        return true;
    }
    public boolean writeCoCo(String username,String filename,String picname,
                            String height,String width,String[] xmin,String[] xmax,String[] ymin,
                            String[] ymax,String[] tagName,String[] tags,
                             String[] segmentation,String[]area,String[]bbox) throws IOException {

        JSONObject result = new JSONObject();

        JSONArray annotations=new JSONArray();
        JSONArray categories = new JSONArray();
        String s;

        //写annotations字段
        for(int i=0;i<tags.length;i++){
            JSONObject annotation = new JSONObject();
            annotation.put("id",1);
            annotation.put("category_id",tags[i]);
            annotation.put("segmentation",segmentation[i]);//重新传入相关数据,四个点的坐标值
            annotation.put("area",area[i]);//重新传入相关数据，计算面积
            annotation.put("bbox",bbox[i]);//重新传入数据[x,y,width,height]
            annotation.put("iscrowd",0);
            annotations.add(annotation);
        }

        //写categories字段
        Map<Integer,String>myTag = new HashMap<>();
        Integer no;
        String text;
        for(int i=0;i<tags.length;i++){
            int pos=tags[i].indexOf('x');
            String t=tags[i].substring(pos+1);
            no=Integer.valueOf(t,16);
            text=tagName[i];
            myTag.put(no,text);
        }

        //写categories字段
        for (Map.Entry<Integer, String> entry : myTag.entrySet()) {
           // System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            JSONObject category = new JSONObject();
            category.put("id",entry.getKey());
            category.put("name",entry.getValue());
            categories.add(category);
        }
        //写info字段 随便抄的
        JSONObject info=new JSONObject();
        info.put("description","COCO 2017 Dataset");
        info.put("url","http://cocodataset.org");
        info.put("version","1.0");
        info.put("year",2017);
        info.put("contributor","COCO Consortium");

        //写licences字段
        JSONObject licenses=new JSONObject();
        licenses.put("url","http://creativecommons.org/licenses/by-nc-sa/2.0/");
        licenses.put("id",1);
        licenses.put("name","Attribution-ShareAlike License");

        //写images字段
        JSONObject images=new JSONObject();
        images.put("license",4);
        images.put("file_name",filename);
        images.put("height",height);
        images.put("width",width);
        result.element("info",info);
        result.element("licenses",licenses);
        result.element("images",images);
        result.element("annotations",annotations);
        result.element("categories",categories);

        String ss=System.getProperty("user.dir");
        int pos=ss.lastIndexOf('\\');
        ss=ss.substring(0,pos);
        String folderPath =ss+"\\Myfiles"  ;
        String cocofolderpath=folderPath+"\\"+username+"\\"+filename+"\\cocofile";
        File file1=new File(cocofolderpath);
        if  (!file1.exists()  && !file1.isDirectory()){
            file1.mkdirs();
        }
        int p=picname.lastIndexOf('.');
        String temp=picname.substring(0,p);
        String cocofilepath=cocofolderpath+"\\"+temp+".coco";
        File file2=new File(cocofilepath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file2),"UTF-8");
        BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
        String jsonString=result.toString();//将jsonarray数组转化为字符串
        //String JsonString=tool.stringToJSON(jsonString);//将jsonarrray字符串格式化
        bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
        bufferedWriter.flush();//清空缓冲区，强制输出数据
        bufferedWriter.close();//关闭输出流
        return true;
    }


}
