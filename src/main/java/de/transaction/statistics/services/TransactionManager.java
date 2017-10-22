package de.transaction.statistics.services;

import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;

public interface TransactionManager {
    void add(Transaction transaction);

    StatisticsSummary getStats();
}
