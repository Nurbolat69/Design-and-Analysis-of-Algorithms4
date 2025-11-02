package graph.metrics;

public interface Metrics {
    void start();
    void stop();
    void inc(String counter);
    long getCounter(String counter);
    long elapsedNanos();
}
