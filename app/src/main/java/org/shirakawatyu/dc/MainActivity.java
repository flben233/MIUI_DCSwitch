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
import org.shirakawatyu.dc.service.DisableDynamicFreshRateService;
import org.shirakawatyu.dc.util.CommandUtil;
import org.shirakawatyu.dc.util.DisplayUtil;
import org.shirakawatyu.dc.util.SharedPreferencesUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.shirakawatyu.dc.util.RootUtil.checkRoot;
import static org.shirakawatyu.dc.util.DisplayUtil.simpleSetAntiFlickerMode;

public class MainActivity extends AppCompatActivity {

    private boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Status status = new Status();
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
                startForegroundService(new Intent(this, DisableDynamicFreshRateService.class));
                Toast.makeText(this, "启用常驻服务", Toast.LENGTH_SHORT).show();
            } else {
                stopService(new Intent(this, DisableDynamicFreshRateService.class));
            }
        });
        if (dcPreferences.getBoolean("disableDynamicFreshRate", false)) {
            startForegroundService(new Intent(this, DisableDynamicFreshRateService.class));
            sw.setChecked(true);
        }
    }

}