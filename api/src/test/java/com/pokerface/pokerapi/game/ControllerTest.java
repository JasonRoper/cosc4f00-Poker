package com.pokerface.pokerapi.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ControllerTest {

    GameController controller;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testMatchmaking() throws Exception {
        mvc.perform(post("/api/v1/matchmaking/basicGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("/gameId").isNumber());
    }
}
