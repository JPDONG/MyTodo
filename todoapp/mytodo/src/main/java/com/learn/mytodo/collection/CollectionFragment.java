package com.learn.mytodo.collection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.learn.mytodo.R;
import com.learn.mytodo.task.TaskListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class CollectionFragment extends Fragment {

    public static final String TAG = "CollectionFragment";

    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private List<CollectionItem> mItemList;
    private CollectionPresenter mPresenter;
    private CompositeDisposable mCompositeDisposable;
    private FloatingActionButton mFloatingActionButton;
    private View.OnClickListener mAddListener;

    public CollectionFragment() {
        mCompositeDisposable = new CompositeDisposable();
    }

    private ItemClickListener mItemClickListener = new ItemClickListener() {

        @Override
        public void onDelete(CollectionViewHolder viewHolder, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(String.format(Locale.CHINESE,"删除 %s（包含%d个条目）",mItemList.get(position).title,mItemList.get(position).nums));
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeItem(position);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewHolder.deleteButton.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

        @Override
        public void onClick(View v, String collectionId) {
            Log.d(TAG, "onClick: show list fragment");
            TaskListFragment fragment = new TaskListFragment();
            fragment.setCollectionId(collectionId);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.hide(CollectionFragment.this);
            transaction.replace(R.id.content_frame,fragment, "TaskListFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        mPresenter = new CollectionPresenter(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_collection);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new CollectionAdapter();
        mItemList = new ArrayList<>();
        /*for (int i = 0; i < 9; i++) {
            list.add(new CollectionItem("todo" + i,12 + i));
        }*/
        mAdapter.setCollectionItemList(mItemList);
        mAdapter.setOnClickListener(mItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        mAddListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColletcionDialog(inflater, container);
            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mFloatingActionButton.setOnClickListener(mAddListener);
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        mFloatingActionButton.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void loadData() {
        mCompositeDisposable.add(mPresenter.getCollections()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CollectionItem>>() {
                    @Override
                    public void accept(List<CollectionItem> collectionItems) throws Exception {
                        if (collectionItems == null) {
                            Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        } else {
                            mItemList = collectionItems;
                            mAdapter.setCollectionItemList(collectionItems);
                        }
                    }
                }));
    }

    private void showColletcionDialog(LayoutInflater inflater, @Nullable ViewGroup container) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_task_add, container, false);
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
        mCompositeDisposable.add(mPresenter.save(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            mItemList.add(item);
                            mAdapter.setCollectionItemList(mItemList);
                            if (mAdapter.getItemCount()-1 >= 0) {
                                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        } else {
                            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    private void removeItem(int position) {
        mCompositeDisposable.add(mPresenter.delete(mItemList.get(position))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) throws Exception {
                if (result) {
                    mAdapter.remove(position);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "on error:" + throwable.toString());
            }
        }));
    }

    static class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

        private List<CollectionItem> list = new ArrayList<>();
        private ItemClickListener clickListener;

        @Override
        public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_collection_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CollectionViewHolder holder, int position) {

            CollectionItem item = list.get(position);
            holder.mCollectionTitleTextView.setText(item.title);
            holder.mCollectionNumsTextView.setText(String.valueOf(item.nums));
            holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.deleteButton.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v, item.id);
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onDelete(holder,position);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            //return list.size() + 1;
            return list.size();
        }

        public void setCollectionItemList(List<CollectionItem> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void setOnClickListener(ItemClickListener listener) {
            this.clickListener = listener;
        }

        public void remove(int position) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {

        public TextView mCollectionTitleTextView;
        public TextView mCollectionNumsTextView;
        public ImageView deleteButton;
        public CardView mCardView;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            mCollectionNumsTextView = (TextView) itemView.findViewById(R.id.tv_collection_nums);
            mCollectionTitleTextView = (TextView) itemView.findViewById(R.id.tv_collection_title);
            deleteButton = (ImageView) itemView.findViewById(R.id.iv_collection_delete);
            mCardView = (CardView) itemView.findViewById(R.id.cv_collection);
        }
    }

    interface ItemClickListener {
        void onDelete(CollectionViewHolder viewHolder,int position);

        void onClick(View v,String collectionId);
    }
}
