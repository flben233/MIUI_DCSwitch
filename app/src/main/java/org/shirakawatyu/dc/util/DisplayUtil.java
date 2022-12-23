package org.shirakawatyu.dc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import org.shirakawatyu.dc.entity.Status;

import java.lang.reflect.Method;

public class DisplayUtil {
    public static boolean setAntiFlickMode(boolean status, boolean selinux) {
        try {
            Class<?> hardware = Class.forName("miui.hardware.display.DisplayFeatureManager");
            Method getInstance = hardware.getDeclaredMethod("getInstance");
            getInstance.setAccessible(true);
            Object o = getInstance.invoke(null);
            Class<?> aClass = o.getClass();
            Method setScreenEffect = aClass.getDeclaredMethod("setScreenEffect", Integer.TYPE, Integer.TYPE);
            if (status) {
                setScreenEffect.invoke(o, 20, 1);
                CommandUtil.exec("su -c settings put system dc_back_light 1");
            } else {
                setScreenEffect.invoke(o, 20, 0);
                CommandUtil.exec("su -c settings put system dc_back_light 0");
            }
            if (selinux) {
                SELinuxUtil.setStatus(true);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void simpleSetAntiFlickerMode(Context context, Status status, boolean mode) {
        simpleSetAntiFlickerMode(context, status, mode, true);
    }
    public static void simpleSetAntiFlickerMode(Context context, Status status, boolean mode, boolean showToast) {
        if (SELinuxUtil.checkStatus()) {
            SELinuxUtil.setStatus(false);
            status.setLastSELinuxStatus(true);
            simpleSetAntiFlickerMode(context, status, mode, showToast);
        } else {
            boolean b = setAntiFlickMode(mode, status.getLastSELinuxStatus());
            if (showToast && !b) Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
            else {
//                SharedPreferences.Editor edit = context.getSharedPreferences("dc", Context.MODE_PRIVATE).edit();
//                edit.putBoolean("dcStatus", mode);
//                edit.apply();
                SharedPreferencesUtil.putBoolean(context, "dc", "dcStatus", mode);
                if (showToast) Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void initDcStatus(Context context) {
        String s = CommandUtil.execString("su -c settings get system dc_back_light");
        SharedPreferencesUtil.putBoolean(context, "dc", "dcStatus", "1".equals(s));
//        SharedPreferences.Editor edit = context.getSharedPreferences("dc", Context.MODE_PRIVATE).edit();
//        edit.putBoolean("dcStatus", "1".equals(s));
//        edit.apply();
    }
}
