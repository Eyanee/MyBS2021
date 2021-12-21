package com.Eyannee.demons.Controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class uploadController {

    @PostMapping ("/uploadPics")
    public void test(@RequestParam("file") MultipartFile file){
        System.out.println("asdf");
    }
}

