package com.learn.mytodo.task;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    public static final String TAG = "CollectionFragment";

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
        mAdapter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showColletcionDialog(inflater, container);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void showColletcionDialog(LayoutInflater inflater, @Nullable ViewGroup container) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = inflater.inflate(R.layout.addtask_dialog, container, false);
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editTitle = (EditText) dialogView.findViewById(R.id.edit_title);
                String title = editTitle.getText().toString().trim();
                if ("".equals(title.trim())) {
                    Log.d(TAG, "nothing to add");
                    return;
                }
                CollectionItem item = new CollectionItem(title);
                saveCollection(item);
                if (mAdapter.getItemCount() - 1 >= 0) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                }

            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveCollection(CollectionItem item) {

    }

    static class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder>{

        private List<CollectionItem> mCollectionItemList = new ArrayList<>();
        private View.OnClickListener mClickListener;
        private View.OnLongClickListener mLongClickListener;

        @Override
        public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_collection_item,parent,false));
        }

        @Override
        public void onBindViewHolder(CollectionViewHolder holder, int position) {
            if (position < mCollectionItemList.size()) {
                CollectionItem item = mCollectionItemList.get(position);
                holder.mCollectionTitleTextView.setText(item.title);
                holder.mCollectionNumsTextView.setText(String.valueOf(item.nums));
            } else {
                holder.mCollectionNumsTextView.setVisibility(View.GONE);
                holder.mCollectionTitleTextView.setVisibility(View.GONE);
                holder.mAddImageView.setVisibility(View.VISIBLE);
                holder.mAddImageView.setOnClickListener(mClickListener);
            }
            holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCollectionItemList.size() + 1;
        }

        public void setCollectionItemList(List<CollectionItem> list) {
            mCollectionItemList = list;
            notifyDataSetChanged();
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.mClickListener = listener;
        }
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder{

        public TextView mCollectionTitleTextView;
        public TextView mCollectionNumsTextView;
        public ImageView mAddImageView;
        public CardView mCardView;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            mCollectionNumsTextView = (TextView) itemView.findViewById(R.id.tv_collection_nums);
            mCollectionTitleTextView = (TextView) itemView.findViewById(R.id.tv_collection_title);
            mAddImageView = (ImageView) itemView.findViewById(R.id.iv_collection_add);
            mCardView = (CardView) itemView.findViewById(R.id.cv_collection);
        }
    }
}
