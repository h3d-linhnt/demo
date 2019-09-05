package com.example.demo.services;

import com.example.demo.models.Session;
import com.example.demo.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService{

    @Autowired
    private Jedis jedisClient;

    private final SessionRepository repository;

    public SessionServiceImpl(SessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Session save(Session session) {
        return repository.save(session);
    }

    @Override
    public List<Session> findAll() {
        List<Session> allSessions = new ArrayList<>();
        repository.findAll().forEach(allSessions::add);
        return allSessions;
    }

    @Override
    public Session findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public Session findByIdWithValidToken(String id) {
        if (repository.findById(id).isPresent()) {
            Session session = repository.findById(id).get();
            Set<String> sessionToken = session.getToken();
            Set<String> validTokens = jedisClient.zrangeByScore("token", (double) new Date().getTime(), (double) Long.MAX_VALUE);
            Set<String> validSessionTokens = sessionToken.stream().filter(validTokens::contains).collect(Collectors.toSet());
            session.setToken(validSessionTokens);
            return session;
        }
        return null;
    }

    @Override
    public void addToken(String token) {
        jedisClient.zadd("token", (double) new Date().getTime(), token);
    }

    @Override
    public void addToken(String token, long timestamp) {
        jedisClient.zadd("token", (double) timestamp, token);
    }
}
