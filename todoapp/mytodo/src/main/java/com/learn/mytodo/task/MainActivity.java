package com.learn.mytodo.task;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.learn.mytodo.R;
import com.learn.mytodo.util.Utils;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        setToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar,R.string.open_string,R.string.close_string);
        actionBarDrawerToggle.syncState();
        TaskListFragment taskListFragment = new TaskListFragment();
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
            case R.id.item_notification:
                Utils.showToast(this,getResources().getString(R.string.item_notification));
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
}
