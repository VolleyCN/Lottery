package com.volleycn.lottery;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @Describe
 * @Date : 2020/7/3
 * @Email : zhangmeng@newstylegroup.com
 * @Author : MENG
 */
public class LotteryGiftEntity implements MultiItemEntity {
    private String name;
    private String value;
    private String pictureurl;
    private boolean focus;
    private int pictureRec;

    private int itemType = LotteryLayout.Lottery_ITEM_TYPE_NORMAL;

    public LotteryGiftEntity() {
    }

    public LotteryGiftEntity(String name,String value, int pictureRec) {
        this.pictureRec = pictureRec;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public int getPictureRec() {
        return pictureRec;
    }

    public void setPictureRec(int pictureRec) {
        this.pictureRec = pictureRec;
    }
}
