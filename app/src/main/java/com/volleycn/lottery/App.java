package com.volleycn.lottery;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @Describe
 * @Date : 2020/7/6
 * @Email : zhangmeng@newstylegroup.com
 * @Author : MENG
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        PrettyFormatStrategy lottery = PrettyFormatStrategy.newBuilder()
                .methodCount(5)
                .tag("Lottery")
                .showThreadInfo(true)
                .methodOffset(5)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(lottery));
    }
}
