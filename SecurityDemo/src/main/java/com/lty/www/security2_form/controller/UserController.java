package com.lty.www.security2_form.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")
public class UserController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, user";
    }
}
