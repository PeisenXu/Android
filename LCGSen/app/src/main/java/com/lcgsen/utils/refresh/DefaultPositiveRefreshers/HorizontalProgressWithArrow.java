package com.lcgsen.utils.refresh.DefaultPositiveRefreshers;

public class HorizontalProgressWithArrow extends OverlayProgressWithArrow {

    public HorizontalProgressWithArrow() {
        super(30);
    }

    @Override
    protected int getSize() {
        if (width == 0 && null != circleImageView) {
            width = circleImageView.getWidth();
        }
        return width;
    }
}
