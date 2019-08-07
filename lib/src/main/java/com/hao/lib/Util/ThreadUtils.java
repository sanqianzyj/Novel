package com.hao.lib.Util;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadUtils {
    static class ExecutorConfig {
        int num = 1;
        Executor executor;

        public ExecutorConfig(int num, Executor executor) {
            this.num = num;
            this.executor = executor;
        }
    }

    static HashMap<String, ExecutorConfig> map = new HashMap<>();
    static int num = 10;
    static ThreadUtils threadUtils;

    private ThreadUtils() {
    }

    private static class ThreadHelper {
        static final ThreadUtils threadUtils = new ThreadUtils();
    }

    public static ThreadUtils getInstance() {
        return ThreadHelper.threadUtils;
    }


    /**
     * 创建一个带有标示的线程池
     *
     * @param tag
     * @return
     */
    public ScheduledExecutorService createSch(String tag) {
        if (threadUtils == null) {
            threadUtils = ThreadHelper.threadUtils;
        }

        if (map.get(tag) == null || !(map.get(tag).executor instanceof ExecutorService) || (num != map.get(tag).num) && num != 0) {
            map.put(tag, new ExecutorConfig(num, Executors.newScheduledThreadPool(num)));
            threadUtils.num = 0;
        }
        return (ScheduledExecutorService) map.get(tag).executor;
    }

    public ExecutorService createFix(String tag) {
        if (threadUtils == null) {
            threadUtils = ThreadHelper.threadUtils;
        }
        if (map.get(tag) == null || !(map.get(tag) instanceof ExecutorService) || (num != map.get(tag).num) && num != 0) {
            map.put(tag, new ExecutorConfig(num, Executors.newFixedThreadPool(num)));
            threadUtils.num = 0;
        }
        return (ExecutorService) map.get(tag).executor;
    }


    //单线程化线程池
    public  ExecutorService createSingle(String tag) {
        if (threadUtils == null) {
            threadUtils = ThreadHelper.threadUtils;
        }
        if (map.get(tag) == null || !(map.get(tag) instanceof ScheduledExecutorService) || (num != map.get(tag).num) && num != 0) {
            map.put(tag, new ExecutorConfig(num, Executors.newSingleThreadExecutor()));
        }
        return (ExecutorService) map.get(tag).executor;
    }

}
