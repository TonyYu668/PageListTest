package com.tony.demo.pagelisttest;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;

/**
 * @author tony
 */
public class ObjectBoxHackDataSource<T> extends PositionalDataSource<T> {
    private final Query<T> query;
    private final DataObserver<List<T>> observer;

    public ObjectBoxHackDataSource(Query<T> query) {
        this.query = query;
        this.observer = new DataObserver<List<T>>() {
            public void onData(@NonNull List<T> data) {
                ObjectBoxHackDataSource.this.invalidate();
            }
        };
        query.subscribe().onlyChanges().weak().observer(this.observer);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<T> callback) {
        int totalCount = (int) this.query.count();
        if (totalCount == 0) {
            callback.onResult(Collections.<T>emptyList(), 0, 0);
        } else {
            int position = computeInitialLoadPosition(params, totalCount);
            int loadSize = computeInitialLoadSize(params, position, totalCount);
            List<T> list = this.loadRange(position, loadSize);
            if (list.size() == loadSize) {
                callback.onResult(list, position, totalCount);
            } else {
                this.invalidate();
            }

        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<T> callback) {
        callback.onResult(this.loadRange(params.startPosition, params.loadSize));
    }

    private List<T> loadRange(int startPosition, int loadCount) {
        return this.query.find((long) startPosition, (long) loadCount);
    }

    public static class Factory<Item> extends DataSource.Factory<Integer, Item> {
        private final Query<Item> query;

        public Factory(Query<Item> query) {
            this.query = query;
        }

        public DataSource<Integer, Item> create() {
            return new ObjectBoxHackDataSource(this.query);
        }
    }
}

