package com.learn.mytodo.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.Log;
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
import com.learn.mytodo.user.UserInformationActivity;
import com.learn.mytodo.user.UserInformationFragment;
import com.learn.mytodo.util.Utils;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private String TAG = "MainActivity";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TasksRepository mTasksRepository;
    private TasksLocalDataSource tasksLocalDataSource;
    private TasksRemoteDataSource tasksRemoteDataSource;
    private ImageView mUserIcon;
    private TextView mUserName;
    private TasksPresenter mTasksPresenter;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tasksLocalDataSource = new TasksLocalDataSource(getApplicationContext());
        tasksRemoteDataSource = new TasksRemoteDataSource(getApplicationContext());
        mTasksRepository = TasksRepository.getInstance(tasksLocalDataSource, tasksRemoteDataSource);
        initViews();
        Log.d(TAG, "onCreate: ");
        //SkinManager.getInstance().register(this);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        setToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar,R.string.open_string,R.string.close_string);
        actionBarDrawerToggle.syncState();


        TaskListFragment taskListFragment = new TaskListFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        mTasksPresenter = new TasksPresenter(this, taskListFragment);
        taskListFragment.setPresenter(mTasksPresenter);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.content_frame,taskListFragment);
        fragmentTransaction.replace(R.id.content_frame,collectionFragment);
        fragmentTransaction.commit();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        mUserIcon = (ImageView) headerView.findViewById(R.id.user_icon);
        mUserName = (TextView) headerView.findViewById(R.id.user_name);
        mUserName.setOnClickListener(this);
        mUserIcon.setOnClickListener(this);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Navigation");
                mFragmentManager.popBackStack();
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_search:
                Utils.showToast(this,getResources().getString(R.string.item_search));
                return true;
            case R.id.item_sync:
                //mTasksRepository.syncData();
                if (!Utils.checkNetwork(this)) {
                    break;
                }
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
                Intent intent = new Intent(this, UserInformationActivity.class);
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
        Log.d(TAG, "onDestroy: ");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mUserName.setText(sharedPreferences.getString("user_name","user name"));
        String imagePath = sharedPreferences.getString("user_icon","");
        if (!"".equals(imagePath)) {
            mUserIcon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
}
