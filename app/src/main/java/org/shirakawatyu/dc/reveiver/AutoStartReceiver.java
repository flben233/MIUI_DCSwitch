package org.shirakawatyu.dc.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.shirakawatyu.dc.service.DisableDynamicFreshRateService;

import static android.content.Context.MODE_PRIVATE;

public class AutoStartReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            SharedPreferences dc = context.getSharedPreferences("dc", MODE_PRIVATE);
            if (dc.getBoolean("disableDynamicFreshRate", false)) {
                context.startForegroundService(new Intent(context, DisableDynamicFreshRateService.class));
            }
        }
    }
}
