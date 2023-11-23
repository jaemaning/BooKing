package com.booking.member.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {
    // error path를 꼭 "/error" 로 하자!
    private final String ERROR_PATH = "/error";

    @GetMapping(ERROR_PATH)
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.info("error status: {}",status.toString());
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // HTTP 상태 코드에 따른 로직 구현
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.notFound().build();
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return ResponseEntity.internalServerError().build();
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied!");
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return ResponseEntity.badRequest().body("Bad Request!");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()){
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
    public String getErrorPath(){
        return ERROR_PATH;
    }
}
