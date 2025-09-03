package org.kir.configuration.cache;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

    @Component
    public class CacheMetrics {
        private final MeterRegistry registry;

        public CacheMetrics(MeterRegistry meterRegistry) {
            this.registry = meterRegistry;
        }

        public void recordCacheHit(String cacheName) {
            Counter.builder("cache.hits")
                    .tag("cache", cacheName)
                    .register(registry)
                    .increment();
        }

        public void recordCacheMiss(String cacheName) {
            Counter.builder("cache.misses")
                    .tag("cache", cacheName)
                    .register(registry)
                    .increment();
        }
    }
