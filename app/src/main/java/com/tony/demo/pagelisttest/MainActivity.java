package com.tony.demo.pagelisttest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * @author tony
 */
public class MainActivity extends AppCompatActivity {

    private BoxStore mBoxStore = PageListApplication.getInstance().getBoxStore();
    private DataPagedAdapter mDataPagedAdapter;
    private Box<Data> mDataBox;
    private RecyclerView mRecyclerView;
    private int mAddIndex = 1;

    //是否添加初始化数据
    private boolean bAddInitData = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBox = mBoxStore.boxFor(Data.class);

        mRecyclerView = findViewById(R.id.activity_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.activity_swipe_refresh);
        mSwipeRefreshLayout.setDistanceToTriggerSync(256);
        mDataPagedAdapter = new DataPagedAdapter(new DataPagedAdapter.DataClickListener() {
            @Override
            public void onDataClick(@Nullable Data data) {
                Toast.makeText(MainActivity.this, "click:" + data.content, Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50; i++) {
                    Data data = new Data();
                    data.content = "第" + i + "条数据";
                    if (!mBoxStore.isClosed()) {
                        mDataBox.put(data);
                    }
                }

            }
        }).start();

        mRecyclerView.setAdapter(mDataPagedAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                addDataFromRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!bAddInitData) {
            bAddInitData = true;
            DataPagedViewModel model = ViewModelProviders.of(this).get(DataPagedViewModel.class);
            model.getNoteLiveDataPaged(mDataBox).observe(this, new Observer<PagedList<Data>>() {
                @Override
                public void onChanged(@Nullable PagedList<Data> notes) {
                    mDataPagedAdapter.submitList(notes);
                }
            });
        }
    }

    public void addData(View view) {
        Data data = new Data();
        data.content = "新增 " + mAddIndex + " 条 数据";
        mAddIndex++;
        if (!mBoxStore.isClosed()) {
            mDataBox.put(data);
        }
    }

    public void addDataFromRefresh() {
        Data data = new Data();
        data.content = "刷新 新增 " + mAddIndex + " 条 数据";
        mAddIndex++;
        if (!mBoxStore.isClosed()) {
            mDataBox.put(data);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBox.removeAll();
        mBoxStore.close();
    }
}
