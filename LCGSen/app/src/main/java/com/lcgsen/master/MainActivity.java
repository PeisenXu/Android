package com.lcgsen.master;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.doview.FragmentUtils;
import com.lcgsen.doview.ViewPagerAdapter;
import com.lcgsen.utils.DragFloatActionButton;
import com.lcgsen.utils.SharedUtils;
import com.lcgsen.utils.ViewHelper;
import com.lcgsen.utils.viewstyle.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private TextView userName;
    private TextView userCreateTime;
    private ImageView personImage;

    private ListView listview;
    /* private float mFirstY;
     private float mCurrentY;
     protected float mTouchSlop;*/
    private ObjectAnimator mAnimatorTitle;
    private ObjectAnimator mAnimatorContent;

    private ViewPager viewPager;
    private String[] data;

    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_view);

        navigationView = (NavigationView) findViewById(R.id.nav);
        headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        userCreateTime = headerView.findViewById(R.id.user_create_time);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
        // listview = (ListView) findViewById(R.id.list);
        navigation = (BottomNavigationView) findViewById(R.id.bottomSelectView);

        // 加载数据
        init();
        initWindow();

        // 设置侧滑栏动态信息
        userName.setText(SharedUtils.getParam(MainActivity.this, "USER_NAME", "未知登陆").toString());
        userCreateTime.setText(SharedUtils.getParam(MainActivity.this, "USER_CREATE_TIME", "加入时间:未来").toString());

        // 设置侧滑菜单监听
        navigationView.setNavigationItemSelectedListener(navigationListener);

        // 设置主页面监听滑动
        viewPager.addOnPageChangeListener(viewPagerOnPageChange);

        // mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(new ViewConfiguration());
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

    private ViewPager.OnPageChangeListener viewPagerOnPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            String[] titles = new String[]{"微信", "通讯录", "发现", "我"};
            /* 此方法在页面被选中时调用 */
            // title.setText(titles[position]);
            changeTextColor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navigationListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_login_out:
                    SharedUtils.clearLoginStatus(MainActivity.this, "ALL");
                    Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                case R.id.navigation_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
            item.setChecked(true);

            drawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.bottom_tools:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.bottom_me:
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        List<Fragment> list = new ArrayList<>();

        list.add(FragmentUtils.newInstance("首页"));
        list.add(FragmentUtils.newInstance("工具"));
        list.add(FragmentUtils.newInstance("我"));

        viewPagerAdapter.setList(list);
    }

    private void initWindow() {
        // 初始化窗口属性，让状态栏和导航栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            // 如果是高版本手机， 侧滑栏图标会被状态栏遮挡， 向下移动
            personImage = (ImageView) headerView.findViewById(R.id.person);
            ViewHelper.setMargins(personImage, 10, ViewHelper.getStatusBarHeight(MainActivity.this), 0, 0);
        }

        // finish掉登陆界面
        if (LoginActivity.instance != null) {
            LoginActivity.instance.finish();
        }

        // 设置页面滑动效果
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        // 设置侧滑菜单宽度
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 1 / 2;
        navigationView.setLayoutParams(params);
        navigationView.setBackgroundColor(Color.argb(120, 200, 200, 200));
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

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
    }

}