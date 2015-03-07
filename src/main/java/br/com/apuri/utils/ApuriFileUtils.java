package br.com.apuri.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

/**
 * Created by junior on 1/3/15.
 */
public class ApuriFileUtils {


    public static String getFileName(Context context, Uri uri){
        System.out.println("FILE NAME URI "+uri.toString());
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }
}
