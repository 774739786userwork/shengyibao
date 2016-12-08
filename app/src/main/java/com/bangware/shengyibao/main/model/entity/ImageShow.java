package com.bangware.shengyibao.main.model.entity;

/**
 * 首页顶部图片展示
 * Created by bangware on 2016/10/24.
 */
public class ImageShow {
    private int iconResId;
    private String intro;

    public ImageShow() {
    }

    public ImageShow(int iconResId, String intro) {
        this.iconResId = iconResId;
        this.intro = intro;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getIntro() {
        return intro;
    }
}
