package com.Eyannee.demons.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class TestController {

    @GetMapping( "/")
    public String hello(){

        return "demons";
    }
    @RequestMapping("/index")
    public  String TestHello(){
        return "这样也可吧";
    }
}
