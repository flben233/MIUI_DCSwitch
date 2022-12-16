package org.shirakawatyu.dc;

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
                Runtime.getRuntime().exec("su -c settings put system dc_back_light 1");
            } else {
                setScreenEffect.invoke(o, 20, 0);
                Runtime.getRuntime().exec("su -c settings put system dc_back_light 0");
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
}
