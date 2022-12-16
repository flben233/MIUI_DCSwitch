package org.shirakawatyu.dc;

import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.shirakawatyu.dc.entity.Status;
import org.shirakawatyu.dc.util.CommandUtil;
import org.shirakawatyu.dc.util.DisplayUtil;

import static org.shirakawatyu.dc.util.RootUtil.checkRoot;
import static org.shirakawatyu.dc.util.DisplayUtil.simpleSetAntiFlickerMode;

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
        if (!getSharedPreferences("dc", MODE_PRIVATE).contains("dcStatus")) {
            DisplayUtil.initDcStatus(getApplicationContext());
        }
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, true));
        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(view -> simpleSetAntiFlickerMode(getApplicationContext(), status, false));
    }
}