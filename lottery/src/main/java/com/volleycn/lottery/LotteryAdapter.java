package com.volleycn.lottery;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Describe
 * @Date : 2020/7/3
 * @Email : zhangmeng@newstylegroup.com
 * @Author : MENG
 */
public class LotteryAdapter extends BaseMultiItemQuickAdapter<LotteryGiftEntity, BaseViewHolder> {
    public LotteryAdapter(List<LotteryGiftEntity> data) {
        super(data);
        addItemType(LotteryLayout.Lottery_ITEM_TYPE_START, R.layout.square_layout_start_item);
        addItemType(LotteryLayout.Lottery_ITEM_TYPE_NORMAL, R.layout.square_layout_normal_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, LotteryGiftEntity item) {
        try {
            if (item.getItemType() == LotteryLayout.Lottery_ITEM_TYPE_NORMAL) {
                helper.setVisible(R.id.lottery_square_item_focus, item.isFocus());
                helper.setVisible(R.id.lottery_square_item_bg, !item.isFocus());
                helper.setText(R.id.lottery_square_item_gift_des, item.getName() + "x" + item.getValue());
                if (item.isFocus()) {
                    helper.getView(R.id.lottery_square_item_gift).setScaleX(1.2f);
                    helper.getView(R.id.lottery_square_item_gift).setScaleY(1.2f);
                } else {
                    helper.getView(R.id.lottery_square_item_gift).setScaleX(1.0f);
                    helper.getView(R.id.lottery_square_item_gift).setScaleY(1.0f);
                }
                helper.setImageResource(R.id.lottery_square_item_gift, item.getPictureRec());
            } else {
                helper.setImageResource(R.id.lottery_square_start, item.getPictureRec());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
