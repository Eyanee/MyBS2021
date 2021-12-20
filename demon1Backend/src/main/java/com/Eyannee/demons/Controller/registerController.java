package com.Eyannee.demons.Controller;


import com.Eyannee.demons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class registerController {

    @Autowired
    private UserService myServer=new UserService();

    @RequestMapping(value = "/register" ,method= RequestMethod.POST)
    @ResponseBody
    public String registerUser(String UserName, String Password, String Email, String Phone){
        boolean res;
        if(!myServer.checkEmailForm(Email)){
            return "errorAddress";
        }
        res = myServer.checkUserValid(UserName);
        if(res){
            return "duplicateName";
        }
        res=myServer.checkEmailValid(Email);
        if (res) {
            return "duplicateEmail";
        }
        res = myServer.insertUser(UserName,Password,Email,Phone);
        if(res == true){
            return "success";
        }
        else {
            return "false";
        }
    }

}
