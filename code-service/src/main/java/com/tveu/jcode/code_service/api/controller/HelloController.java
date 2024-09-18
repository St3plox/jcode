package com.tveu.jcode.code_service.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/code/hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
