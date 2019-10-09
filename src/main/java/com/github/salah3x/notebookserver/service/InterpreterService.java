package com.github.salah3x.notebookserver.service;

import com.github.salah3x.notebookserver.exception.InterpreterException;
import com.github.salah3x.notebookserver.exception.LanguageNotSupportedException;

public interface InterpreterService {

    /**
     * Execute a piece of code in the requested programming
     * language and return the response.
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
     * @throws InterpreterException if an error occurred during code execution.
     * @throws LanguageNotSupportedException if the interpreter doesn't support the language.
     *
     * @return The output of the code execution.
     */
    String execute(String lang, String code, String sessionId) throws InterpreterException;
}
