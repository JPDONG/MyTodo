package com.learn.mytodo.task;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.learn.mytodo.data.source.TasksDataSource;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

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
    private TasksRemoteDataSource mTasksRemoteDataSource;
    private TasksRepository mTasksRepository;
    private View mDialogView;
    private CoordinatorLayout mCoordinatorLayout;
    private String TAG = "TaskListFragment";
    private boolean mTestRemoteData = true;


    public TaskListFragment() {

    }

    public static TaskListFragment getInstance() {
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskList = new ArrayList(0);
        mTaskListAdapter = new TaskListAdapter(mTaskList);
        mTasksLocalDataSource = new TasksLocalDataSource(getContext());
        mTasksRemoteDataSource = new TasksRemoteDataSource(getContext());
        mTasksRepository = TasksRepository.getInstance(mTasksLocalDataSource, mTasksRemoteDataSource);
        if (mTestRemoteData) {
            mTasksRepository.refreshTasks();
            mTestRemoteData = false;
        }
        loadTask();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasklist_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        setupRecyclerView();
        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                mDialogView = inflater.inflate(R.layout.addtask_dialog, container, false);
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
                        showSnackerBar("add sucess");
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

    private void showSnackerBar(String s) {
        Snackbar.make(getView(),s,Snackbar.LENGTH_LONG).show();
    }

    private void loadTask() {
        final List<Task> taskList = new ArrayList<Task>();
        mTasksRepository.getTask(new TasksRepository.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> task) {
                for (Task t : task) {
                    taskList.add(t);
                    /*if (!mTaskList.contains(t)) {
                        mTaskList.add(t);
                        Log.d(TAG, "onTasksLoaded: t.getmId() = " + t.getmId() + ",mTaskList.get(0).getmId() = " + mTaskList.get(0).getmId());
                    }*/
                }
            }

            @Override
            public void onDataNotAvailabel() {
            }
        });
        //mTaskListAdapter.notifyDataSetChanged();
        mTaskListAdapter.replaceData(taskList);
    }

    private void addTask(Task task) {
        //mTaskList.add("new task");
        mTasksRepository.saveTask(task);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mTaskListAdapter);
    }

    class TaskListAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

        private List<Task> mList;

        public TaskListAdapter() {

        }

        public TaskListAdapter(List<Task> list) {
            mList = list;
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder listItemViewHolder = new ListItemViewHolder(getActivity().getLayoutInflater().inflate(R.layout.tasklist_item, parent, false));

           /*if (task.ismCompleted()) {
                holder.mTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }*/

           /* if ("".equals(task.getmDescription())) {
                holder.mDescription.setVisibility(View.GONE);
            } else {
                holder.mDescription.setText(task.getmDescription());
            }*/

            return listItemViewHolder;
        }

        @Override
        public void onBindViewHolder(final ListItemViewHolder holder, int position) {
            final Task task = mList.get(position);
            holder.mTitle.setText(task.getmTitle());
            if (task.ismCompleted()) {
                holder.mTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            holder.mDescription.setText(task.getmDescription());
           /* if ("".equals(task.getmDescription())) {
                holder.mDescription.setVisibility(View.GONE);
            } else {
                holder.mDescription.setText(task.getmDescription());
            }*/
            holder.mCheckBox.setChecked(task.ismCompleted());
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task.ismCompleted()) {
                        //Log.d(TAG, "onClick: activateTask :" + task);
                        mTasksRepository.activateTask(task);
                        showSnackerBar("activate task");
                        holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    } else {
                        //Log.d(TAG, "onClick: completeTask :" + task);
                        mTasksRepository.completeTask(task);
                        showSnackerBar("complete task");
                        holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    loadTask();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void replaceData(List<Task> mTaskList) {
            setList(mTaskList);
            notifyDataSetChanged();
        }

        public void setList(List<Task> list) {
            mList = list;
        }
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private CheckBox mCheckBox;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.task_title);
            mDescription = (TextView) itemView.findViewById(R.id.task_description);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.task_checkbox);
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
