package de.transaction.statistics.services.impl;

import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;
import de.transaction.statistics.services.StatisticsService;
import de.transaction.statistics.services.TransactionManager;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private TransactionManager tsManager;

    public StatisticsServiceImpl(TransactionManager tsManager) {
        this.tsManager = tsManager;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        tsManager.add(transaction);
    }

    @Override
    public StatisticsSummary getStatistics() {
        return tsManager.getStats();
    }

}
