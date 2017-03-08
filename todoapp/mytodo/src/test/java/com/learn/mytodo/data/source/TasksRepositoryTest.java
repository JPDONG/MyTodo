package com.learn.mytodo.data.source;

import android.content.Context;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */
public class TasksRepositoryTest extends TestCase{

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private TasksLocalDataSource mTasksLocalDataSource;

    @Mock
    private Context mContext;

    @Mock
    private TasksRemoteDataSource mTasksRemoteDataSource;



    @Before
    public void setupTasksRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        mTasksLocalDataSource = Mockito.mock(TasksLocalDataSource.class);
        mTasksRemoteDataSource = Mockito.mock(TasksRemoteDataSource.class);
        // Get a reference to the class under test
        mTasksRepository = TasksRepository.getInstance(mTasksLocalDataSource, mTasksRemoteDataSource);
    }

    @Test
    public void test() {
        // Given a stub task with title and description
        Task newTask = new Task("title", "Some Task Description");

        // When a task is saved to the tasks repository
        mTasksRepository.saveTask(newTask);

        // Then the service API and persistent repository are called and the cache is updated
        verify(mTasksRemoteDataSource).saveTask(newTask);
        verify(mTasksLocalDataSource).saveTask(newTask);
        assertThat(mTasksRepository.mCacheTasks.size(), is(1));
    }

    @Test
    public void test1(){
        Task task = new Task("12","");
        assertFalse("".equals(task.getmId()));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}