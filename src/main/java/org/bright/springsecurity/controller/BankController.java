package org.bright.springsecurity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    @PostMapping("/debit")
    public String debit(){
        return "money deducted";
    }
}
