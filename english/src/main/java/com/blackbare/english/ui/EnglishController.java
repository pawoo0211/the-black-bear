package com.blackbare.english.ui;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnglishController {
    public void englishApplicationPing() {
        System.out.println("English Application pong");
    }
}
