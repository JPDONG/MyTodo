package com.learn.mytodo.task;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;
    private List<Task> mTaskList;
    private TasksLocalDataSource mTasksLocalDataSource;
    private View mDialogView;
    private String TAG = "TaskListFragment";


    public TaskListFragment(){

    }

    public static TaskListFragment getInstance(){
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskList = new ArrayList(0);
        mTaskListAdapter = new TaskListAdapter(mTaskList);
        mTasksLocalDataSource = new TasksLocalDataSource(getContext());
        loadTask();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasklist_fragment,container,false);
        mDialogView = inflater.inflate(R.layout.addtask_dialog, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(mDialogView);
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTitle = (EditText) mDialogView.findViewById(R.id.edit_title);
                        EditText editDescription = (EditText) mDialogView.findViewById(R.id.edit_description);
                        String title = editTitle.getText().toString();
                        String description = editDescription.getText().toString();
                        Log.d(TAG, "onClick: title = " + title + ", description = " + description);
                        Task task = new Task(title, description);
                        addTask(task);
                        loadTask();
                        if (mTaskListAdapter.getItemCount() - 1 >= 0) {
                            mRecyclerView.smoothScrollToPosition(mTaskListAdapter.getItemCount() - 1);
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
        mTasksLocalDataSource.getTask(new TasksLocalDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> task) {
                for (Task t : task) {
                    mTaskList.add(t);
                }
            }

            @Override
            public void onDataNotAvailabel() {

            }
        });
        mTaskListAdapter.notifyDataSetChanged();
    }

    private void addTask(Task task) {
        //mTaskList.add("new task");
        mTasksLocalDataSource.saveTask(task);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));
        mRecyclerView.setAdapter(mTaskListAdapter);
    }

    class TaskListAdapter extends RecyclerView.Adapter<ListItemViewHolder>{
        private List<Task> mList;

        public TaskListAdapter(){

        }
        public TaskListAdapter(List<Task> list) {
            mList = list;
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder listItemViewHolder = new ListItemViewHolder(getActivity().getLayoutInflater().inflate(R.layout.tasklist_item,parent,false));
            return listItemViewHolder;
        }

        @Override
        public void onBindViewHolder(ListItemViewHolder holder, int position) {
            holder.mTitle.setText(mList.get(position).getmTitle());
            holder.mDescription.setText(mList.get(position).getmDescription());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTitle;
        private TextView mDescription;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.task_title);
            mDescription = (TextView) itemView.findViewById(R.id.task_description);
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
