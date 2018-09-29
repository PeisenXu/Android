package com.lcgsen.master.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcgsen.master.R;
import com.lcgsen.master.adapter.HomeView1;
import com.lcgsen.master.adapter.HomeView2;
import com.lcgsen.master.adapter.HomeView3;

@SuppressLint("ValidFragment")
public class FragmentUtils extends Fragment {

    private LayoutInflater mInflater;
    private Context mainContext;

    private FragmentUtils(Context context) {
        this.mainContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // 传递主页面View对象调用构造函数赋值
    public static FragmentUtils newInstance(Context context, String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentUtils fragment = new FragmentUtils(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        try {
            if (inflater == null) {
                throw new Exception();
            }
            Bundle bundle = getArguments();
            Object nameObj = bundle.get("name");
            if (nameObj != null) {
                if ("One".equalsIgnoreCase(nameObj.toString())) {
                    view = inflater.inflate(R.layout.fragment_one, container, false);
                    // 先画指定显示的页面布局, 然后再加载数据
                    HomeView1 homeView1 = new HomeView1(mainContext, view);
                    homeView1.start();
                } else if ("Two".equalsIgnoreCase(nameObj.toString())) {
                    view = inflater.inflate(R.layout.fragment_two, container, false);
                    // 先画指定显示的页面布局, 然后再加载数据
                    HomeView2 homeView2 = new HomeView2(mainContext, view);
                    homeView2.start();
                } else if ("Three".equalsIgnoreCase(nameObj.toString())) {
                    view = inflater.inflate(R.layout.fragment_three, container, false);
                    // 先画指定显示的页面布局, 然后再加载数据
                    HomeView3 homeView3 = new HomeView3(mainContext, view);
                    homeView3.start();
                } else {
                    view = inflater.inflate(R.layout.fragment, container, false);
                    TextView tv = view.findViewById(R.id.fragment_test_tv);
                    String name = nameObj.toString();
                    tv.setText(name);
                }
            } else {
                view = inflater.inflate(R.layout.fragment, container, false);
            }
        } catch (Exception e) {
            view = null;
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showAndHide(int a, int b, View view) {
        // 显示
        view.setVisibility(View.VISIBLE);
        // 隐藏
        view.setVisibility(View.INVISIBLE);
    }
}
