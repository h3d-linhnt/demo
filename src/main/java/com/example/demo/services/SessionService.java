package com.example.demo.services;

import com.example.demo.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

public interface SessionService {

    Session save(Session session);

    List<Session> findAll();

    Session findById(String id);

    Session findByIdWithValidToken(String id);

    void addToken(String token);

    void addToken(String token, long timestamp);
}
