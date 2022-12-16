package org.shirakawatyu.dc.util;

public class SELinuxUtil {
    public static boolean checkStatus() {
        String result = CommandUtil.execString("su -c getenforce");
        return "Enforcing".equals(result);
    }

    public static void setStatus(boolean status) {
        if (status) {
            CommandUtil.exec("su -c setenforce 1");
        } else {
            CommandUtil.exec("su -c setenforce 0");
        }
    }
}
