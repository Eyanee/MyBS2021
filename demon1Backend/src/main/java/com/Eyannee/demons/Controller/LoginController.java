package com.Eyannee.demons.Controller;

import com.Eyannee.demons.entity.User;
import com.Eyannee.demons.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//@Controller
@RestController
public class LoginController {

    @Autowired
    private UserService myServer=new UserService();

    @RequestMapping(value = "/login" ,method= RequestMethod.POST)
    @ResponseBody
    public boolean CheckPwd(String UserName,String Password){
        boolean res;
        res=myServer.checkUserValid(UserName);
        if(!res){
            return false;
        }
        res=myServer.checkPSWValid(UserName,Password);
        if(!res){
            return false;
        }
        return true;
    }
    @RequestMapping("/newUser")
    public boolean InsertUser(User t){
        boolean res;
        res=myServer.newUser(t);
        return res;
    }
    @RequestMapping("/foremail")
    public String AskForEmail(String username){
        String temp;
        temp=myServer.selectEmail(username);
        return temp;
    }
    @RequestMapping("/Edit")
    public boolean UpdateInfo(String UserName,String Password,String Email){
        boolean res;
        res=myServer.updateUser(UserName,Password,Email);
        return res;
    }
}
