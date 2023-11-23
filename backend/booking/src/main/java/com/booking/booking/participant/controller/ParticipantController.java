package com.booking.booking.participant.controller;

import com.booking.booking.participant.dto.response.ParticipantResponse;
import com.booking.booking.participant.service.ParticipantService;
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
@RequestMapping("/api/booking/participant")
@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/{meetingId}")
    public ResponseEntity<Flux<ParticipantResponse>> findAllByMeetingId(@PathVariable("meetingId") Long meetingId) {
        Flux<ParticipantResponse> participantResponseFlux = participantService.findAllResponseByMeetingId(meetingId)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(participantResponseFlux);
    }
}
