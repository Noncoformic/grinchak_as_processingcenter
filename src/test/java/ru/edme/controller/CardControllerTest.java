package ru.edme.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCards() throws Exception {
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCard() throws Exception {
        String json = """
                {
                  "cardNumber": "1234567890123456",
                  "expirationDate": "2026-12-31",
                  "holderName": "Иван Иванов",
                  "cardStatus": {
                    "id": 1
                  },
                  "paymentSystem": {
                    "id": 1
                  },
                  "account": {
                    "id": 1
                  },
                  "receivedFromIssuingBank": "2024-03-24T12:00:00",
                  "sentToIssuingBank": "2024-03-24T13:00:00"
                }
                """;

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
