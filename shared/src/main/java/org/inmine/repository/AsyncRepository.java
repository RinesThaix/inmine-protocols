package org.inmine.repository;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by RINES on 19.11.17.
 */
public abstract class AsyncRepository<K, V> {

    private final AsyncLoadingCache<K, V> cache;

    public AsyncRepository() {
        this(Caffeine.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES));
    }

    @SuppressWarnings("unchecked")
    public AsyncRepository(Caffeine caffeine) {
        this.cache = caffeine.buildAsync(new AsyncCacheLoader<K, V>() {
            @Override
            public CompletableFuture<V> asyncLoad(K key, Executor executor) {
                CompletableFuture<V> future = new CompletableFuture<>();
                preload(key, future::complete);
                return future;
            }
        });
    }

    public void get(K key, Consumer<V> callback) {
        this.cache.get(key).whenComplete((value, ex) -> callback.accept(value));
    }

    public void getIfPresent(K key, Consumer<V> callback) {
        CompletableFuture<V> future = this.cache.getIfPresent(key);
        if(future != null)
            future.whenComplete((value, ex) -> callback.accept(value));
    }

    protected abstract void preload(K key, Consumer<V> callback);

}
