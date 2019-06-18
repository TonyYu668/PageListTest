package com.tony.demo.pagelisttest;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author tony
 */
public class DataPagedAdapter extends PagedListAdapter<Data, DataPagedAdapter.DataViewHolder> {

    public interface DataClickListener {
        void onDataClick(@Nullable Data data);
    }

    @NonNull
    private final DataClickListener dataClickListener;

    public DataPagedAdapter(@NonNull DataClickListener dataClickListener) {
        super(DIFF_CALLBACK);
        this.dataClickListener = dataClickListener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_item, parent, false);
        return new DataViewHolder(view, dataClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data data = getItem(position);
        if (data != null) {
            holder.bindTo(data);
        } else {
            holder.clear();
        }
    }

    @Nullable
    @Override
    protected Data getItem(int position) {
        return super.getItem(position);
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public Data mData;

        public DataViewHolder(View itemView, final DataClickListener dataClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataClickListener.onDataClick(mData);
                }
            });
            text = itemView.findViewById(R.id.data_item_content);
        }

        public void bindTo(Data data) {
            this.mData = data;
            text.setText(data.content);
        }

        public void clear() {
            text.setText("");
        }
    }

    private static final DiffUtil.ItemCallback<Data> DIFF_CALLBACK = new DiffUtil.ItemCallback<Data>() {
        @Override
        public boolean areItemsTheSame(Data oldItem, Data newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(Data oldItem, Data newItem) {
            return oldItem.equals(newItem);
        }
    };

}
