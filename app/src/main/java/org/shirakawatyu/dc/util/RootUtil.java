package org.shirakawatyu.dc.util;

public class RootUtil {
    public static boolean checkRoot() {
        String result = CommandUtil.execString("su -c getenforce");
        return !"Permission denied".equals(result);
    }
}
