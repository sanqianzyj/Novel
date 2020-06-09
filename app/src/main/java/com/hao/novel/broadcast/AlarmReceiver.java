package com.hao.novel.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent in = new Intent();
//        in.setClass(context, StartTiming.class);
//        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(in);

        Log.i("广播", intent.getAction());
    }
}
