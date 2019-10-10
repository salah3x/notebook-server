package com.github.salah3x.notebookserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.salah3x.notebookserver.controller.dtos.InterpreterRequest;
import com.github.salah3x.notebookserver.exception.LanguageNotSupportedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotebookServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void emptyBody_ShouldFailWith400() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/execute"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(HttpMessageNotReadableException.class)));
        assertThat(result.getResolvedException().getMessage(), startsWith("Required request body is missing"));
    }

    @Test
    public void wrongCodeFormat_ShouldFailWith400() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setCode("console.log(1 + 1);");
        MvcResult result = this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
        assertThat(result.getResolvedException().getMessage(), startsWith("Validation failed"));
    }

    @Test
    public void unsupportedLanguage_ShouldFailWith400() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setCode("%unknown\nprint('Hello')");
        MvcResult result = this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(LanguageNotSupportedException.class)));
        assertThat(result.getResolvedException().getMessage(), startsWith("A language with id 'unknown' is not installed."));
    }

    @Test
    public void shouldPrintHelloWorld() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setCode("%js\nconsole.log('Hello world');");
        this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.result", is("Hello world\n")));
    }

    @Test
    public void shouldPrintErrorMessage() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setCode("%js\nconsole.log(a);");
        this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.result", is("ReferenceError: a is not defined")));
    }

    @Test
    public void shouldPreserveContext() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setSessionId(UUID.randomUUID().toString());
        body.setCode("%js\nvar a = 10;");
        this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        body.setCode("%js\nconsole.log(a);");
        this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.result", is("10\n")));

    }

    @Test
    public void shouldTimeout() throws Exception {
        InterpreterRequest body = new InterpreterRequest();
        body.setCode("%js\nwhile(true);");
        this.mockMvc.perform(
                post("/execute")
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.result", is("Execution got cancelled.")));
    }
}
