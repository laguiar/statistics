package de.transaction.statistics.models;

import java.util.Objects;

public class StatisticsSummary {

    private long count = 0;
    private double sum = 0;
    private double avg = 0;
    private double max = 0;
    private double min;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticsSummary)) return false;
        StatisticsSummary summary = (StatisticsSummary) o;
        return count == summary.count &&
                Double.compare(summary.sum, sum) == 0 &&
                Double.compare(summary.avg, avg) == 0 &&
                Double.compare(summary.max, max) == 0 &&
                Double.compare(summary.min, min) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, sum, avg, max, min);
    }
}
