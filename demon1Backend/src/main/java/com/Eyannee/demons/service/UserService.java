package com.Eyannee.demons.service;

import com.Eyannee.demons.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<User> listAll() {
        //jdbcTemplate=t;
        String sql = "select * from user";
        List<User> userList = jdbcTemplate.query(sql, new RowMapper<User>() {
            User MyUser = null;
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUser = new User();
                //MyUser.setId(rs.getInt("id"));
                MyUser.setName(rs.getString("name"));
                MyUser.setPassword(rs.getString("password"));
                MyUser.setEmail(rs.getString("email"));
                MyUser.setPhone(rs.getString("phone"));
                return MyUser;
            }
        });
        for (User user : userList) {
            System.out.println(user.toString());
        }
        return userList;
    }
    public List<User> selectUser(String ss) {
        String sql = "SELECT * FROM user where name= "+"'"+ss+"'";
        System.out.println(sql);
        List<User> userList = jdbcTemplate.query(sql, new RowMapper<User>() {
            User MyUser = null;
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUser = new User();
                //MyUser.setId(rs.getInt("id"));
                MyUser.setName(rs.getString("name"));
                MyUser.setPassword(rs.getString("password"));
                MyUser.setEmail(rs.getString("email"));
                MyUser.setPhone(rs.getString("phone"));
                return MyUser;
            }
        });
        return userList;
    }


    public boolean checkUserValid(String Name){
        List<User> tUser;
        tUser=listAll();
        for(User temp:tUser){
            if(temp.getName().equals(Name)){
                return true;
            }
        }
        return false;
    }
    public boolean checkEmailValid(String Email){
        List<User> tUser;
        tUser=listAll();
        for(User temp:tUser){
            if(temp.getEmail().equals(Email)){
                return true;
            }
        }
        return false;
    }
    public boolean checkPSWValid(String Name,String psw){
        List<User> tUser;
        tUser=selectUser(Name);
        User temp=tUser.get(0);
        if(temp.getPassword().equals(psw)){
            return true;
        }
        return false;
    }
    public boolean newUser(User t){
        return true;
    }
    public String selectEmail(String username){
        List<User> tUser;
        tUser=selectUser(username);
        User temp=tUser.get(0);
        String email=temp.getEmail();
        System.out.println("email is "+email);
        return email;
    }
    public User updateUser(String username,String password,String email,String previousName){
        //先检查是否冲突
        String sql="select * from user where name <> '"+ previousName+"'";
        List<User> userList = jdbcTemplate.query(sql, new RowMapper<User>() {
            User MyUser = null;
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUser = new User();
                MyUser.setName(rs.getString("name"));
                MyUser.setPassword(rs.getString("password"));
                MyUser.setEmail(rs.getString("email"));
                MyUser.setPhone(rs.getString("phone"));
                return MyUser;
            }
        });
        boolean res=true;
        for(User temp:userList){
            if(temp.getName().equals(username)){
                res=false;
                break;
            }
            if(temp.getEmail().equals(email)){
                res=false;
                break;
            }
        }
        User newUser=new User();
        if(res==true){
            sql="update user set name='"+username+"',email='"+email+"',password='"+password+"' where name ='"+
                    previousName+"'";
            jdbcTemplate.update(sql);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setName(username);
            newUser.setId(0);
            newUser.setPhone("1245");//set default值以防传不进去
            return newUser;
        }
        sql="select * from user where name='" + previousName+"'";
        List<User> oldUser = jdbcTemplate.query(sql, new RowMapper<User>() {
            User MyUser = null;
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUser = new User();
                MyUser.setName(rs.getString("name"));
                MyUser.setPassword(rs.getString("password"));
                MyUser.setEmail(rs.getString("email"));
                MyUser.setPhone(rs.getString("phone"));
                return MyUser;
            }
        });
        newUser=oldUser.get(0);
        return newUser;
    }

    //检查邮箱格式
    public boolean checkEmailForm(String Email){
        String regex = "^(.+)@(.+)$";
        if(Email.matches(regex)){
            return true;
        }
        else return false;
    }

    public boolean insertUser(String UserName,String Password,String Email,String Phone){
        String sql="insert into user values('"+UserName+"','"+Password+"','"+Email+"','"+Phone+"')";
        int res;
        res=jdbcTemplate.update(sql);
        if(res>0){
            return true;
        }
        else return false;
    }

    public Integer getPicNum(String username){
        String sql="select * from picture where username= '"+username+"'";
        List<Picture> allPics = jdbcTemplate.query(sql, new RowMapper<Picture>() {
            Picture MyPic = null;
            @Override
            public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPic = new Picture();
                MyPic.setUsername(rs.getString("username"));
                return MyPic;
            }
        });
        Integer res=allPics.size();
        return res;
    }

    public Integer getReceiveNum(String username){
        String sql="select * from userReceived where username='"+username+"'";
        List<userReceived> allReceievd = jdbcTemplate.query(sql, new RowMapper<userReceived>() {
            userReceived MyReceived = null;
            @Override
            public userReceived mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyReceived = new userReceived();
                MyReceived.setUsername(rs.getString("username"));
                return MyReceived;
            }
        });
        Integer res=allReceievd.size();
        return res;
    }

    public Integer getReleaseNum(String username){
        String sql="select * from publishInfo where username='"+username+"'";
        List<PublishInfo> allInfo = jdbcTemplate.query(sql, new RowMapper<PublishInfo>() {
            PublishInfo MyInfo = null;
            @Override
            public PublishInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyInfo = new PublishInfo();
                MyInfo.setUsername(rs.getString("username"));
                return MyInfo;
            }
        });
        Integer res=allInfo.size();
        return res;
    }
}
