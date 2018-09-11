package com.lcgsen.doview;

import android.content.ClipData;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.MainActivity;
import com.lcgsen.master.R;
import com.lcgsen.utils.DragFloatActionButton;
import com.lcgsen.utils.ViewHelper;
import com.lcgsen.utils.custom.EllipsizingTextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtils extends Fragment {

    private TextView tv;
    private DragFloatActionButton floatingView;
    private ListView lv;
    private LinearLayout linearLayout;


    public static FragmentUtils newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentUtils fragment = new FragmentUtils();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        View view;
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                view = inflater.inflate(R.layout.fragment_one, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment, container, false);
            }
        } else {
            view = inflater.inflate(R.layout.fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                floatingView = (DragFloatActionButton) view.findViewById(R.id.fab);
                floatingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(), "FAB clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                addTextView(view);

            } else {
                tv = (TextView) view.findViewById(R.id.fragment_test_tv);
                String name = nameObj.toString();
                tv.setText(name);
            }
        }
    }

    private ListView addTextView(final View view) {
        lv = view.findViewById(R.id.lv); // 初始化控件
        // 实例化 adapter
        String[] data = new String[]{"暹罗猫", "布偶猫", "折耳猫", "短毛猫", "波斯猫", "蓝猫", "森林猫",
                "孟买猫", "缅因猫", "埃及猫", "伯曼猫", "缅甸猫", "新加坡猫", "美国短尾猫", "巴厘猫"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                /*
                 * 点击列表项时触发onItemClick方法，四个参数含义分别为
                 * arg0：发生单击事件的AdapterView
                 * arg1：AdapterView中被点击的View
                 * position：当前点击的行在adapter的下标
                 * id：当前点击的行的id
                 */
                Toast.makeText(view.getContext(), "您选择的是" + lv.getAdapter().getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        return lv;
    }


}
