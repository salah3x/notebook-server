package com.github.salah3x.notebookserver.service.impl;

import com.github.salah3x.notebookserver.config.AppProperties;
import com.github.salah3x.notebookserver.service.ExecutionContext;
import com.github.salah3x.notebookserver.service.ExecutionContextStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class InMemoryExecutionContextStore implements ExecutionContextStore {

    private final AppProperties properties;
    private final Map<String, ExecutionContext> store = new HashMap<>();

    @Override
    public ExecutionContext getContext(String sessionId) {
        ExecutionContext context = this.store.get(sessionId);
        if (context == null) {
            context = new ExecutionContext();
            this.store.put(sessionId, context);
        } else {
            context.setLastAccessed(LocalDateTime.now());
        }
        return context;
    }

    @Override
    public void resetContext(String sessionId) {
        this.store.put(sessionId, new ExecutionContext());
    }

    @Scheduled(fixedDelay = 60 * 1000)
    @Async
    @Override
    public void cleanup() {
        int count = 0;
        for (String sessionId : this.store.keySet()) {
            long inactive = ChronoUnit.SECONDS.between(this.store.get(sessionId).getLastAccessed(), LocalDateTime.now());
            if (inactive > properties.getSessionExpiresIn()) {
                this.store.remove(sessionId);
                count++;
            }
        }
        log.info("Sessions cleanup finished, {} sessions deleted.", count);
    }
}
