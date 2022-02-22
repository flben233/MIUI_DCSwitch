package org.shirakawatyu.dc;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Tips：如果您的设备没有ROOT本程序不会有任何作用",Toast.LENGTH_LONG).show();
        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Runtime.getRuntime().exec("su -c settings put system user_refresh_rate 60");
                    Intent intent = new Intent();
                    intent.setClassName("com.xiaomi.misettings","com.xiaomi.misettings.display.AntiFlickerMode.AntiFlickerActivity");
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button bt2 = (Button) findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    Display display = getDisplay();
                    a = (int)display.getRefreshRate();
                    try {
                        Runtime.getRuntime().exec("su -c settings put system user_refresh_rate " + Integer.toString(a));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Display display = getWindowManager().getDefaultDisplay();
                    a = (int)display.getRefreshRate();
                    try {
                        Runtime.getRuntime().exec("su -c settings put system user_refresh_rate " + Integer.toString(a));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}