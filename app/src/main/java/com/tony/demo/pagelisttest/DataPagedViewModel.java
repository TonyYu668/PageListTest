package com.tony.demo.pagelisttest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * @author tony
 */
public class DataPagedViewModel extends ViewModel {

    private LiveData<PagedList<Data>> mDataLiveDataPaged;

    @SuppressWarnings("unchecked")
    public LiveData<PagedList<Data>> getNoteLiveDataPaged(Box<Data> notesBox) {
        if (mDataLiveDataPaged == null) {
            Query<Data> query = notesBox.query().order(Data_.id).build();
            PagedList.Config mPagedListConfig = new PagedList.Config.Builder()
                    .setPageSize(3) //分页数据的数量。
                    .setPrefetchDistance(5) //初始化时候，预取数据数量。
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(33)
                    .build();

            mDataLiveDataPaged = new LivePagedListBuilder(
                    new ObjectBoxHackDataSource.Factory<>(query),
                    mPagedListConfig
            ).build();

        }

        return mDataLiveDataPaged;
    }
}
