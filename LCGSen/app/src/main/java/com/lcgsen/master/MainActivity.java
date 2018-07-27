package com.lcgsen.master;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.utils.SharedUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private TextView userName;

    private ListView listview;
    private float mFirstY;
    private float mCurrentY;
    protected float mTouchSlop;
    private ObjectAnimator mAnimatorTitle;
    private ObjectAnimator mAnimatorContent;

    private ViewPager viewPager;
    private String[] data;

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_view);
        initWindow();

        // 加载数据
        // init();

        navigationView = (NavigationView) findViewById(R.id.nav);
        headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
        // listview = (ListView) findViewById(R.id.list);
        navigation = (BottomNavigationView) findViewById(R.id.bottomSelectView);


        // 设置侧滑菜单宽度
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 1 / 2;
        navigationView.setLayoutParams(params);
        navigationView.setBackgroundColor(Color.argb(120, 200, 200, 200));

        // 设置侧滑栏动态信息
        userName.setText(SharedUtils.getParam(MainActivity.this, "USER_NAME", "未知登陆").toString());

        // 设置侧滑菜单监听
        navigationView.setNavigationItemSelectedListener(navigationListener);

        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(new ViewConfiguration());

        // 设置列表监听
        // listview.setOnTouchListener(listViewListener);

        // 设置底部菜单监听
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu://点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
        }
    }
    private void initWindow() {
        // 初始化窗口属性，让状态栏和导航栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // finish掉登陆界面
        if (LoginActivity.instance != null) {
            LoginActivity.instance.finish();
        }
    }

    private NavigationView.OnNavigationItemSelectedListener navigationListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.file:
                    SharedUtils.clearLoginStatus(MainActivity.this, "USER_NAME");
                    Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                case R.id.favorite:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
            item.setChecked(true);

            drawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

    private View.OnTouchListener listViewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = event.getY();
                    if (mCurrentY - mFirstY > mTouchSlop) { // 下滑 显示titleBar
                        showHideTitlebar(true);
                    } else if (mFirstY - mCurrentY > mTouchSlop) {
                        showHideTitlebar(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favorite:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.dress:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.file:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    /**
     * 初始化数据
     */
    private void init() {
        data = new String[]{"暹罗猫", "布偶猫", "折耳猫", "短毛猫", "波斯猫", "蓝猫", "森林猫", "孟买猫", "缅因猫", "埃及猫", "伯曼猫", "缅甸猫", "新加坡猫", "美国短尾猫", "巴厘猫"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
       // ((ListView) findViewById(R.id.list)).setAdapter(adapter);

    }

    protected void showHideTitlebar(boolean tag) {
        LinearLayout titleBar = (LinearLayout) findViewById(R.id.titlebar);
        if (mAnimatorTitle != null && mAnimatorTitle.isRunning()) {
            mAnimatorTitle.cancel();
        }
        if (mAnimatorContent != null && mAnimatorContent.isRunning()) {
            mAnimatorContent.cancel();
        }
        if (tag) {
            mAnimatorTitle = ObjectAnimator.ofFloat(titleBar, "translationY", titleBar.getTranslationY(), 0);
            float y = listview.getTranslationY();
            int height = titleBar.getHeight();
            mAnimatorContent = ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), 0);
            float theY = listview.getY();
            System.out.println(theY);
        } else {
            mAnimatorTitle = ObjectAnimator.ofFloat(titleBar, "translationY", titleBar.getTranslationY(), -titleBar.getHeight());
            mAnimatorContent = ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), 0);
            float theY = listview.getY();
            System.out.println(theY);
        }
        mAnimatorContent.start();
        mAnimatorTitle.start();
    }
}