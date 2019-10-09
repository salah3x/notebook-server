package com.github.salah3x.notebookserver.service;

import lombok.Getter;
import lombok.Setter;
import org.graalvm.polyglot.Context;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

/**
 * This class represent an execution context associated with a session.
 * <p>
 * Instances from this class are kept in the {@link ExecutionContextStore ExecutionContextStore}.
 */
@Getter
@Setter
public class ExecutionContext {

    /**
     * Thr GraalVM execution context
     */
    private Context context;

    /**
     * The date and time when this context was last used.
     * <p>
     * It help to determine if the session expired qnd therefore
     * the execution context is to be removed from the store.
     */
    private LocalDateTime lastAccessed;

    /**
     * The output stream of the execution.
     * <p>
     * This where output of the interpreter is stored.
     * <p>
     * Both the standard output and the standard error of the
     * interpreter are channelled to this stream.
     */
    private ByteArrayOutputStream outputStream;

    public ExecutionContext() {
        this.lastAccessed = LocalDateTime.now();
        this.outputStream = new ByteArrayOutputStream();
        this.context = Context.newBuilder().out(this.outputStream).err(this.outputStream).build();
    }
}