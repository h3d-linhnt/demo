package com.example.demo.controllers;

import com.example.demo.models.Session;
import com.example.demo.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class TokenController {

    private final SessionService sessionService;

    public TokenController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/")
    public ResponseEntity home() {
        Map<String, String> returnObject = new HashMap<>();
        returnObject.put("message","Hello, World!");
        return ResponseEntity.ok(returnObject);
    }

    @GetMapping("/session/create")
    public ResponseEntity createSession(HttpServletRequest request, @RequestHeader("User-Agent") String userAgent) {
        Session newSession = new Session();
        newSession.setUserAgent(userAgent);
        newSession.setDateTime(new Date().toString());
        newSession.setIpAddress(request.getRemoteAddr());
        String token = UUID.randomUUID().toString();
        Set<String> listToken = new HashSet<>();
        listToken.add(token);
        newSession.setToken(listToken);
        sessionService.addToken(token);
        Session savedSession = sessionService.save(newSession);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/session/create/{timestamp}")
    public ResponseEntity createSession(HttpServletRequest request, @RequestHeader("User-Agent") String userAgent, @PathVariable("timestamp") long timestamp) {
        Session newSession = new Session();
        newSession.setUserAgent(userAgent);
        newSession.setDateTime(new Date().toString());
        newSession.setIpAddress(request.getRemoteAddr());
        String token = UUID.randomUUID().toString();
        Set<String> listToken = new HashSet<>();
        listToken.add(token);
        newSession.setToken(listToken);
        sessionService.addToken(token, timestamp);
        Session savedSession = sessionService.save(newSession);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/session/get")
    public ResponseEntity getSessionInfo(HttpServletRequest request, @RequestHeader("User-Agent") String userAgent) {
        List<Session> allSessions = sessionService.findAll();
        return ResponseEntity.ok(allSessions);
    }

    @GetMapping("/session/put/{ssid}")
    public ResponseEntity addTokenToSession(HttpServletRequest request, @PathVariable("ssid") String ssid) {
        Session session = sessionService.findById(ssid);
        String token = UUID.randomUUID().toString();
        Set<String> sessionTokens = session.getToken();
        sessionTokens.add(token);
        session.setToken(sessionTokens);
        sessionService.addToken(UUID.randomUUID().toString());
        Session savedSession = sessionService.save(session);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/session/get/{ssid}/from-now")
    public ResponseEntity getSessionTokenFromNowBySessionId(HttpServletRequest request, @PathVariable("ssid") String ssid) {
        Session session = sessionService.findByIdWithValidToken(ssid);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/session/get/{ssid}")
    public ResponseEntity getSessionTokenBySessionId(HttpServletRequest request, @PathVariable("ssid") String ssid) {
        Session session = sessionService.findById(ssid);
        return ResponseEntity.ok(session);
    }
}
