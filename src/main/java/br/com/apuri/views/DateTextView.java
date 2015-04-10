/*
 * Copyright 2015 apuri Developers
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.apuri.lib.R;
import br.com.apuri.utils.ApuriJavaUtils;

public class DateTextView extends TextView {

    private Calendar calendar = Calendar.getInstance();

    private DateType type;

    private SimpleDateFormat formatter;

    private  static enum DateType{
        DATE, HOUR;
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DateTextView,
                0, 0);

        try {
            int typeInt = a.getInt(br.com.apuri.lib.R.styleable.DateTextView_dateType,0);
            if(typeInt == 0)
                type = DateType.DATE;
            else
                type = DateType.HOUR;

        } finally {
            a.recycle();
        }

        setupFormatter();

        setDate(calendar.getTime());

        setOnClickListener(new DateDialogOnClickListener());
    }

    private void setupFormatter() {
        switch (type){
            case HOUR:
                formatter = createHourFormatter();
                break;
            default:
                formatter = createDateFormatter();
                break;
        }
    }

    public void setDate(final Date date){
        calendar.setTime(date);
        if(type == DateType.DATE){
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }
        setText(formatter.format(calendar.getTime()));
    }

    public Date getDate(){
        return calendar.getTime();
    }


    private class DateDialogOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(final View view) {
            if(type == DateType.DATE)
                createDatePickDialog();
            else
                createTimePickDialog();
        }

        private void createTimePickDialog() {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minute);
                }
            };

            TimePickerDialog dialog = new TimePickerDialog(getContext(),
                    timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    DateTextView.this.setText(formatter.format(calendar.getTime()));
                }
            });
            dialog.show();
        }

        private void createDatePickDialog() {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(year,monthOfYear,dayOfMonth);
                }
            };

            DatePickerDialog dialog = new DatePickerDialog(DateTextView.this.getContext(), dateSetListener,
                    calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    DateTextView.this.setText(formatter.format(calendar.getTime()));
                }
            });
            dialog.show();
        }
    }

    private SimpleDateFormat createHourFormatter() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    private SimpleDateFormat createDateFormatter() {
        Calendar today = Calendar.getInstance();
        SimpleDateFormat formatter;
        if(this.calendar.get(Calendar.YEAR) != today.get(Calendar.YEAR))
            formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale.getDefault());
        else
            formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM",Locale.getDefault());
        return formatter;
    }

}
