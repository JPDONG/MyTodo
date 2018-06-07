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
import com.learn.mytodo.view.SlideListLayout;
import com.learn.mytodo.view.SlideListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskListFragment extends Fragment {
    private SlideListView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Task> mTaskList;
    private TasksLocalDataSource mTasksLocalDataSource;
    private TasksRemoteDataSource mTasksRemoteDataSource;
    private TasksRepository mTasksRepository;
    private View mDialogView;
    private String TAG = "MainActivity:TaskListFragment";
    private boolean mTestRemoteData = false;
    private TasksPresenter mTasksPresenter;
    private String mCollectionId;
    private CompositeDisposable mCompositeDisposable;
    private FloatingActionButton mFloatingActionButton;

    private TaskItemClickListener mTaskItemClickListener = new TaskItemClickListener() {
        @Override
        public void onActive(ListItemViewHolder holder, Task task) {
            mCompositeDisposable.add(mTasksPresenter.activateTask(task)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean result) throws Exception {
                            if (result) {
                                holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                holder.mCheckBox.setChecked(false);
                                task.setCompleted(false);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "on error:" +throwable.toString() );
                        }
                    }));
        }

        @Override
        public void onComplete(ListItemViewHolder holder, Task task) {
            mCompositeDisposable.add(mTasksPresenter.completeTask(task)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean result) throws Exception {
                            if (result) {
                                holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.mCheckBox.setChecked(true);
                                task.setCompleted(true);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "on error:" +throwable.toString() );
                        }
                    }));
        }

        @Override
        public void onShowDetail(ListItemViewHolder viewHolder, Task task) {
            showTaskDetail(task);
        }

        @Override
        public void onDelete(int position) {
            removeItem(position);
        }

        @Override
        public void onMoveTop(ListItemViewHolder viewHolder, Task task) {

        }
    };

    public TaskListFragment() {

    }

    public void setCollectionId(String id) {
        this.mCollectionId = id;
    }

    public static TaskListFragment getInstance() {
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        mTasksPresenter = new TasksPresenter(getContext());
        mTaskList = new ArrayList(0);
        mTaskListAdapter = new TaskListAdapter(mTaskList,mTaskItemClickListener);
        mCompositeDisposable = new CompositeDisposable();
        View view = inflater.inflate(R.layout.tasklist_fragment, container, false);
        mRecyclerView = (SlideListView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        setupRecyclerView();
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mTasksPresenter.addNewTask();
                showAddTaskDialog(inflater, container);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTask();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        loadTask();
        return view;
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
        mFloatingActionButton.setOnClickListener(null);
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
        Log.d(TAG, "onDestroy: ");
    }

    private void removeItem(int position) {
        mCompositeDisposable.add(mTasksPresenter.delete(mTaskList.get(position))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) throws Exception {
                if (result) {
                    mTaskListAdapter.remove(position);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "removeItem on error:" +throwable.toString() );
            }
        }));
    }

    private void showAddTaskDialog(LayoutInflater inflater, ViewGroup container) {
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
                Task task = new Task(title, description, mCollectionId);
                addTask(task);
                showSnackerMessage("add sucess");
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

    public void showSnackerMessage(String message) {
        //Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public void showTaskDetail(Task task) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.TASK_ID, task.getId());
        startActivity(intent);
    }

    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    private void loadTask() {
        mCompositeDisposable.add(mTasksPresenter.getTaskList(mCollectionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        mTaskList = tasks;
                        mTaskListAdapter.replaceData(tasks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "on error: " + throwable.toString());
                    }
                }));

    }

    private void addTask(Task task) {
        //mTaskList.add("new task");
        //mTasksRepository.saveTask(task);
        mCompositeDisposable.add(mTasksPresenter.save(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            mTaskList.add(task);
                            mTaskListAdapter.replaceData(mTaskList);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "on error: " + throwable.toString());
                    }
                })
        );
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
        /*TaskItemTouchHelperCallback taskItemTouchHelperCallback = new TaskItemTouchHelperCallback(mTaskListAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(taskItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);*/
    }


    public void showTasks(List<Task> tasks) {
        mTaskListAdapter.replaceData(tasks);
    }


    public void setPresenter(TasksContract.TasksPresenter tasksPresenter) {
        //mTasksPresenter = tasksPresenter;
    }

    class TaskListAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

        private List<Task> mList;
        private TaskItemClickListener mListener;
        private boolean isSomeOneOpen = false;
        private View viewOpened;

        public TaskListAdapter(List<Task> list,TaskItemClickListener listener) {
            mList = list;
            mListener = listener;
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder listItemViewHolder = new ListItemViewHolder(getActivity().getLayoutInflater().inflate(R.layout.list_slide_item, parent, false));
            return listItemViewHolder;
        }

        @Override
        public void onBindViewHolder(final ListItemViewHolder holder, int position) {
            final Task task = mList.get(position);
            holder.mTitle.setText(task.getTitle());
            if (task.isCompleted()) {
                holder.mTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            holder.mCheckBox.setChecked(task.isCompleted());
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task.isCompleted()) {
                        //Log.d(TAG, "onClick: activateTask :" + task);
                        mListener.onActive(holder,task);
                    } else {
                        //Log.d(TAG, "onClick: completeTask :" + task);
                        mListener.onComplete(holder,task);
                    }
                }
            });
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onShowDetail(holder,task);
                }
            });
            final View itemView = holder.mItemView;
            ((SlideListLayout)holder.mItemView).setSlideListener(new SlideListLayout.OnSlideListener() {
                @Override
                public void onOpen() {
                    isSomeOneOpen = true;
                    viewOpened = itemView;
                    mRecyclerView.setCurrentViewOpened(itemView);
                }

                @Override
                public void onClose() {
                    isSomeOneOpen = false;
                    mRecyclerView.setViewClosed();
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDelete(position);
                }
            });
            holder.topButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoveTop(holder,task);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void replaceData(List<Task> mTaskList) {
            Log.d(TAG, "replaceData: size " + mTaskList.size());
            mList = mTaskList;
            setList(mTaskList);
            notifyDataSetChanged();
        }

        public void remove(int position) {
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
        private TextView deleteButton;
        private TextView topButton;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.task_title);
            //mDescription = (TextView) itemView.findViewById(R.id.task_description);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.task_checkbox);
            deleteButton = (TextView) itemView.findViewById(R.id.tv_slide_delete);
            topButton = (TextView) itemView.findViewById(R.id.tv_slide_top);
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
                    Collections.swap(taskListAdapter.mList, i, i + 1);
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
            //taskListAdapter.remove(deletePosition);
        }
    }

    interface TaskItemClickListener {
        void onActive(ListItemViewHolder viewHolder,Task task);
        void onComplete(ListItemViewHolder viewHolder,Task task);
        void onShowDetail(ListItemViewHolder viewHolder,Task task);
        void onDelete(int position);
        void onMoveTop(ListItemViewHolder viewHolder,Task task);
    }

}
