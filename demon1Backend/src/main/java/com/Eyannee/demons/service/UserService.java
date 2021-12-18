package com.Eyannee.demons.service;

import com.Eyannee.demons.entity.Book;
import com.Eyannee.demons.entity.User;
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
                MyUser.setId(rs.getInt("id"));
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
                MyUser.setId(rs.getInt("id"));
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
}
