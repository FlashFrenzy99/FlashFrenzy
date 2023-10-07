package com.example.flashfrenzy.sample;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @RequestMapping("/")
    public String sample() {
        return "배포 테스트";
    }
}
