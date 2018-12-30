package com.lcgsen.master;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.hjm.bottomtabbar.BottomTabBar;
import com.lcgsen.master.fragment.CartoonFragment;
import com.lcgsen.master.fragment.HomeFragment;
import com.lcgsen.master.fragment.MoveFragment;
import com.lcgsen.master.fragment.TVplayFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tq on 2018/8/9.
 */

public class HomeActivity extends FragmentActivity {
    @BindView(R.id.tabBar)
    BottomTabBar mTabBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initWindow();

        setContentView(R.layout.video_home);
        ButterKnife.bind(this);
        mTabBar.init(getSupportFragmentManager())
                .setImgSize(0, 0)
                .setFontSize(20)
                .setTabPadding(6, 6, 10)
                .setChangeColor(Color.RED, Color.DKGRAY)
                .addTabItem("首页", null, HomeFragment.class)
                .addTabItem("电影", null, MoveFragment.class)
                .addTabItem("电视剧", null, TVplayFragment.class)
                .addTabItem("动漫", null, CartoonFragment.class);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /** 修改状态栏为全透明开始 **/
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            /** 修改状态栏为全透明结束 **/

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
    }
}
