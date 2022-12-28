package org.shirakawatyu.dc.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import org.shirakawatyu.dc.R;
import org.shirakawatyu.dc.util.CommandUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisableDynamicFreshRateService extends Service {
    private final String CHANNEL_ID = "org.shirakawatyu.dc";
    private final int NOTIFICATION_ID = 233;
    private boolean register = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!register) initBroadcastReceiver();
        startForeground(NOTIFICATION_ID, initNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();

    }

    private Notification initNotification() {
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
        return builder.build();
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
}
