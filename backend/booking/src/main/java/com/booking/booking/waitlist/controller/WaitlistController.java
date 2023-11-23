package com.booking.booking.waitlist.controller;

import com.booking.booking.waitlist.dto.response.WaitlistResponse;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/booking/waitlist")
@RestController
public class WaitlistController {
    private final WaitlistService waitlistService;

    @GetMapping("/{meetingId}")
    public ResponseEntity<Flux<WaitlistResponse>> findAllByMeetingMeetingId(@PathVariable("meetingId") Long meetingId) {
        Flux<WaitlistResponse> waitlistResponseFlux = waitlistService.findAllByMeetingId(meetingId)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(waitlistResponseFlux);
    }
}
