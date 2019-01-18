package com.lcgsen.master.app.inventory.module.about;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lcgsen.master.app.inventory.BuildConfig;
import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.base.BaseActivity;
import com.lcgsen.master.app.inventory.helper.ToolbarHelper;

/**
 * author: xujiaji
 * created on: 2018/11/29 16:10
 * description:
 */
public class AboutActivity extends BaseActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public int layoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        ToolbarHelper.initMarginTopDiffBar(findViewById(R.id.btnBack));

        TextView tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(String.format("V%s",  BuildConfig.VERSION_NAME));

        Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(BuildConfig.DOWNLOAD_RQ)
                .into((ImageView) findViewById(R.id.imgRQ));
    }

    public void onClickAboutBack(View view) {
        finish();
    }

    public void onClickLicense(View view) {
        LicenseActivity.launch(this);
    }
}
