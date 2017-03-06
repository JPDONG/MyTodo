package com.learn.mytodo.data.source;

import com.learn.mytodo.data.Task;

import junit.framework.Test;
import junit.framework.TestCase;

import static org.junit.Assert.*;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */
public class TasksRepositoryTest extends TestCase{

    public void test(){
        Task task = new Task("12","");
        assertTrue("".equals(task.getmId()));
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