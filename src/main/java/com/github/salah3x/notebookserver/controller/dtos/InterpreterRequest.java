package com.github.salah3x.notebookserver.controller.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InterpreterRequest {

    /**
     * The piece of code to execute.
     * The first line must contain '%' plus the desired interpreter language.
     * <p>
     * ex:
     * %js
     * console.log(1 + 1);
     * console.log("Hello world!");
     */
    @NotEmpty
    @Pattern(regexp = "%[a-zA-Z]+\\n[\\W\\w]+")
    private String code;

    /**
     * The session id generated by the server on the first call.
     * It will be used to preserve the state of the interpreter.
     */
    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
    private String sessionId;
}
