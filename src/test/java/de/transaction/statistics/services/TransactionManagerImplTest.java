package de.transaction.statistics.services;

import de.transaction.statistics.controllers.exceptions.OldTransactionException;
import de.transaction.statistics.models.StatisticsSummary;
import de.transaction.statistics.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionManagerImplTest {

    @Autowired
    private TransactionManager tested;

    @Test
    public void shouldInsertTransactionsAndReadCorrectSummary() throws InterruptedException {
        tested.add(new Transaction(1.0, currentTimeMillis() - 1000));
        tested.add(new Transaction(2.0, currentTimeMillis() - 2000));
        tested.add(new Transaction(3.0, currentTimeMillis() - 3000));
        tested.add(new Transaction(4.0, currentTimeMillis() - 4000));
        tested.add(new Transaction(5.0, currentTimeMillis() - 5000));
        tested.add(new Transaction(6.0, currentTimeMillis() - 6000));
        tested.add(new Transaction(7.0, currentTimeMillis() - 7000));
        tested.add(new Transaction(8.0, currentTimeMillis() - 8000));
        tested.add(new Transaction(9.0, currentTimeMillis() - 9000));
        tested.add(new Transaction(10.0, currentTimeMillis() - 10000));
        tested.add(new Transaction(11.0, currentTimeMillis() - 59990)); // will expire

        Thread.sleep(11L);

        StatisticsSummary summary = tested.getStats();
        assertThat(summary.getCount(), is(10L));
        assertThat(summary.getSum(), is(55.0));
        assertThat(summary.getMax(), is(10.0));
        assertThat(summary.getMin(), is(1.0));
        assertThat(summary.getAvg(), is(55.0 / 10));

        StatisticsSummary updatedSummary = tested.getStats();
        assertEquals(summary, updatedSummary);
    }

    @Test(expected = OldTransactionException.class)
    public void shouldThrowExceptionForAddOldTransaction() {
        tested.add(new Transaction(1.0, currentTimeMillis() - 60000));
    }

}