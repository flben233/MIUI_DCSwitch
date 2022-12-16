package org.shirakawatyu.dc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandUtil {
    public static void exec(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String execString(String cmd) {
        try {
            Process exec = Runtime.getRuntime().exec(cmd);
            BufferedInputStream inputStream = new BufferedInputStream(exec.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = reader.readLine().trim();
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
