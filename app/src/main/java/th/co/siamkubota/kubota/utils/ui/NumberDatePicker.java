package th.co.siamkubota.kubota.utils.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;

import th.co.siamkubota.kubota.R;

/**
 * Created by atthapok on 14/09/2558.
 */

class NumberDatePicker extends DatePicker {

    private static final String TAG = NumberDatePicker.class.getSimpleName();

    private Context mContext;


    public NumberDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        String[] s = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0)
            {
                NumberPicker daySpinner = (NumberPicker)this.findViewById(daySpinnerId);
                setDividerColor(daySpinner);
            }

            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (monthSpinnerId != 0)
            {
                NumberPicker monthSpinner = (NumberPicker)this.findViewById(monthSpinnerId);
                monthSpinner.setDisplayedValues(s);
                setDividerColor(monthSpinner);
            }

            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
            if (yearSpinnerId != 0)
            {
                NumberPicker yearSpinner = (NumberPicker)this.findViewById(yearSpinnerId);
                setDividerColor(yearSpinner);
            }
        } else {
            Field[] fields = DatePicker.class.getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);

                    if (TextUtils.equals(field.getName(), "mMonthSpinner")) {
                        NumberPicker monthPicker = (NumberPicker) field.get(this);
                        monthPicker.setDisplayedValues(s);
                        setDividerColor(monthPicker);
                    }

                    if (TextUtils.equals(field.getName(), "mDaySpinner")) {
                        NumberPicker dayPicker = (NumberPicker) field.get(this);
                        setDividerColor(dayPicker);
                    }

                    if (TextUtils.equals(field.getName(), "mYearSpinner")) {
                        NumberPicker yearPicker = (NumberPicker) field.get(this);
                        setDividerColor(yearPicker);
                    }

                    if (TextUtils.equals(field.getName(), "mShortMonths")) {
                        field.set(this, s);
                    }

                    if (TextUtils.equals(field.getName(), "mDateFormat")) {

                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        field.set(this, sdf);
                    }

                    if (TextUtils.equals(field.getName(), "FORMAT_DDMMYYYY")) {

                        field.set(this, true);
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, ContextCompat.getDrawable(mContext, R.drawable.vertical_divider_orange));
                } catch (IllegalArgumentException e) {
                    Log.v(TAG, "Illegal Argument Exception");
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    Log.v(TAG, "Resources NotFound");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.v(TAG, "Illegal Access Exception");
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}