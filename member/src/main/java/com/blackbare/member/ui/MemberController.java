package com.blackbare.member.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/member/ping")
    public void pong() {
        System.out.println("Member Application pong");
    }
}
