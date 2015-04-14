/*
 * Copyright  2015 apuri Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    public static Date getDateAtZeroAM(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

	public static int differenceInDays(Date a, Date b){
		Calendar greater = Calendar.getInstance(Locale.getDefault());
		Calendar lower = Calendar.getInstance(Locale.getDefault());
		if(a.getTime() - b.getTime() < 0){
			greater.setTime(b);
			lower.setTime(a);
		}else{
			greater.setTime(a);
			lower.setTime(b);
		}
		return greater.get(Calendar.DAY_OF_YEAR) - lower.get(Calendar.DAY_OF_YEAR);
	}

}
