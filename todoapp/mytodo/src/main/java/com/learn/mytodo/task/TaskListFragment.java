package com.learn.mytodo.task;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.learn.mytodo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;
    private List<String> mTaskList;

    public TaskListFragment(){

    }

    public static TaskListFragment getInstance(){
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskList = new ArrayList(10);
        for (int i = 0;i < 10;i ++) {
            mTaskList.add("task " + i);
        }
        mTaskListAdapter = new TaskListAdapter(mTaskList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasklist_fragment,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
                loadTask();
                mRecyclerView.smoothScrollToPosition(mTaskListAdapter.getItemCount() - 1);
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTask();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void loadTask() {
        mTaskListAdapter.notifyDataSetChanged();
    }

    private void addTask() {
        mTaskList.add("new task");
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));
        mRecyclerView.setAdapter(mTaskListAdapter);
    }

    class TaskListAdapter extends RecyclerView.Adapter<ListItemViewHolder>{
        private List<String> mList;

        public TaskListAdapter(){

        }
        public TaskListAdapter(List list) {
            mList = list;
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder listItemViewHolder = new ListItemViewHolder(getActivity().getLayoutInflater().inflate(R.layout.tasklist_item,parent,false));
            return listItemViewHolder;
        }

        @Override
        public void onBindViewHolder(ListItemViewHolder holder, int position) {
            holder.mTextView.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTextView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
