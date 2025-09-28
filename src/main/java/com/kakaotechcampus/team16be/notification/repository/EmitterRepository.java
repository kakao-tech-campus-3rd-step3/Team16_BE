package com.kakaotechcampus.team16be.notification.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitterMap.put(getKey(userId), sseEmitter);
        log.info("Saved SseEmitter for {}", userId);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long userId) {
        log.info("Got SseEmitter for {}", userId);
        return Optional.ofNullable(emitterMap.get(getKey(userId)));
    }

    public void delete(Long userId) {
        emitterMap.remove(getKey(userId));
        log.info("Deleted SseEmitter for {}", userId);
    }

    private String getKey(Long userId) {
        return "Emitter:UID:" + userId;
    }
}
