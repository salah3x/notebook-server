package com.github.salah3x.notebookserver.service.impl;

import com.github.salah3x.notebookserver.config.AppProperties;
import com.github.salah3x.notebookserver.exception.InterpreterException;
import com.github.salah3x.notebookserver.service.ExecutionContextStore;
import com.github.salah3x.notebookserver.service.InterpreterService;
import lombok.RequiredArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class GraalVMInterpreterService implements InterpreterService {

    private final AppProperties properties;
    private final ExecutionContextStore store;

    @Override
    public String execute(String lang, String code, String sessionId) {
        Context context = this.store.getContext(sessionId);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.close(true);
                store.resetContext(sessionId);
            }
        }, properties.executionTimeout * 1000);
        String output;
        try {
            output = context.eval(lang, code).toString();
        } catch (PolyglotException e) {
            throw new InterpreterException(e.getMessage());
        }
        timer.cancel();
        return output;
    }
}
