package br.com.apuri.utils;

/**
 * Created by junior on 1/14/15.
 */
public class ApuriJavaUtils {

    public static boolean isEmptyString(String string){
        return string == null || string.length() == 0;
    }

    public static String trimmedString(String string){
        return string == null ? null : string.trim();
    }
}
