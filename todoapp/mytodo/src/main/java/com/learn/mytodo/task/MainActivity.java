package com.learn.mytodo.task;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mytodo.R;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;
import com.learn.mytodo.user.LoginActivity;
import com.learn.mytodo.user.UserInformationFragment;
import com.learn.mytodo.util.Utils;
import com.zhy.changeskin.SkinManager;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TasksRepository mTasksRepository;
    private TasksLocalDataSource tasksLocalDataSource;
    private TasksRemoteDataSource tasksRemoteDataSource;
    private ImageView mUserIcon;
    private TextView mUserName;
    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tasksLocalDataSource = new TasksLocalDataSource(getApplicationContext());
        tasksRemoteDataSource = new TasksRemoteDataSource(getApplicationContext());
        mTasksRepository = TasksRepository.getInstance(tasksLocalDataSource, tasksRemoteDataSource);
        initViews();
        //SkinManager.getInstance().register(this);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        setToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        mUserIcon = (ImageView) headerView.findViewById(R.id.user_icon);
        mUserName = (TextView) headerView.findViewById(R.id.user_name);
        mUserName.setOnClickListener(this);
        mUserIcon.setOnClickListener(this);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar,R.string.open_string,R.string.close_string);
        actionBarDrawerToggle.syncState();
        TaskListFragment taskListFragment = new TaskListFragment();
        mTasksPresenter = new TasksPresenter(this, taskListFragment);
        taskListFragment.setPresenter(mTasksPresenter);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,taskListFragment);
        fragmentTransaction.commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        break;
                    case R.id.item2:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void setToolbar() {
        /*mToolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        mToolbar.setLogo(R.mipmap.ic_zhihu_logo);
        mToolbar.setTitle(R.string.title_toolbar);
        mToolbar.setSubtitle(R.string.subtitle_toolbar);*/
        mToolbar.inflateMenu(R.menu.menu_item);
        mToolbar.setOnMenuItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_search:
                Utils.showToast(this,getResources().getString(R.string.item_search));
                return true;
            case R.id.item_sync:
                //mTasksRepository.syncData();
                mTasksPresenter.syncData();
                Utils.showToast(this,getResources().getString(R.string.item_sync));
                return true;
            case R.id.item1:
                Utils.showToast(this,getResources().getString(R.string.title_item1));
                return true;
            case R.id.item2:
                Utils.showToast(this,getResources().getString(R.string.title_item2));
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_icon:
                /*ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.activity_main);
                Scene scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.navigation_header, this);
                Scene scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.user_infomation_frag, this);
                TransitionManager.go(scene2);
                UserInformationFragment userInformationFragment = new UserInformationFragment();
                Slide slideTransition = new Slide(Gravity.RIGHT);
                slideTransition.setDuration(1000);
                userInformationFragment.setEnterTransition(slideTransition);
                userInformationFragment.setSharedElementEnterTransition(new Slide());
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, userInformationFragment)
                        .addSharedElement(mUserIcon, "transition")
                        .commit();
                */
                //
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.user_name:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }
}
