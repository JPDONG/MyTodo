package com.learn.mytodo;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        setToolbar();

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
                break;
            case R.id.item_notification:
                Utils.showToast(this,getResources().getString(R.string.item_notification));
                break;
            case R.id.item1:
                Utils.showToast(this,getResources().getString(R.string.title_item1));
                break;
            case R.id.item2:
                Utils.showToast(this,getResources().getString(R.string.title_item2));
                break;
            default:
                break;
        }
        return false;
    }
}
