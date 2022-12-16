package org.shirakawatyu.dc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SELinuxUtil {
    public static boolean checkStatus() {
        Process getEnforce = null;
        try {
            getEnforce = Runtime.getRuntime().exec("su -c getenforce");
            BufferedInputStream inputStream = new BufferedInputStream(getEnforce.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = reader.readLine().trim();
            if (result.equals("Enforcing")) {
                return true;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setStatus(boolean status) {
        try {
            if (status) {
                Runtime.getRuntime().exec("su -c setenforce 0");
            } else {
                Runtime.getRuntime().exec("su -c setenforce 1");
            }
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
