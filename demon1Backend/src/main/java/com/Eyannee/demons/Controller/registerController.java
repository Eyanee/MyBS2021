package com.Eyannee.demons.Controller;


import com.Eyannee.demons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class registerController {

    @Autowired
    private UserService myServer=new UserService();

    @RequestMapping(value = "/register" ,method= RequestMethod.POST)
    public String registerUser(String UserName, String Password, String Email, String Phone){
        String res;
        res=myServer.insertUser(UserName,Password,Email,Phone);
        return res;
    }
}
