package de.transaction.statistics.controllers;

import de.transaction.statistics.controllers.exceptions.OldTransactionException;
import de.transaction.statistics.models.Transaction;
import de.transaction.statistics.services.StatisticsService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService service;

    @Test
    public void shouldAcceptTransactions() throws Exception {
        JSONObject json = new JSONObject();
        json.put("amount", 10.0);
        json.put("timestamp", System.currentTimeMillis());

        mvc.perform(MockMvcRequestBuilders.post("/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json.toString()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldValidateOlderTransactions() throws Exception {
        JSONObject json = new JSONObject();
        json.put("amount", 10.0);
        json.put("timestamp", System.currentTimeMillis() - 60000);

        doThrow(OldTransactionException.class).when(service).addTransaction(any(Transaction.class));

        mvc.perform(MockMvcRequestBuilders.post("/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(json.toString()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void shouldValidateNullData() throws Exception {
        JSONObject json = new JSONObject();
        json.put("amount", 10.0);
        json.put("timestamp", null);

        mvc.perform(MockMvcRequestBuilders.post("/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(json.toString()))
            .andExpect(status().is4xxClientError());
    }

}