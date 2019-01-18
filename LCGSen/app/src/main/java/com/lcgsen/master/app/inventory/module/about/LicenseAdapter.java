package com.lcgsen.master.app.inventory.module.about;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.repository.bean.LicenseBean;

/**
 * author: xujiaji
 * created on: 2018/8/30 19:50
 * description:
 */
public class LicenseAdapter extends BaseQuickAdapter<LicenseBean, BaseViewHolder> {

    public LicenseAdapter() {
        super(R.layout.item_licenses);
    }

    @Override
    protected void convert(BaseViewHolder helper, LicenseBean item) {
        ImageView avatar = helper.getView(R.id.avatar);
        Glide.with(mContext).load(item.getThumb()).into(avatar);

        helper.setText(R.id.author, item.getAuthor())
                .setText(R.id.title, item.getName())
                .setText(R.id.desc, item.getDesc())
                .setText(R.id.tag, item.getLicense());
    }
}
