package com.lcgsen.master.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.a520wcf.yllistview.YLListView;
import com.allen.library.SuperTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcgsen.entity.AccountTask;
import com.lcgsen.enums.DBServiceError;
import com.lcgsen.master.R;
import com.lcgsen.master.refresh.IRefreshListener;
import com.lcgsen.master.refresh.RefreshRelativeLayout;
import com.lcgsen.utils.HttpUtils;
import com.lcgsen.utils.httpurlconnectionutil.HttpCallbackStringListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private String weburl;
    private String channelName;
    private List<String> list;
    private YLListView listView;
    private View topView;
    private RefreshRelativeLayout refreshRelativeLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {//优化View减少View的创建次数

            View returnView = new View(inflater.getContext());

            if ("句子".equalsIgnoreCase(channelName)) {

                listView = new YLListView(inflater.getContext());

                // 不添加也有默认的头和底
                topView = View.inflate(inflater.getContext(), R.layout.home_top, null);

                // 加载主页下拉图片
                // new DownloadImageTask().execute("http://www.xupeisen.com/app/android/img/home_top.jpg");

                listView.addHeaderView(topView);
                // View bottomView=new View(getApplicationContext());
                // istView.addFooterView(bottomView);

                // 顶部和底部也可以固定最终的高度 不固定就使用布局本身的高度
                listView.setFinalBottomHeight(0);
                listView.setFinalTopHeight(0);
                listView.setPadding(0, 100, 0, 0);

                refreshData(inflater);

                refreshRelativeLayout = new RefreshRelativeLayout(inflater.getContext());

/*                refreshRelativeLayout.setPositiveRefresher(new PositiveRefresherWithText(false));
                refreshRelativeLayout.setNegativeRefresher(new NegativeRefresherWithNodata(false));
                refreshRelativeLayout.setNegativeOverlayUsed(true);
                refreshRelativeLayout.setPositiveOverlayUsed(false);*/


                refreshRelativeLayout.addRefreshListener(new IRefreshListener() {
                    @Override
                    public void onPositiveRefresh() {
                        positiveRefresh(inflater);
                    }

                    @Override
                    public void onNegativeRefresh() {
                        negativeRefresh(inflater);
                    }
                });

                refreshRelativeLayout.addView(listView);
                refreshRelativeLayout.startPositiveRefresh();

                returnView = refreshRelativeLayout;
            } else if("Xposed".equalsIgnoreCase(channelName)){
                TextView tvTitle = new TextView(getActivity());
                tvTitle.setText("支付宝默认修改财富页面余额为981210.00\n修改支付宝付款页面等级为钻石会员");
                tvTitle.setTextSize(50);
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                returnView = tvTitle;
            } else {
                // 该部分可通过xml文件设计Fragment界面，再通过LayoutInflater转换为View组件
                // 这里通过代码为fragment添加一个TextView
                TextView tvTitle = new TextView(getActivity());
                tvTitle.setText("未登陆，无授权");
                tvTitle.setTextSize(50);
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                returnView = tvTitle;
            }
            view = returnView;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) { // 如果View已经添加到容器中，要进行删除，否则会报错
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void setArguments(Bundle bundle) {//接收传入的数据
        weburl = bundle.getString("weburl");
        channelName = bundle.getString("name");
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        protected void onPostExecute(Drawable result) {
            // TODO Auto-generated method stub
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                topView.setBackground(result);
            }
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {

        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }

    /**
     * 初始化数据
     */
    class DemoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // https://blog.csdn.net/u011622280/article/details/77159837

            SuperTextView s = (SuperTextView) getLayoutInflater().inflate(R.layout.home_supertext_view, null);
            s.setLeftTopString("")
                    .setLeftString("")
                    .setLeftBottomString("")
                    .setCenterTopString("")
                    .setCenterString(list.get(position))
                    .setCenterBottomString("")
                    .setRightTopString("")
                    .setRightString("")
                    .setRightBottomString("")
                    .setLeftIcon(0)
                    .setRightIcon(0)
                    .setCbChecked(true)
                    .setCbBackground(null)
                    .setLeftTvDrawableLeft(null)
                    .setLeftTvDrawableRight(null)
                    .setCenterTvDrawableLeft(null)
                    .setCenterTvDrawableRight(null)
                    .setRightTvDrawableLeft(null)
                    .setRightTvDrawableRight(null);

            s.setShapeCornersRadius(20)
                    .setShapeCornersTopLeftRadius(40)
                    .setShapeCornersBottomLeftRadius(20)
                    .setShapeCornersTopRightRadius(20)
                    .setShapeCornersBottomRightRadius(20)
                    .setShapeStrokeColor(getResources().getColor(R.color.colorPrimary))
                    .setShapeStrokeWidth(1)
                    .setShapeSrokeDashWidth(1)
                    .setShapeStrokeDashGap(5)
                    .setShapeSolidColor(getResources().getColor(R.color.white))
                    .setShapeSelectorNormalColor(getResources().getColor(R.color.white))
                    .setShapeSelectorPressedColor(getResources().getColor(R.color.common_pressed))
                    .useShape();//设置完各个参数之后这句调用才生效
            return s;
        }

    }

    public void refreshData(final LayoutInflater inflater) {
        //返回字符串
        try {
            String url = DBServiceError.DB_SERVICE_URL.getMsg() + "/app/driver/driver.select.php?select=SELECT%20*%20FROM%20account_task%20ORDER%20BY%20RAND()%20LIMIT%2020";
            HttpUtils.doGet(inflater.getContext(), url, new HttpCallbackStringListener() {
                @Override
                public void onFinish(String response) {
                    Gson gson = new Gson();
                    List<AccountTask> accountTasks = new ArrayList<>();
                    try {
                        accountTasks = gson.fromJson(response, new TypeToken<List<AccountTask>>() {
                        }.getType());
                    } catch (Exception e) {
                        Log.e("错误", e.toString());
                        Toast.makeText(inflater.getContext(), DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = new ArrayList<>();
                    for (AccountTask accountTask : accountTasks) {
                        list.add(accountTask.getTitle());
                    }

                    // 数据获取成功后， 加载数据
                    listView.setAdapter(new DemoAdapter());

                    //YLListView默认有头和底  处理点击事件位置注意减去
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //  position = position - listView.getHeaderViewsCount();
                            Toast.makeText(inflater.getContext(), "别点了", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("错误", e.toString());
                    Toast.makeText(inflater.getContext(), DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("错误", e.toString());
            Toast.makeText(inflater.getContext(), DBServiceError.DB_SERVICE_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private void positiveRefresh(final LayoutInflater inflater) {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // listView.removeAllViews();
                refreshRelativeLayout.positiveRefreshComplete();
                addViewsToTarget(inflater);
            }
        }, 1000);
    }

    private void negativeRefresh(final LayoutInflater inflater) {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshRelativeLayout.negativeRefreshComplete();
                addViewsToTarget(inflater);
            }
        }, 1000);
    }

    private void addViewsToTarget(LayoutInflater inflater) {
        refreshData(inflater);
    }
}
