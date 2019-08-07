package com.hao.lib.base.Rx;

import com.hao.lib.base.MI2App;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Rx {

    private Rx() {
    }

    public static Rx getInstance() {
        return RxHelp.rx;
    }

    private static class RxHelp {
        static final Rx rx = new Rx();
    }


    BlockingQueue<RxMessage> queue = new LinkedBlockingQueue<>(10000);

    public void sendMessage(final Object tag, final Object o) {
        for (final RxMessage rx : queue) {
            try {
                if (MI2App.getInstance().getNowActivitie() != null) {
                    MI2App.getInstance().getNowActivitie().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rx.rxDo(tag, o);
                        }
                    });
                } else {
                    rx.rxDo(tag, o);
                }
            } catch (Exception e) {
            }
        }
    }


    public void addRxMessage(RxMessage rxMessage) {
        queue.offer(rxMessage);
    }

    public void remove(RxMessage rxMessage) {
        queue.remove(rxMessage);
    }

    public void removeAll() {
        queue.clear();
    }

}
