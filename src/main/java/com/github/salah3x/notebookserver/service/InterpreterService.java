package com.github.salah3x.notebookserver.service;

import com.github.salah3x.notebookserver.exception.InterpreterException;

public interface InterpreterService {

    /**
     * Execute a piece of code in the requested programming
     * language and return the response.
     *
     * If an error occurred during code execution, throw a
     * {@link InterpreterException InterpreterException} exception.
     *
     * The service preserve the execution context based on the session id.
     * If the execution timed out, the context will be recreated and
     * any state preserved will be lost.
     *
     * See application.properties for execution-timeout config (default: 5 seconds).
     *
     * @param lang Execution language.
     * @param code Code to execute.
     * @param sessionId Id of the session to run in the same previous context.
     *
     * @throws InterpreterException The error message from the interpreter.
     *
     * @return The output of the code execution.
     */
    String execute(String lang, String code, String sessionId) throws InterpreterException;
}
