package org.shirakawatyu.dc;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.shirakawatyu.dc.entity.Status;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Status status = new Status();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkRoot()) {
            Toast.makeText(getApplicationContext(),"Tips：您的设备没有ROOT，本程序不会有任何作用",Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, true));
        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, false));
    }

    private static boolean checkRoot() {
        Process getStatus = null;
        try {
            getStatus = Runtime.getRuntime().exec("su -c getenforce");
            BufferedReader reader = new BufferedReader(new InputStreamReader( new BufferedInputStream(getStatus.getInputStream())));
            String result = reader.readLine().trim();
            if (result.equals("Permission denied")) {
                return false;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void simpleSetAntiFlickerMode(Context context, Status status, boolean mode) {
        if (SELinuxUtil.checkStatus()) {
            SELinuxUtil.setStatus(false);
            status.setLastSELinuxStatus(true);
            simpleSetAntiFlickerMode(context, status, mode);
        } else {
            boolean b = DisplayUtil.setAntiFlickMode(mode, status.getLastSELinuxStatus());
            if (!b) Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
            else Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
        }
    }
}