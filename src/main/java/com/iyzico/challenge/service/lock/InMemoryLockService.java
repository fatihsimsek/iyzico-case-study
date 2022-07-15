package com.iyzico.challenge.service.lock;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class InMemoryLockService implements LockService {
    private static Map<Long, ReentrantLock> lockMap = new ConcurrentHashMap<Long, ReentrantLock>();

    @Override
    public void lock(Long id) {
        lockMap.computeIfAbsent(id, l -> new ReentrantLock());
        lockMap.get(id).lock();
    }

    @Override
    public void release(Long id) {
        lockMap.get(id).unlock();
    }
}
