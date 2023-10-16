package com.example.flashfrenzy.domain.event.controller;

import com.example.flashfrenzy.domain.event.dto.EventCreateRequestDto;
import com.example.flashfrenzy.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    //이벤트 발생
    @PostMapping("/api/events")
    public String  createEvent(@RequestBody EventCreateRequestDto request) {
        eventService.createEvent(request);

        return "이벤트 생성 성공.";
    }
}
