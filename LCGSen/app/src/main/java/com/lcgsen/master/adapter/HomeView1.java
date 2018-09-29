package com.lcgsen.master.adapter;

import android.content.Context;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcgsen.entity.AccountTask;
import com.lcgsen.enums.DBServiceError;
import com.lcgsen.master.R;
import com.lcgsen.utils.DragFloatActionButton;
import com.lcgsen.utils.HttpUtils;
import com.lcgsen.utils.httpurlconnectionutil.HttpCallbackStringListener;

import java.util.ArrayList;
import java.util.List;

public class HomeView1 {

    private Context mainContext;
    private View tempView;

    private ListView listView; // 首页ListView
    private int oldPosition;
    private List<AccountTask> list;

    public HomeView1(Context mainContext, View tempView) {
        this.mainContext = mainContext;
        this.tempView = tempView;
    }

    public void start() {
        // 设置首页悬浮刷新按钮的监听事件
        DragFloatActionButton floatingView = tempView.findViewById(R.id.fab);
        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(tempView.getContext(), "请等待", Toast.LENGTH_SHORT).show();
                addTextView(tempView);
                Toast.makeText(tempView.getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        addTextView(tempView);
    }

    private ListView addTextView(final View view) {
        listView = view.findViewById(R.id.lv); // 初始化控件
        listView.setDivider(null);
        // 通过子页面查找父页面上的元素
        // mainView = ((ViewGroup) view.getParent().getParent()).findViewById(R.id.main_menu);

        list = new ArrayList<>();
        //返回字符串
        try {
            String url = DBServiceError.DB_SERVICE_URL.getMsg() + "/app/driver/driver.select.php?select=SELECT%20*%20FROM%20account_task%20ORDER%20BY%20RAND()%20LIMIT%2020";
            HttpUtils.doGet(mainContext, url, new HttpCallbackStringListener() {
                @Override
                public void onFinish(String response) {
                    Gson gson = new Gson();
                    list = gson.fromJson(response, new TypeToken<List<AccountTask>>() {
                    }.getType());

                    adapter = new MyAdapter();
                    listView.setAdapter(adapter);
                    return;
                }

                @Override
                public void onError(Exception e) {
                    Log.e("错误", e.toString());
                    Toast.makeText(mainContext, DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainContext, DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
        }
        return listView;
    }


    private MyAdapter adapter;

    private class MyAdapter extends BaseAdapter implements View.OnClickListener {
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
                convertView = View.inflate(mainContext, R.layout.item_layout, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }

            final String text = list.get(position) == null ? "" : list.get(position).getTitle();
            vh.tv_test.setText(text);

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
                    // 兼容旧版Android复制剪贴板
                    ClipboardManager cm = (ClipboardManager) mainContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(text);
                    Toast.makeText(mainContext, DBServiceError.COPY_SUCCESS.getMsg(), Toast.LENGTH_SHORT).show();
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
                    // Toast.makeText(getContext(), list.get(position) + "被点了", Toast.LENGTH_SHORT).show();
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
            LinearLayout ll_hide;

            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                tv_test = itemView.findViewById(R.id.text1);

                hide_1 = itemView.findViewById(R.id.hide_1);
                hide_2 = itemView.findViewById(R.id.hide_2);
                hide_3 = itemView.findViewById(R.id.hide_3);
                hide_4 = itemView.findViewById(R.id.hide_4);
                hide_5 = itemView.findViewById(R.id.hide_5);
                ll_hide = itemView.findViewById(R.id.ll_hide);
            }
        }
    }
}
