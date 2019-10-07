package com.github.salah3x.notebookserver.controller.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterpreterResponse {

    /**
     * A boolean value that indicate if the request was executed
     * successfully by the interpreter or not.
     */
    private boolean success;

    /**
     * The output of the code execution.
     */
    private String result;

    /**
     * The generated session id.
     * <p>
     * When sessionId is not specified in the request, the server
     * will create an new execution context identified by this ID.
     * <p>
     * Include this id in the request to execute code in the same
     * context and preserve the state of the interpreter.
     */
    private String sessionId;
}
