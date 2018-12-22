package com.lcgsen.master;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lcgsen.enums.DBServiceError;
import com.lcgsen.entity.LoginResponse;
import com.lcgsen.utils.HttpUtils;
import com.lcgsen.utils.MD5Utils;
import com.lcgsen.utils.SharedUtils;
import com.lcgsen.utils.httpurlconnectionutil.HttpCallbackStringListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    public static LoginActivity instance = null;
    private TextView tv_register, tv_find_psw;//显示的注册，找回密码
    private Button btn_login;//登录按钮
    private String userName, psw, spPsw;//获取的用户名，密码，加密密码
    private EditText et_user_name, et_psw;//编辑框
    private static Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        instance = this;

        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    //获取界面控件
    private void init() {
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);

        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("立即注册".equalsIgnoreCase(tv_register.getText().toString())) {
                    btn_login.setText("注册");
                    tv_register.setText("返回登陆");
                } else {
                    btn_login.setText("登录");
                    tv_register.setText("立即注册");
                }
                //为了跳转到注册界面，并实现注册功能
                //Intent intent=new Intent(LoginActivity.this.RegisterActivity.class);
                // startActivityForResult(intent, 1);
            }
        });

        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码界面（此页面暂未创建）
                Toast.makeText(LoginActivity.this, "QQ75037664", Toast.LENGTH_SHORT).show();
            }
        });

        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("注册".equalsIgnoreCase(btn_login.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "抱歉,暂不开放注册", Toast.LENGTH_SHORT).show();
                    return;
                }

                //开始登录，获取用户名和密码 getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_NO_NAME.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_NO_PWD.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // MD5加密
                spPsw = MD5Utils.md5(psw);

                //返回字符串
                try {
                    String url = DBServiceError.DB_SERVICE_URL.getMsg() + "/app/driver/driver.login.php?phone=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(spPsw, "UTF-8");
                    HttpUtils.doGet(LoginActivity.this, url, new HttpCallbackStringListener() {
                        @Override
                        public void onFinish(String response) {
                            LoginResponse reponseData = new LoginResponse();
                            LoginResponse mapObj = gson.fromJson(response, reponseData.getClass());
                            if (!mapObj.getData().isEmpty()) {
                                //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                                SharedUtils.setParam(LoginActivity.this, "USER_STATUS", true);
                                SharedUtils.setParam(LoginActivity.this, "USER_NAME", userName);
                                SharedUtils.setParam(LoginActivity.this, "USER_CREATE_TIME", mapObj.getData().get(0).getCreateDate());

                                Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_SUCCESS.getMsg(), Toast.LENGTH_SHORT).show();

                                //登录成功后关闭此页面进入主页
                                Intent data = new Intent();
                                //datad.putExtra( ); name , value ;
                                data.putExtra("isLogin", true);
                                //RESULT_OK为Activity系统常量，状态码为-1
                                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                                setResult(RESULT_OK, data);
                                //销毁登录界面
                                LoginActivity.this.finish();
                                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                return;
                            }
                            Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("错误", e.toString());
                            Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, DBServiceError.DB_SERVICE_LOGIN_ERROR.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}