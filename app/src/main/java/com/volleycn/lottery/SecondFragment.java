package com.volleycn.lottery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    LotteryLayout lotteryLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lotteryLayout.isRunning()){
                    lotteryLayout.startGame();
                }else {
                    lotteryLayout.tryToStop(5);
                }
            }
        });
        lotteryLayout = view.findViewById(R.id.lottery);
        initData();
    }

    private void initData() {
        List<LotteryGiftEntity> giftEntities = new ArrayList<>();
        giftEntities.add(new LotteryGiftEntity("秀秀", "1", R.mipmap.chengbao));
        giftEntities.add(new LotteryGiftEntity("秀秀", "2", R.mipmap.yifeichongtian));
        giftEntities.add(new LotteryGiftEntity("秀秀", "3", R.mipmap.chengbao));
        giftEntities.add(new LotteryGiftEntity("秀秀", "4", R.mipmap.yifeichongtian));
        giftEntities.add(new LotteryGiftEntity("秀秀", "5", R.mipmap.chengbao));
        giftEntities.add(new LotteryGiftEntity("秀秀", "6", R.mipmap.mil));
        giftEntities.add(new LotteryGiftEntity("秀秀", "7", R.mipmap.chengbao));
        giftEntities.add(new LotteryGiftEntity("秀秀", "8", R.mipmap.yifeichongtian));

//        giftEntities.add(new LotteryGiftEntity("秀秀", "5", R.mipmap.chengbao));
//        giftEntities.add(new LotteryGiftEntity("秀秀", "6", R.mipmap.mil));
//        giftEntities.add(new LotteryGiftEntity("秀秀", "7", R.mipmap.chengbao));
//        giftEntities.add(new LotteryGiftEntity("秀秀", "8", R.mipmap.yifeichongtian));

        int screenWidth = ScreenUtils.getScreenWidth();
//        int screenWidth = SizeUtils.dp2px(300);
        ViewGroup.LayoutParams params = lotteryLayout.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;
        lotteryLayout.setSquare(false);
        lotteryLayout.setData(giftEntities);
    }
}