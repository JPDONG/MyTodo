package com.learn.mytodo.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.mytodo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_collection);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new CollectionAdapter();
        List<CollectionItem> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add(new CollectionItem("todo" + i,12 + i));
        }
        mAdapter.setCollectionItemList(list);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    static class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder>{

        private List<CollectionItem> mCollectionItemList = new ArrayList<>();

        @Override
        public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_collection_item,parent,false));
        }

        @Override
        public void onBindViewHolder(CollectionViewHolder holder, int position) {
            CollectionItem item = mCollectionItemList.get(position);
            holder.mCollectionTitleTextView.setText(item.title);
            holder.mCollectionNumsTextView.setText(String.valueOf(item.nums));
        }

        @Override
        public int getItemCount() {
            return mCollectionItemList.size();
        }

        public void setCollectionItemList(List<CollectionItem> list) {
            mCollectionItemList = list;
            notifyDataSetChanged();
        }
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder{

        public TextView mCollectionTitleTextView;
        public TextView mCollectionNumsTextView;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            mCollectionNumsTextView = (TextView) itemView.findViewById(R.id.tv_collection_nums);
            mCollectionTitleTextView = (TextView) itemView.findViewById(R.id.tv_collection_title);
        }
    }
}
