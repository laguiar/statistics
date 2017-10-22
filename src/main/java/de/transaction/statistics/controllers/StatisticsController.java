package de.transaction.statistics.controllers;

import de.transaction.statistics.controllers.exceptions.OldTransactionException;
import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;
import de.transaction.statistics.services.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StatisticsController {

    private StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransaction(@Valid @RequestBody Transaction transaction) {
        service.addTransaction(transaction);
    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatisticsSummary getStatistics() {
        return service.getStatistics();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(OldTransactionException.class)
    public void handleOldTransctions() {
        // log something here
    }

}
