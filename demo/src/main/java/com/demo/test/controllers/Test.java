package com.demo.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping("/")
    public String protected1() {
        return "Proctected 1";
    }

    @GetMapping("/protected2")
    public String protected2() {
        return "Proctected 2";
    }

    @GetMapping("/protected3")
    public String protected3() {
        return "Proctected 3";
    }

}
