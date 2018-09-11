package com.lcgsen.doview;

import android.content.ClipData;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import static android.support.v4.content.ContextCompat.getSystemService;

public class FragmentUtils extends Fragment {

    private TextView tv;
    private DragFloatActionButton floatingView;
    private ListView lv;
    private LinearLayout linearLayout;
    private int oldPosition;
    private ArrayList<String> list;


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

        list = new ArrayList<>();
        String[] data = new String[]{"暹罗猫", "布偶猫", "折耳猫", "短毛猫", "波斯猫", "蓝猫", "森林猫", "孟买猫", "缅因猫", "埃及猫", "伯曼猫", "缅甸猫", "新加坡猫", "美国短尾猫", "巴厘猫"};
        for (String name : data) {
            list.add(name);
        }

        adapter = new MyAdapter();
        lv.setAdapter(adapter);


        // 实例化 adapter
        /*

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.item_layout, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                *//*
         * 点击列表项时触发onItemClick方法，四个参数含义分别为
         * arg0：发生单击事件的AdapterView
         * arg1：AdapterView中被点击的View
         * position：当前点击的行在adapter的下标
         * id：当前点击的行的id
         *//*
                Toast.makeText(view.getContext(), "您选择的是" + lv.getAdapter().getItem(position), Toast.LENGTH_SHORT).show();
            }
        });*/
        return lv;
    }

    private void showAndHide(int a, int b, View view) {
        // 显示
        view.setVisibility(View.VISIBLE);
        // 隐藏
        view.setVisibility(View.INVISIBLE);
    }


    private MyAdapter adapter;

    class MyAdapter extends BaseAdapter implements View.OnClickListener {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_layout, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }
            vh.tv_test.setText(list.get(position));

            //刷新adapter的时候，getview重新执行，此时对在点击中标记的position做处理
            if (oldPosition == position) {//当条目为刚才点击的条目时
                if (vh.ll_hide.getVisibility() == View.VISIBLE) {//当条目状态图标为选中时，说明该条目处于展开状态，此时让它隐藏，并切换状态图标的状态。
                    vh.ll_hide.setVisibility(View.GONE);
                    oldPosition = -1;//隐藏布局后需要把标记的position去除掉，否则，滑动listview让该条目划出屏幕范围.
                } else {//当状态条目处于未选中时，说明条目处于未展开状态，此时让他展开。同时切换状态图标的状态。
                    vh.ll_hide.setVisibility(View.VISIBLE);
                }
            } else {
                vh.ll_hide.setVisibility(View.GONE);
            }

            vh.hide_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(list.get(position));
                    Toast.makeText(getContext(), "复制成功，快去粘贴吧", Toast.LENGTH_SHORT).show();
                    oldPosition = position;//记录点击的position
                    notifyDataSetChanged();//刷新adapter重新填充条目。在重新填充的过程中，被记录的position会做展开或隐藏的动作，具体的判断看上面代码
                    //在此处需要明确的一点是，当adapter执行刷新操作时，整个getview方法会重新执行，也就是条目重新做一次初始化被填充数据。
                    //所以标记position，不会对条目产生影响，执行刷新后 ，条目重新填充当，填充至所标记的position时，我们对他处理，达到展开和隐藏的目的。
                    //明确这一点后，每次点击代码执行逻辑就是 onclick（）---》getview（）
                }
            });

            vh.hide_2.setOnClickListener(this);
            vh.hide_3.setOnClickListener(this);
            vh.hide_4.setOnClickListener(this);
            vh.hide_5.setOnClickListener(this);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), list.get(position) + "被点了", Toast.LENGTH_SHORT).show();
                    oldPosition = position;//记录点击的position
                    notifyDataSetChanged();//刷新adapter重新填充条目。在重新填充的过程中，被记录的position会做展开或隐藏的动作，具体的判断看上面代码
                    //在此处需要明确的一点是，当adapter执行刷新操作时，整个getview方法会重新执行，也就是条目重新做一次初始化被填充数据。
                    //所以标记position，不会对条目产生影响，执行刷新后 ，条目重新填充当，填充至所标记的position时，我们对他处理，达到展开和隐藏的目的。
                    //明确这一点后，每次点击代码执行逻辑就是 onclick（）---》getview（）
                }
            });
            return convertView;
        }

        @Override
        public void onClick(View v) {
        }

        class MyViewHolder {
            View itemView;
            TextView tv_test;
            TextView hide_1, hide_2, hide_3, hide_4, hide_5;
            ImageView selectorImg;
            LinearLayout ll_hide;

            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                tv_test = (TextView) itemView.findViewById(R.id.text1);
                hide_1 = (TextView) itemView.findViewById(R.id.hide_1);
                hide_2 = (TextView) itemView.findViewById(R.id.hide_2);
                hide_3 = (TextView) itemView.findViewById(R.id.hide_3);
                hide_4 = (TextView) itemView.findViewById(R.id.hide_4);
                hide_5 = (TextView) itemView.findViewById(R.id.hide_5);
                ll_hide = (LinearLayout) itemView.findViewById(R.id.ll_hide);
            }
        }
    }
}
