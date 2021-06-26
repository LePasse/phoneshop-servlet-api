package com.es.phoneshop.model.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private final Map<String, FrequencyController> countMap = new ConcurrentHashMap<>();

    private static DefaultDosProtectionService instance;

    public static synchronized DefaultDosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        FrequencyController count = countMap.computeIfAbsent(ip, k -> new FrequencyController(THRESHOLD));
        count.addVisit();
        return count.isAllowed();
    }
}
