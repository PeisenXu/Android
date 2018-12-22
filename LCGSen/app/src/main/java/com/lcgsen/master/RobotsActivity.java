package com.lcgsen.master;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lcgsen.enums.DBServiceError;
import com.lcgsen.utils.HttpUtils;
import com.lcgsen.utils.httpurlconnectionutil.HttpCallbackStringListener;

import java.net.URLEncoder;

public class RobotsActivity extends FragmentActivity {
    private static String ApiKey = "98fc4e4f2d00405796034765a7991f95";
    private static String yourQuestion = "你好啊";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_robots);


        /** 修改状态栏为全透明开始 **/
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        /** 修改状态栏为全透明结束 **/



        try {
            String Info = URLEncoder.encode(yourQuestion, "utf-8");
            String url_path = "http://www.tuling123.com/openapi/api?key=" + ApiKey + "&info=" + Info;

            HttpUtils.doGet(RobotsActivity.this, url_path, new HttpCallbackStringListener() {
                @Override
                public void onFinish(String response) {
                    JsonObject returnData =  new JsonParser().parse(response).getAsJsonObject();
                    System.out.println(returnData.get("text"));//打印图灵机器人的回复
                }

                @Override
                public void onError(Exception e) {
                    Log.e("错误", e.toString());
                    Toast.makeText(RobotsActivity.this, DBServiceError.DB_SERVICE_LOGIN_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
