package com.hao.mqtt;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.Nullable;

import org.eclipse.paho.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by a'su's on 2018/5/15.
 */

public class GetPushService extends Service {
    private MqttAndroidClient mqttAndroidClient;

//    final String serverUri = "tcp://112.74.102.125:1883";

    //    final String serverUri = "tcp://118.126.100.235:1883";
//    final String serverUri = "tcp://101.200.77.166:1883";
//    final String subscriptionTopic = "ActiveMQ.Advisory.Consumer.Topic.TEST-MN-T-NEW_PLACEORDER_QUEUE";
    public static String serverUri = "tcp://139.199.158.253:1883";
    //    final String serverUri = "tcp://134.175.56.14:1883";
//    final String serverUri = "tcp://112.74.102.125:1883";
//    final String subscriptionTopic = "wydTopic";
    public static String subscriptionTopic = "by_print";
    public static String clientId = "123456771";

    public static String MESSAGE = "message";
    public static String userName = "admin";
    //    private String passWord = "password";
    public static String passWord = "admin";


    private MqttConnectOptions options;

    private static final String TAG = "GetPushService";
    private ScheduledExecutorService scheduler;
    private MqttClient client;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                Toast.makeText(App.getInstance(), msg.obj.toString(),
                        Toast.LENGTH_SHORT).show();
                System.out.println("-----------------------------");
            } else if (msg.what == 2) {
                System.out.println("------------连接成功-----------------");
                try {
                    client.subscribe(subscriptionTopic, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (backcall != null) {
                    backcall.resavemessage("------------连接成功-----------------");
                }
            } else if (msg.what == 3) {
                if (backcall != null) {
                    backcall.resavemessage("------------连接失败，系统正在重连-----------------");
                }
                System.out.println("------------连接失败，系统正在重连-----------------");
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public GetPushService getService() {
            return GetPushService.this;
        }
    }

    BackCall backcall;

    public void setBackCall(BackCall backcall) {
        this.backcall = backcall;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // daoSimple = new DaoSimple(App.getInstance());

        System.out.println("设备id---------");
        init();
        startReconnect();
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    init();
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(serverUri, clientId,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("连接      connectionLost----------");
                    if (backcall != null) {
                        backcall.resavemessage("connectionLost");
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("连接    deliveryComplete---------"
                            + token.isComplete());

                    if (backcall != null) {
                        backcall.resavemessage("deliveryComplete");
                    }
                }

                int i = 0;

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    String result = new String(message.getPayload());
                    Log.e(TAG, "连接   messageArrived:--> result" + result);
                    System.out.println("messageArrived----------");
                    if (backcall != null) {
                        backcall.resavemessage(result);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    public void  unConnect() throws MqttException {
        client.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        GetPushService.this.stopSelf();
    }


}
