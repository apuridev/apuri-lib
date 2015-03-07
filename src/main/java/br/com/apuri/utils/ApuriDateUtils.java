package br.com.apuri.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

public class ApuriDateUtils {
	
	
	
	public static Date utcToLocal(Date utcDate){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		localCalendar.setTimeInMillis(utcDate.getTime());
		Date localDate = localCalendar.getTime();
		return localDate;
	}

	private static DateFormat formatterStringDateWithHour = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT,Locale.getDefault()); 

	
	public static CharSequence stringDateWithHour(Context context,Date scheduledDate) {				
		return formatterStringDateWithHour.format(scheduledDate);
	}
	
}
