package com.hackathon.Lops.services;

import java.util.concurrent.TimeUnit;

public interface ILockingService {
        public Boolean acquireLock(String lockId, Long lockUntil, TimeUnit timeUnit);
        public Boolean releaseLock(String lockId);
}

