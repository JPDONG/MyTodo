package com.learn.mytodo.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.learn.mytodo.R;
import com.learn.mytodo.addedittask.AddEditTaskActivity;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;
import com.learn.mytodo.taskdetail.TaskDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskListFragment extends Fragment implements TasksContract.TasksView{
    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Task> mTaskList;
    private TasksLocalDataSource mTasksLocalDataSource;
    private TasksRemoteDataSource mTasksRemoteDataSource;
    private TasksRepository mTasksRepository;
    private View mDialogView;
    private String TAG = "MainActivity:TaskListFragment";
    private boolean mTestRemoteData = false;
    private TasksContract.TasksPresenter mTasksPresenter;


    public TaskListFragment() {

    }

    public static TaskListFragment getInstance() {
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mTaskList = new ArrayList(0);
        mTaskListAdapter = new TaskListAdapter(mTaskList);
        mTasksLocalDataSource = new TasksLocalDataSource(getContext());
        mTasksRemoteDataSource = new TasksRemoteDataSource(getContext());
        mTasksRepository = TasksRepository.getInstance(mTasksLocalDataSource, mTasksRemoteDataSource);
        if (mTestRemoteData) {
            mTasksRepository.refreshTasks();
            mTestRemoteData = false;
        }
        //loadTask();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasklist_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        setupRecyclerView();
        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mTasksPresenter.addNewTask();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                mDialogView = inflater.inflate(R.layout.addtask_dialog, container, false);
                builder.setView(mDialogView);
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTitle = (EditText) mDialogView.findViewById(R.id.edit_title);
                        EditText editDescription = (EditText) mDialogView.findViewById(R.id.edit_description);
                        String title = editTitle.getText().toString().trim();
                        String description = editDescription.getText().toString().trim();
                        Log.d(TAG, "onClick: title = " + title + ", description = " + description);
                        if ("".equals(title.trim())) {
                            showSnackerMessage("nothing to add");
                            return;
                        }
                        Task task = new Task(title, description);
                        addTask(task);
                        showSnackerMessage("add sucess");
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTask();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void showSnackerMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskDetail(Task task) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.TASK_ID, task.getmId());
        startActivity(intent);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    private void loadTask() {
        //mTasksPresenter.loadTasks(false);
        /**
         * test taskrepository
         */
        final List<Task> taskList = new ArrayList<Task>();
        mTasksRepository.getTask(new TasksRepository.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> task) {
                for (Task t : task) {
                    taskList.add(t);
                }
            }

            @Override
            public void onDataNotAvailabel() {
            }
        });
        mTaskListAdapter.replaceData(taskList);
    }

    private void addTask(Task task) {
        //mTaskList.add("new task");
        mTasksRepository.saveTask(task);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mTaskListAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                Log.d(TAG, "onScrolled: topRowVerticalPosition = " + topRowVerticalPosition);
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        TaskItemTouchHelperCallback taskItemTouchHelperCallback = new TaskItemTouchHelperCallback(mTaskListAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(taskItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTaskListAdapter.replaceData(tasks);
    }

    @Override
    public void setPresenter(TasksContract.TasksPresenter tasksPresenter) {
        mTasksPresenter = tasksPresenter;
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
            //holder.mDescription.setText(task.getmDescription());
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
                        mTasksPresenter.activateTask(task);
                        holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    } else {
                        //Log.d(TAG, "onClick: completeTask :" + task);
                        mTasksPresenter.completeTask(task);
                        holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    loadTask();
                }
            });
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTasksPresenter.openTaskDetail(task);
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

        public void remove(int position) {
            mTasksLocalDataSource.deleteTask(mList.get(position));
            mList.remove(position);
            notifyItemRemoved(position);
            mTasksRepository.refreshDB();
            //loadTask();
        }

        public void setList(List<Task> list) {
            mList = list;
        }
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private CheckBox mCheckBox;
        private View mItemView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.task_title);
            //mDescription = (TextView) itemView.findViewById(R.id.task_description);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.task_checkbox);
        }
    }

    class TaskItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        private TaskListAdapter taskListAdapter;

        public TaskItemTouchHelperCallback(TaskListAdapter adapter) {
            super(0, ItemTouchHelper.LEFT);
            this.taskListAdapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mSwipeRefreshLayout.setEnabled(false);
            int oldPosition = viewHolder.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();
            if (oldPosition < targetPosition) {
                for (int i = oldPosition; i < targetPosition; i++) {
                    Collections.swap(taskListAdapter.mList, i , i + 1);
                }
            } else {
                for (int i = oldPosition; i > targetPosition; i--) {
                    Collections.swap(taskListAdapter.mList, i, i - 1);
                }
            }
            taskListAdapter.notifyItemMoved(oldPosition, targetPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int deletePosition = viewHolder.getAdapterPosition();
            taskListAdapter.remove(deletePosition);
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
        Log.d(TAG, "onResume: ");
        mTasksPresenter.start();
        mTaskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
