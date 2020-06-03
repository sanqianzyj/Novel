package com.hao.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    TextView show_message;
    EditText connect_url;
    EditText topic_name;
    EditText account;
    EditText password;
    EditText address;
    Button connect;
    GetPushService getPushService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        startBackRunService();
    }

    private void initView() {
        show_message = findViewById(R.id.show_message);
        connect_url = findViewById(R.id.connect_url);
        topic_name = findViewById(R.id.topic_name);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);

        connect_url.setText(GetPushService.serverUri);
        topic_name .setText(GetPushService.subscriptionTopic);
        account .setText(GetPushService.userName);
        password.setText(GetPushService.passWord);
        address.setText(GetPushService.clientId);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_message.setText("");
                if (getPushService != null) {
                    try {
                        getPushService.unConnect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void startBackRunService() {
        Intent bindIntent = new Intent(this, GetPushService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            getPushService = ((GetPushService.Binder) service).getService();
            getPushService.setBackCall(new BackCall() {
                @Override
                public void resavemessage(final String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show_message.setText(s);
                        }
                    });
                }
            });
        }
    };

    public void stopService() {
        unbindService(connection);
    }
}
