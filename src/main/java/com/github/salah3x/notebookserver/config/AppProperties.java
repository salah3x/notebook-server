package com.github.salah3x.notebookserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("interpreter")
public class AppProperties {

    /**
     * The amount of seconds before removing execution contexts
     * from the store of inactive sessions
     */
    private int sessionExpiresIn = 3600;

    /**
     * The amount of second before cancelling execution
     */
    private int executionTimeout = 5;
}
