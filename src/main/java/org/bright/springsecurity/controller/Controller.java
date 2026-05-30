package org.bright.springsecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/hey")
    public String hey(){
        return "hey world";
    }

    @GetMapping("/hi")
    public String hi(){
        return "hi world";
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrf(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
