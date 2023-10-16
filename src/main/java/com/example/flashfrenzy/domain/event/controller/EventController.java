package com.example.flashfrenzy.domain.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    //이벤트 발생
    @PostMapping("/api/events")
    public void createEvent(@RequestBody EventCreateRequestDto request) {
        eventService.createEvent(request);
    }
}
