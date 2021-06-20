package com.es.phoneshop.model.security;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class FrequencyController {
    private final Deque<Long> requestTimeStamps;
    private final long threshold;

    public FrequencyController(long threshold) {
        this.requestTimeStamps = new ConcurrentLinkedDeque<>();
        this.threshold = threshold;
    }

    public void addVisit() {
        requestTimeStamps.addLast(System.currentTimeMillis());
        if (requestTimeStamps.size() > threshold) {
            requestTimeStamps.removeFirst();
        }
    }

    public boolean isAllowed() {
        if (requestTimeStamps.size() < threshold) {
            return true;
        }
        return requestTimeStamps.peekFirst() <= requestTimeStamps.peekLast() - TimeUnit.MINUTES.toMillis(1L);
    }

}