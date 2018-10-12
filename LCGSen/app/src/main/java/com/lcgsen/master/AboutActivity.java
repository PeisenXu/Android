package com.lcgsen.master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.favicon)// 图片
                .setDescription("不怕路远，就怕志短。\n（应用市场请在酷安打开）")// 介绍
                .addItem(new Element().setTitle("Version 1.4"))
                .addGroup("与我联系")
                .addEmail("admin@xupeisen.com")//邮箱
                .addWebsite("http://www.xupeisen.com")// 网站
                .addPlayStore("com.lcgsen.master")// 应用商店
                .addGitHub("PeisenXu")// github
                .create();
        setContentView(aboutPage);
    }
}
