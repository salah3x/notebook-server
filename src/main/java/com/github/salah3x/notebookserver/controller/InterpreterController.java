package com.github.salah3x.notebookserver.controller;

import com.github.salah3x.notebookserver.controller.dtos.InterpreterRequest;
import com.github.salah3x.notebookserver.controller.dtos.InterpreterResponse;
import com.github.salah3x.notebookserver.exception.InterpreterException;
import com.github.salah3x.notebookserver.service.InterpreterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class InterpreterController {

    private final InterpreterService service;

    /**
     * Remote code execution endpoint.
     * <p>
     * It accept a block of code and execute it then forward the output
     * of the execution along with success status.
     * <p>
     * Subsequent calls can be run in the same execution context
     * if a valid session id is present in the request.
     * <p>
     * Programming languages support depends on the GraalVM installation.
     *
     * @param request The piece of code to execute along with the session id.
     * @return The output of the execution.
     */
    @PostMapping("/execute")
    public InterpreterResponse execute(@Valid @RequestBody InterpreterRequest request) {
        String code = request.getCode().substring(request.getCode().indexOf('\n') + 1);
        String lang = request.getCode().substring(1, request.getCode().indexOf('\n'));
        String sessionId = request.getSessionId();

        InterpreterResponse response = new InterpreterResponse();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            response.setSessionId(sessionId);
        }
        try {
            response.setResult(service.execute(lang, code, sessionId));
            response.setSuccess(true);
        } catch (InterpreterException e) {
            response.setResult(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }
}
