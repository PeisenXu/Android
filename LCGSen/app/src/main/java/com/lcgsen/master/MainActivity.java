package com.lcgsen.master;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.service.FragmentUtils;
import com.lcgsen.master.adapter.ViewPagerAdapter;
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
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_view);

        navigationView = findViewById(R.id.nav);
        headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        userCreateTime = headerView.findViewById(R.id.user_create_time);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.activity_na);
        navigation = findViewById(R.id.bottomSelectView);

        // 加载数据 首页逻辑于 init()
        init();
        initWindow();

        // 设置侧滑栏动态信息
        userName.setText(SharedUtils.getParam(MainActivity.this, "USER_NAME", "未知登陆").toString());
        userCreateTime.setText(SharedUtils.getParam(MainActivity.this, "USER_CREATE_TIME", "加入时间:未来").toString());

        // 设置侧滑菜单监听
        navigationView.setNavigationItemSelectedListener(navigationListener);

        // 设置主页面监听滑动
        viewPager.addOnPageChangeListener(viewPagerOnPageChange);

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
            // 设置底部菜单被选中样式
            navigation.getMenu().getItem(position).setChecked(true);
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

        // 目前所有操作全部在FragmentUtils处理
        list.add(FragmentUtils.newInstance(this,"One"));
        list.add(FragmentUtils.newInstance(this,"内测"));
        list.add(FragmentUtils.newInstance(this,"Three"));

        viewPagerAdapter.setList(list);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /** 修改状态栏为全透明开始 **/
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            /** 修改状态栏为全透明结束 **/


            // 如果是高版本手机， 侧滑栏图标会被状态栏遮挡， 向下移动
            personImage = headerView.findViewById(R.id.person);
            ViewHelper.setMargins(personImage, 10, ViewHelper.getStatusBarHeight(MainActivity.this), 0, 0);


            boolean isLight = true;
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (isLight) {
                //light --> a|=b的意思就是把a和b按位或然后赋值给a,   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                //dark  --> &是位运算里面，与运算,  a&=b相当于 a = a&b,  ~非运算符
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
        }

        // 设置页面滑动效果
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        // 设置侧滑菜单宽度
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 1 / 2;
        navigationView.setLayoutParams(params);
        navigationView.setBackgroundColor(Color.argb(200, 200, 200, 200));
    }
}