package org.shirakawatyu.dc;

import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkRoot()) {
            Toast.makeText(getApplicationContext(),"Tips：您的设备没有ROOT，本程序不会有任何作用",Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> {
            boolean b = DisplayUtil.setAntiFlickMode(true);
            if (!b) Toast.makeText(getApplicationContext(), "开启失败", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "开启成功", Toast.LENGTH_SHORT).show();
        });
        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(view -> {
            boolean b = DisplayUtil.setAntiFlickMode(false);
            if (!b) Toast.makeText(getApplicationContext(), "关闭失败", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "关闭成功", Toast.LENGTH_SHORT).show();
        });
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
}