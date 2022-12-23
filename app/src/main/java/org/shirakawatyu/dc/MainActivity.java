package org.shirakawatyu.dc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.*;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;
import org.shirakawatyu.dc.entity.Status;
import org.shirakawatyu.dc.util.CommandUtil;
import org.shirakawatyu.dc.util.DisplayUtil;
import org.shirakawatyu.dc.util.SharedPreferencesUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.shirakawatyu.dc.util.RootUtil.checkRoot;
import static org.shirakawatyu.dc.util.DisplayUtil.simpleSetAntiFlickerMode;

public class MainActivity extends AppCompatActivity {
    private final String CHANNEL_ID = "org.shirakawatyu.dc";
    private final int NOTIFICATION_ID = 233;
    private boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Status status = new Status();
        autoObtainLocationPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences dcPreferences = getSharedPreferences("dc", MODE_PRIVATE);
        if (!checkRoot()) {
            Toast.makeText(getApplicationContext(),"Tips：您的设备没有ROOT，本程序不会有任何作用", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        if (!dcPreferences.contains("dcStatus")) {
            DisplayUtil.initDcStatus(getApplicationContext());
        }

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, true));

        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, false));

        SwitchMaterial sw = findViewById(R.id.switch1);
        sw.setOnClickListener((view) -> {
            SharedPreferencesUtil.putBoolean(this, "dc", "disableDynamicFreshRate", sw.isChecked());
            if (sw.isChecked()) {
                initNotification();
                initBroadcastReceiver();
                Toast.makeText(this, "启用常驻服务", Toast.LENGTH_SHORT).show();
            } else {
                NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            }
        });
        if (dcPreferences.getBoolean("disableDynamicFreshRate", false)) {
            initNotification();
            if (!register) initBroadcastReceiver();
            sw.setChecked(true);
        }
    }

    private void initNotification() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "dc", NotificationManager.IMPORTANCE_LOW);
        channel.setDescription("DC Switch Service");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_line)
                .setContentTitle("DC开关")
                .setContentText("DC开关常驻服务")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setNotificationSilent()
                .setOngoing(true);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void initBroadcastReceiver() {
        if (register) return;
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int mode = 0;
                String speedMode = CommandUtil.execString("su -c settings get system speed_mode");
                if (speedMode != null){
                    mode = Integer.parseInt(speedMode);
                }
                Logger.getLogger("Broadcast").log(Level.INFO, "屏幕开启");
                CommandUtil.exec("su -c settings put system speed_mode " + (1 - mode));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CommandUtil.exec("su -c settings put system speed_mode " + mode);
            }
        };
        registerReceiver(receiver, filter);
        register = true;
    }

    private void autoObtainLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_SETTINGS") != 0) {
            ActivityCompat.requestPermissions(this, new String[] { "android.permission.WRITE_SETTINGS" }, 1);
        }
    }
}