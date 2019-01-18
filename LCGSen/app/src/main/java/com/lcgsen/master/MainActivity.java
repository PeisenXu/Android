package com.lcgsen.master;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.adapter.PageFragmentAdapter;
import com.lcgsen.master.fragment.NewsFragment;
import com.lcgsen.utils.SharedUtils;
import com.lcgsen.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView; // 左滑动菜单
    private View headerView; // 左滑动菜单 --> 侧滑顶部布局
    private ImageView home_left_img; // 首页左上角菜单图标
    private LinearLayout home_layout; // 首页主布局
    private RelativeLayout homeListView; // 首页动态嵌入布局
    private LinearLayout main_layout;

    private long exitTime = 0; // 返回按钮连续点击时间

    private HorizontalScrollView hvChannel; // 首页顶部Tab
    private RadioGroup rgChannel; // 首页顶部Tab配套
    private ViewPager viewPager;  // 首页顶部Tab滑动显示用的view
    private List<Fragment> fragmentList = new ArrayList<Fragment>(); // 首页顶部TabFragment列表
    private PageFragmentAdapter adapter = null; // 首页顶部TabFragment列表Adapter

    private static List<String> channelList = new ArrayList<>();

    static {
        channelList.add("句子");
        channelList.add("Xposed");
        channelList.add("工具");
    }

    public static List<String> getSelectedChannel() {
        return channelList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_view);

        navigationView = findViewById(R.id.nav);
        headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userCreateTime = headerView.findViewById(R.id.user_create_time);
        drawerLayout = findViewById(R.id.activity_na);
        home_left_img = findViewById(R.id.main_menu);
        home_layout = findViewById(R.id.home_layout);

        // 加载数据
        initWindow();
        initHomeView();

        // 设置侧滑栏动态信息
        userName.setText(SharedUtils.getParam(MainActivity.this, "USER_NAME", "未知登陆").toString());
        userCreateTime.setText(SharedUtils.getParam(MainActivity.this, "USER_CREATE_TIME", "加入时间:未来").toString());

        navigationView.getMenu().add(1, 1, 1, "背景");//需要获取id的话，id就等于1；
        navigationView.getMenu().add(1, 2, 2, "报错");//需要获取id的话，id就等于1；
/*        navigationView.getMenu().add(1,2,2,"图库");
        navigationView.getMenu().add(1,3,2,"上传");*/

        // 设置侧滑菜单监听
        navigationView.setNavigationItemSelectedListener(navigationListener);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        };

        home_left_img.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮
            exit();
            return true;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void initHomeView() {
        String color = SharedUtils.getParam(MainActivity.this, "USER_COLOR", "").toString();
        if ("2".equals(color)) {
            main_layout = findViewById(R.id.main_layout);
            main_layout.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.about_github_color));
        }

        homeListView = (RelativeLayout) getLayoutInflater().inflate(R.layout.recycler_view, null);
        home_layout.addView(homeListView);

        /** 设置头部滑动标签开始 **/
        hvChannel = (HorizontalScrollView) findViewById(R.id.hvChannel);
        rgChannel = (RadioGroup) findViewById(R.id.rgChannel);
        viewPager = (ViewPager) findViewById(R.id.vpNewsList);

        rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewPager.setCurrentItem(checkedId);
            }
        });

        // 设置页面监听， 当页面切换后， 切换Tab选中
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initTab();// 动态产生RadioButton
        initViewPager();
        rgChannel.check(0);
        /** 设置头部滑动标签结束 **/
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
            ImageView personImage = headerView.findViewById(R.id.person);
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

        // 设置侧滑菜单宽度
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.5);
        navigationView.setLayoutParams(params);
        navigationView.setBackgroundColor(Color.argb(200, 200, 200, 200));
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    private void initTab() {
        List<String> channelList = MainActivity.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.tb_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb, params);
        }

    }

    private void initViewPager() {
        List<String> channelList = MainActivity.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            NewsFragment frag = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("weburl", channelList.get(i));
            bundle.putString("name", channelList.get(i));
            frag.setArguments(bundle);     //向Fragment传入数据
            fragmentList.add(frag);
        }
        adapter = new PageFragmentAdapter(super.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(0);
    }

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
        }
    }


    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private NavigationView.OnNavigationItemSelectedListener navigationListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String id = item.getItemId() + "";
            if (id.equalsIgnoreCase(R.id.navigation_login_out + "")) {
                SharedUtils.clearLoginStatus(MainActivity.this, "ALL");
                Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else if (id.equalsIgnoreCase(R.id.navigation_about + "")) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            } else if (id.equalsIgnoreCase(R.id.action_qq + "")) {
                if (isQQClientAvailable(MainActivity.this)) {
                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=75037664&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                } else {
                    Toast.makeText(MainActivity.this, "请安装QQ客户端", Toast.LENGTH_SHORT).show();
                }
            } else if (id.equalsIgnoreCase(R.id.video + "")) {
                Toast.makeText(MainActivity.this, "播放时如果白屏, 请返回多试几次", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else if (id.equalsIgnoreCase("1")) {
                String color = SharedUtils.getParam(MainActivity.this, "USER_COLOR", "").toString();
                if ("".equals(color) || "1".equals(color)) {
                    SharedUtils.setParam(MainActivity.this, "USER_COLOR", "2");
                    main_layout.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.about_github_color));
                } else {
                    SharedUtils.setParam(MainActivity.this, "USER_COLOR", "1");
                    main_layout.setBackgroundColor(0);
                }
                // Toast.makeText(MainActivity.this, "无访问权限", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(MainActivity.this, RobotsActivity.class));
            } else if (id.equalsIgnoreCase("2")) {
                /*Boolean setNull = true;
                if (setNull) {
                    throw new NullPointerException();
                }*/
            }


            item.setChecked(true);

            drawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

}