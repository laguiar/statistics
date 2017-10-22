package de.transaction.statistics.services;

import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;

public interface StatisticsService {

    void addTransaction(Transaction dto);

    StatisticsSummary getStatistics();
}
