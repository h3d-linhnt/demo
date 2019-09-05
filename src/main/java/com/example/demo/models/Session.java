package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Set;

@RedisHash("Session")
@Getter
@Setter
@NoArgsConstructor
public class Session implements Serializable {
    @Id
    private String sessionId;

    private String userAgent;

    private String dateTime;

    private String ipAddress;

    private Set<String> token;
}
