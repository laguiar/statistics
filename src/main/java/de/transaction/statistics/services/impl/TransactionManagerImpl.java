package de.transaction.statistics.services.impl;

import de.transaction.statistics.controllers.exceptions.OldTransactionException;
import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;
import de.transaction.statistics.services.TransactionManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionManagerImpl implements TransactionManager {

    private static final long THRESHOLD = TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS);

    private ConcurrentNavigableMap<Long, StatisticsSummary> summaryStorage = new ConcurrentSkipListMap<>();

    @Override
    public void add(Transaction transaction) {
        if (isNotExpired(transaction.getTimestamp())) {
            summaryStorage.compute(transaction.getTimestamp(), (time, summary) -> {
                if (null == summary) {
                    summary = new StatisticsSummary();
                    summary.setCount(1L);
                    summary.setMin(transaction.getAmount());
                    summary.setMax(transaction.getAmount());
                    summary.setAvg(transaction.getAmount());
                    summary.setSum(transaction.getAmount());
                    return summary;
                }

                summary.setCount(summary.getCount() + 1);
                summary.setMin(Double.min(summary.getMin(), transaction.getAmount()));
                summary.setMax(Double.max(summary.getMax(), transaction.getAmount()));
                summary.setSum(Double.sum(summary.getSum(), transaction.getAmount()));
                summary.setAvg(summary.getSum() / summary.getCount());
                return summary;
            });

        } else {
            throw new OldTransactionException();
        }
    }

    @Override
    public synchronized StatisticsSummary getStats() {
        final StatisticsSummary summary = new StatisticsSummary();
        summary.setMin(Integer.MAX_VALUE);

        summaryStorage.forEach((timestamp, stats) -> {
            if (isNotExpired(timestamp)) {
                summary.setCount(summary.getCount() + 1);
                summary.setMin(Double.min(summary.getMin(), stats.getMin()));
                summary.setMax(Double.max(summary.getMax(), stats.getMax()));
                summary.setSum(Double.sum(summary.getSum(), stats.getSum()));
                summary.setAvg(BigDecimal.valueOf(summary.getSum())
                        .divide(BigDecimal.valueOf(summary.getCount()), 2, RoundingMode.HALF_UP)
                        .doubleValue());

            } else {
                // for the test proposal, will remove the expired transactions here
                // for a real world app, probably we need a more sophisticated way to clean up the items
                summaryStorage.remove(timestamp);
            }
        });

        // if all items are expired
        if (summaryStorage.isEmpty()) {
            summary.setMin(0);
        }

        return summary;
    }

    private boolean isNotExpired(long timestamp) {
        return System.currentTimeMillis() - THRESHOLD < timestamp;
    }

}
