package com.iyzico.challenge.service.lock;

public interface LockService {
    void lock(Long id);
    void release(Long id);
}
