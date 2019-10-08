package com.github.salah3x.notebookserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("interpreter")
public class AppProperties {

    /**
     * The amount of seconds before removing execution contexts
     * from the store of inactive sessions
     */
    public int sessionExpiresIn = 3600;

    /**
     * The amount of second before cancelling execution
     */
    public int executionTimeout = 5;
}
