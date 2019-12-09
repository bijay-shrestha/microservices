package com.bijay.imageservice.controller;

import com.bijay.imageservice.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class ImageController {

    @Autowired
    private Environment env;

    @GetMapping("/images")
    public List<Image> getImages() {
        return Arrays.asList(
                new Image(1L, "Bryan Stark", "http://helloworld"),
                new Image(1L, "Sansa", "http://helloworld"),
                new Image(1L, "Arya Stark", "http://helloworld")
        );
    }
}
