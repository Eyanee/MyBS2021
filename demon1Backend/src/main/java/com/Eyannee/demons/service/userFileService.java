package com.Eyannee.demons.service;

import com.Eyannee.demons.entity.*;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Service
public class userFileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //插入新的userfile项
    public boolean insertUserFile(String username,String filename,
                                  String picfilepath,String xmlfilepath,String cocofilepath){
        String tSql="select * from userfile where username='"+username+"' and filename='"+filename+"'";

        List<Userfile> userList=jdbcTemplate.query(tSql, new RowMapper<Userfile>() {
            Userfile MyUserfile = null;
            @Override
            public Userfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUserfile = new Userfile();
                //MyUser.setId(rs.getInt("id"));
                MyUserfile.setReceived(rs.getBoolean("isReceived"));
                return MyUserfile;
            }
        });
        if(userList.size()>0){
            return true;
        }
        picfilepath=picfilepath.replace("\\","\\\\");
        xmlfilepath=xmlfilepath.replace("\\","\\\\");
        cocofilepath=cocofilepath.replace("\\","\\\\");

        String sql="insert into userfile values('"+username+"','"+filename+"','"+picfilepath
                +"',false,false,false,false,'"
                +xmlfilepath+"','"+cocofilepath+"')";
        int res;
        res=jdbcTemplate.update(sql);
        return res > 0;
    }



    //插入新的图片项
    public boolean insertNewPic(String username,String filename,
                                String picname,String filepath,String xmlpath, String cocopath){
        String picfilepath=filepath.replace("\\","\\\\");
        String xmlfilepath=xmlpath.replace("\\","\\\\");
        String cocofilepath=cocopath.replace("\\","\\\\");
        String sql="insert into picture values('"+username+"','"+filename+"','"+picname
                +"','"+picfilepath+"',false,'"+xmlfilepath+"','"+cocofilepath+"')";

        int res;
        res=jdbcTemplate.update(sql);
        return res > 0;
    }
    public boolean deletepic(String username,String filename,String picname){
        String sql="delete from picture where username='"+username+"' and filename='"+
                filename+"' and picname='"+picname+"'";
        int res;
        res=jdbcTemplate.update(sql);
        return res>0;
    }

    //接受任务的数据库
    public boolean receivefile(String username, String filename, String publishername){
        //先查询相关信息
        //检查是否已被领取是否存在
        boolean res;
        res=checkFileExist(publishername,filename);
        if(!res){
            return false;
        }
        //创建新的recieve条目
        res=setNewReceive(username,filename,publishername);
        if(!res){
            return false;
        }
        //set相关为已领取
        res=AlterInfo(publishername,filename,username);
        if(!res){
            return false;
        }
        res=ALterPublisher(publishername,filename,username);
        if(!res){
            return false;
        }
        return true;
    }
    public boolean setNewReceive(String username,String filename,String publishername){
        String sql="select picfilepath from userfile where username= '"+publishername+"' and filename = '"+filename+"'";
        List<Userfile> userList=jdbcTemplate.query(sql, new RowMapper<Userfile>() {
            Userfile MyUserfile = null;
            @Override
            public Userfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUserfile = new Userfile();
                //MyUser.setId(rs.getInt("id"));
                MyUserfile.setPicfilepath(rs.getString("picfilepath"));
                return MyUserfile;
            }
        });
        if(userList.size()==0){
            return false;
        }
        String picfilepath=userList.get(0).getPicfilepath();
        picfilepath=picfilepath.replace("\\","\\\\");
        sql="insert into userReceived values('"+username+"','"+filename+"','"+publishername+"',false,false,'"+
                picfilepath+"')";
        int res;
        res=jdbcTemplate.update(sql);
        return res > 0;
    }

    public boolean AlterInfo(String username,String filename,String receivePerson){
        String sql="update publishinfo set isReceive = true,receivePerson='"+
                receivePerson+"'where username= '"+username+"' and filename = '"
                +filename+"'";
        int res;
        res=jdbcTemplate.update(sql);
        return res > 0;
    }

    public boolean ALterPublisher(String username,String filename,String ReceivedPerson){
        String sql="update userfile set isReceived = true where username= '"+username+"' and filename = '"
                +filename+"'";
        int res;
        res=jdbcTemplate.update(sql);
        return res > 0;
    }

    public boolean checkFileExist(String username, String filename){

        String sql= "select isReceived from userfile where username= '"+username+"' and filename = '"+filename+"'";
        List<Userfile> userList=jdbcTemplate.query(sql, new RowMapper<Userfile>() {
            Userfile MyUserfile = null;
            @Override
            public Userfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUserfile = new Userfile();
                //MyUser.setId(rs.getInt("id"));
                MyUserfile.setReceived(rs.getBoolean("isReceived"));
                return MyUserfile;
            }
        });
        if(userList.size()==0){
            return false;
        }
        else if(userList.get(0).getIsReceived()){
            return false;
        }
        return true;

    }

    //发布任务时的数据库接口
    public boolean setPublish(String username,String filename, String desInfo,String ReceivedPerson){
        //两项记录
        String sql="insert into publishInfo values ('"+username+"','"+filename+"','"+
                desInfo+"','"+ReceivedPerson+"',false,false)";
        int res;
        res=jdbcTemplate.update(sql);
        if(res<=0){
            return false;
        }
        //setfilePublished
        sql="update userfile set isPublish = true where username='"+username+"'and filename='"+filename+"'";
        res=jdbcTemplate.update(sql);
        if(res<=0){
            return false;
        }
        return true;
    }

    public boolean setMarked(String username, String filename, String picname){
        String sql="update picture set isMarked=true where username='"+username+"' and filename ='"+filename
                +"'and picname ='"+picname+"'";
        int res;
        res=jdbcTemplate.update(sql);
        if(res<=0){
            return false;
        }
        return true;
    }

    public boolean checkFinshed(String username,String filename,String publisher){
        //可能要检查一下是否已完成
        String sql="select isMarked from picture where username='"+username+"'and filename='"+filename+"'";
        List<Picture> picList=jdbcTemplate.query(sql, new RowMapper<Picture>() {
            Picture MyPicture = null;
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPicture = new Picture();
                MyPicture.setMarked(rs.getBoolean("isMarked"));
                return MyPicture;
            }
        });
        boolean res = true;
        for(Picture temp:picList){
            if(!temp.getIsMarked()){
                res=false;
                break;
            }
        }
        if(!res){
            return false;
        }
        sql="update userReceived set isFinished= true where username='"+username+
                "' and filename='"+filename+"'and publishername ='"+publisher+"'";

        int t;
        t=jdbcTemplate.update(sql);
        if(t<=0){
            return false;
        }
        return true;
    }
    public boolean setSubmit(String username,String filename,String publisher){
        boolean res;
//        res=checkFinshed(username,filename,publisher);
//        if(!res){
//            return false;//还有未完成标注任务
//        }
        String sql="update userReceived set isSubmit = true where username='"+username+"' and filename ='"
                +filename+"'";
        int t;
        t=jdbcTemplate.update(sql);
        if(t<=0){
            return false;
        }
        sql="update userfile set isFinished = true where username ='"+publisher+"' and filename='"+filename+"'";
        t=jdbcTemplate.update(sql);
        if(t<=0){
            return false;
        }
        return true;
    }

    public boolean setConfirmed(String username,String filename){
        String sql="update userfile set isConfirmed=true where username='"+username+"' and filename='"+filename+"'";
        int res;
        res=jdbcTemplate.update(sql);
        if(res<=0){
            return false;
        }
        return true;
    }

    public List<PublishInfo> getAllPost(){
        String sql="select * from publishinfo where isReceive = false";
        List<PublishInfo> allPost = jdbcTemplate.query(sql, new RowMapper<PublishInfo>() {
            PublishInfo onePost = null;
            @Override
            public PublishInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost = new PublishInfo();
                //MyUser.setId(rs.getInt("id"));
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setDesInfo(rs.getString("desInfo"));
                onePost.setReceivedPerson(rs.getString("receivePerson"));
                onePost.setReceived(rs.getBoolean("isReceive"));
                return onePost;
            }
        });
        return allPost;
    }

    public List<PublishInfo> getnoSubmit(String username){
        String sql="select * from publishInfo where isSubmit = false and receivePerson='"+username+"'";
        List<PublishInfo> allPost = jdbcTemplate.query(sql, new RowMapper<PublishInfo>() {
            PublishInfo onePost = null;
            @Override
            public PublishInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost = new PublishInfo();
                //MyUser.setId(rs.getInt("id"));
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setReceived(rs.getBoolean("isReceive"));
                onePost.setReceivedPerson(rs.getString("receivePerson"));
                onePost.setDesInfo(rs.getString("desInfo"));
                return onePost;
            }
        });
        return allPost;
    }

    public List<String> getUnPost(String username){
        String sql="select filename from userfile where isPublish= false and username ='"+username+"'";
        List<Userfile> allPost = jdbcTemplate.query(sql, new RowMapper<Userfile>() {
            Userfile onePost = new Userfile();
            @Override
            public Userfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost = new Userfile();
                onePost.setFilename(rs.getString("filename"));
                return onePost;
            }
        });
        List<String> newList=new LinkedList<>();
        for(Userfile temp:allPost){
            newList.add(temp.getFilename());
        }
        return newList;
    }

    public List<String> forPicPath(String username,String filename){
        String sql="select * from picture where username='"+username+"' and filename='"+filename+"'";
        List<Picture> allPost = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                Picture onePost = new Picture();
                onePost.setFilepath(rs.getString("filepath"));
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setPicname(rs.getString("picname"));
                onePost.setMarked(rs.getBoolean("isMarked"));
                onePost.setXmlpath(rs.getString("xmlmarkpath"));
                onePost.setCocopath(rs.getString("cocomarkpath"));
                return onePost;
            }
        });
        List<String> res = new LinkedList<>();
        for(Picture temp:allPost){
            String s=temp.getFilepath();
            res.add(s);
        }

        return res;
    }


    public List<upPicInfo> forupInfo(String username,String filename){
        String sql="select * from picture where username='"+username+"' and filename='"+filename+"'";
        List<Picture> allPost = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                Picture onePost = new Picture();
                onePost.setFilepath(rs.getString("filepath"));
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setPicname(rs.getString("picname"));
                onePost.setMarked(rs.getBoolean("isMarked"));
                onePost.setXmlpath(rs.getString("xmlmarkpath"));
                onePost.setCocopath(rs.getString("cocomarkpath"));
                return onePost;
            }
        });
        List<upPicInfo> res = new LinkedList<>();
        for(Picture temp:allPost){
            upPicInfo one=new upPicInfo();
            String s=temp.getFilepath();
            boolean t=temp.getIsMarked();
            one.setMarked(t);
            one.setFilepath(s);
            res.add(one);
        }
        return res;
    }

    public List<userReceived> getAllRns(String username){
        String sql="select * from userReceived where username ='"+
                username +"' and isSubmit=false";

        List<userReceived> allPost = jdbcTemplate.query(sql, new RowMapper<userReceived>() {
            userReceived onePost = new userReceived();
            @Override
            public userReceived mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost.setFilename(rs.getString("filename"));
                onePost.setFinished(rs.getBoolean("isFinished"));
                onePost.setSubmit(rs.getBoolean("isSubmit"));
                onePost.setUsername(rs.getString("username"));
                onePost.setPublisher(rs.getString("publishername"));
                onePost.setPicfilepath(rs.getString("picfilepath"));
                return onePost;
            }
        });
        return allPost;
    }

    public List<PublishInfo> getMyPost(String username){
        String sql="select * from publishinfo where username='"+username+"'";

        List<PublishInfo> allPost = jdbcTemplate.query(sql, new RowMapper<PublishInfo>() {
            PublishInfo onePost = new PublishInfo();
            @Override
            public PublishInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setDesInfo(rs.getString("desInfo"));
                onePost.setReceivedPerson(rs.getString("receivePerson"));
                onePost.setSubmit(rs.getBoolean("isSubmit"));
                return onePost;
            }
        });
        return allPost;
    }

    public List<Picture> getMarkedPics(String username){
        String sql="select * from picture where username='"+username+"' and isMarked= true";

        List<Picture> allPost = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            Picture onePost = new Picture();
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setFilepath(rs.getString("filepath"));
                onePost.setPicname(rs.getString("picname"));
                onePost.setCocopath(rs.getString("cocomarkpath"));
                onePost.setXmlpath(rs.getString("xmlmarkpath"));
                onePost.setMarked(rs.getBoolean("isMarked"));
                return onePost;
            }
        });
        return allPost;
    }
    public String getxmlpath(String username,String filename,String picname){
        String sql="select * from picture where username='"+username+"' and filename='"+
                filename+"'and picname='"+picname+"'";

        List<Picture> allPost = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            Picture onePost = new Picture();
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setFilepath(rs.getString("filepath"));
                onePost.setPicname(rs.getString("picname"));
                onePost.setCocopath(rs.getString("cocomarkpath"));
                onePost.setXmlpath(rs.getString("xmlmarkpath"));
                onePost.setMarked(rs.getBoolean("isMarked"));
                return onePost;
            }

        });
        return allPost.get(0).getXmlpath();
    }

    public String getcocopath(String username,String filename,String picname){
        String sql="select * from picture where username='"+username+"' and filename='"+
                filename+"'and picname='"+picname+"'";

        List<Picture> allPost = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            Picture onePost = new Picture();
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                onePost.setUsername(rs.getString("username"));
                onePost.setFilename(rs.getString("filename"));
                onePost.setFilepath(rs.getString("filepath"));
                onePost.setPicname(rs.getString("picname"));
                onePost.setCocopath(rs.getString("cocomarkpath"));
                onePost.setXmlpath(rs.getString("xmlmarkpath"));
                onePost.setMarked(rs.getBoolean("isMarked"));
                return onePost;
            }

        });
        return allPost.get(0).getCocopath();
    }

}
