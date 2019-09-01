package com.bijay.galleryservice.controller;

import com.bijay.galleryservice.model.Gallery;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/")
public class GalleryController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @GetMapping("/home")
    public String home(){
        return "Hello from Gallery Service running at port :: " +
                env.getProperty("local.server.port");

    }

    @GetMapping("/{id}")
    public Gallery getGallery(@PathVariable final int id){
        Gallery gallery = new Gallery();
        gallery.setId(id);

        List<Object> images =
                restTemplate.getForObject(
                        "http://image-service/images",
                        List.class );
        gallery.setImages(images);

        return gallery;
    }

    @GetMapping("/admin")
    public String homeAdmin(){
        return "This is the admin area of Gallery service running at port :: "
                + env.getProperty("local.server.port");
    }
}
