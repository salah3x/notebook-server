package com.github.salah3x.notebookserver.service;

import org.graalvm.polyglot.Context;

/**
 * This class represent a store for execution contexts for the interpreter.
 * This is where contexts are stored, recreated and ultimately removed.
 *
 * Context are kept in a store (ex: in-memory) and removed after
 * being inactive for period time.
 */
public interface ExecutionContextStore {

    /**
     * Get the context from the store.
     *
     * If there is no context for the specified session id,
     * a new one will be created.
     *
     * @param sessionId Id of the session
     * @return The execution context for this session
     */
    Context getContext(String sessionId);

    /**
     * Reset the context of this session.
     *
     * All data (state) preserved by this context will be lost.
     * Useful when the interpreted stopped responding.
     *
     * @param sessionId Id of the session
     */
    void resetContext(String sessionId);

    /**
     * Schedule a periodic cleanup.
     * Expired sessions will be removed from the store.
     *
     * See application.properties for session-expires-in config (default: 1 hour).
     */
    void cleanup();
}
