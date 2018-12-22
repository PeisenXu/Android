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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcgsen.entity.AccountTask;
import com.lcgsen.enums.DBServiceError;
import com.lcgsen.master.adapter.MyRecyclerViewAdapter;
import com.lcgsen.utils.HttpUtils;
import com.lcgsen.utils.SharedUtils;
import com.lcgsen.utils.ViewHelper;
import com.lcgsen.utils.httpurlconnectionutil.HttpCallbackStringListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView; // 左滑动菜单
    private View headerView; // 左滑动菜单 --> 侧滑顶部布局
    private ImageView home_left_img; // 首页左上角菜单图标
    private LinearLayout home_layout; // 首页主布局
    private CoordinatorLayout home_recycler_view; // 首页嵌入布局

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

        // 加载数据 首页逻辑于 init()
        initWindow();
        init2();

        // 设置侧滑栏动态信息
        userName.setText(SharedUtils.getParam(MainActivity.this, "USER_NAME", "未知登陆").toString());
        userCreateTime.setText(SharedUtils.getParam(MainActivity.this, "USER_CREATE_TIME", "加入时间:未来").toString());

        navigationView.getMenu().add(1,1,2,"语音智能");//需要获取id的话，id就等于1；
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


    private long exitTime = 0; // 返回按钮连续点击时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮
            exit();
            return true;
        }
        return true;
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
                Toast.makeText(MainActivity.this, "无访问权限", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(MainActivity.this, RobotsActivity.class));
            }
            item.setChecked(true);

            drawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

    /**
     * 初始化数据
     */

    private List<String> list;
    private RecyclerView mRecyclerView;

    private void init2() {
        home_recycler_view = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.recycler_view, null);
        home_layout.addView(home_recycler_view);

        mRecyclerView = findViewById(R.id.recycler_view);

        addView();

        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));

        setGestureListener();
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

    @Override
    public void onClick(View v) {

    }

    private float mPosX = 0, mPosY = 0, mCurPosX = 0, mCurPosY = 0;

    private void setGestureListener() {
        // 是要监听的视图 mAlertImageViewD
        // TODO
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if ((mPosX - mCurPosX) > 40) {
                            addView();
                        }
                        break;
                }
                return true;
            }

        });
    }

    public void addView() {
        //返回字符串
        try {
            String url = DBServiceError.DB_SERVICE_URL.getMsg() + "/app/driver/driver.select.php?select=SELECT%20*%20FROM%20account_task%20ORDER%20BY%20RAND()%20LIMIT%208";
            HttpUtils.doGet(MainActivity.this, url, new HttpCallbackStringListener() {
                @Override
                public void onFinish(String response) {
                    Gson gson = new Gson();
                    List<AccountTask> accountTasks = gson.fromJson(response, new TypeToken<List<AccountTask>>() {
                    }.getType());
                    list = new ArrayList<String>();
                    for (AccountTask accountTask : accountTasks) {
                        list.add(accountTask.getTitle());
                    }
                    Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();


                    //初始化适配器
                    MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(list);
                    mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String data) {
                        }
                    });
                    mAdapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(View view, int position, String data) {
                            // 兼容旧版Android复制剪贴板
                            ClipboardManager cm = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(data);
                            Toast.makeText(MainActivity.this, DBServiceError.COPY_SUCCESS.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //设置适配器
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("错误", e.toString());
                    Toast.makeText(MainActivity.this, DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}