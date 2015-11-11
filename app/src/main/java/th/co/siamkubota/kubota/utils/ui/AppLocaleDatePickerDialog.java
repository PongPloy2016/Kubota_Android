package th.co.siamkubota.kubota.utils.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import th.co.siamkubota.kubota.R;

/**
 * Created by atthapok on 30/09/2558.
 */
public class AppLocaleDatePickerDialog extends DatePickerDialog implements OnDateChangedListener {

    private static final String TAG = AppLocaleDatePickerDialog.class.getSimpleName();

    private static final String MONTH_LONG_FORMAT = "MMMMMM, yyyy";
    private static final String MONTH_SHORT_FORMAT = "dd/MM/yyyy";
    private static SimpleDateFormat sMonthLongFormat;
    private static SimpleDateFormat sMonthFormat;

    //protected DatePicker mDatePicker;
    protected NumberDatePicker mDatePicker;
    protected Calendar mCalendar;

    private int mInitialYear;
    private int mInitialMonth;
    private int mInitialDay;

    public AppLocaleDatePickerDialog(Context context, int theme,
                                     OnDateSetListener callBack, int year, int monthOfYear,
                                     int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);

        pullDatePickerRefFromSuper();
        initPicker(context);


        mInitialYear = year;
        mInitialMonth = monthOfYear;
        mInitialDay = dayOfMonth;
        mCalendar = Calendar.getInstance();

        AttributeSet attrs = new AttributeSet() {
            @Override
            public int getAttributeCount() {
                return 0;
            }

            @Override
            public String getAttributeName(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(String namespace, String name) {
                return null;
            }

            @Override
            public String getPositionDescription() {
                return null;
            }

            @Override
            public int getAttributeNameResource(int index) {
                return 0;
            }

            @Override
            public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeListValue(int index, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(int index, float defaultValue) {
                return 0;
            }

            @Override
            public String getIdAttribute() {
                return null;
            }

            @Override
            public String getClassAttribute() {
                return null;
            }

            @Override
            public int getIdAttributeResourceValue(int defaultValue) {
                return 0;
            }

            @Override
            public int getStyleAttribute() {
                return 0;
            }
        };

        mDatePicker = new NumberDatePicker(context, null);

        mDatePicker.init(mInitialYear, mInitialMonth, mInitialDay, this);
        updateTitle(mInitialYear, mInitialMonth, mInitialDay);
    }

    public AppLocaleDatePickerDialog(Context context, OnDateSetListener callBack,
                                     int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    /**
     * Use reflection to use the Locale of the application for the month spinner.
     *
     * PS: DAMN DATEPICKER DOESN'T HONOR Locale.getDefault()
     * <a href="http://code.google.com/p/android/issues/detail?id=25107">Bug Report</a>
     * @param context
     */
    public void initPicker(Context context) {
        String monthPickerVarName;
        String months[] =  context.getResources().getStringArray(R.array.month_in_number);

        if (Build.VERSION.SDK_INT >= 14) {
            monthPickerVarName = "mMonthSpinner";
        } else {
            monthPickerVarName = "mMonthPicker";
        }

        try {
            Field f[] = mDatePicker.getClass().getDeclaredFields();

            for (Field field : f) {
                if (field.getName().equals("mShortMonths")) {
                    field.setAccessible(true);
                    field.set(mDatePicker, months);
                } else if (field.getName().equals(monthPickerVarName)) {
                    field.setAccessible(true);
                    Object o = field.get(mDatePicker);
                    if (Build.VERSION.SDK_INT >= 14) {
                        Method m = o.getClass().getDeclaredMethod("setDisplayedValues", String[].class);
                        m.setAccessible(true);
                        m.invoke(o, (Object)months);
                    } else {
                        Method m = o.getClass().getDeclaredMethod("setRange", int.class, int.class, String[].class);
                        m.setAccessible(true);
                        m.invoke(o, 1, 12, (Object)months);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        try {
            final Method updateSpinner = mDatePicker.getClass().getDeclaredMethod("updateSpinners");
            updateSpinner.setAccessible(true);
            updateSpinner.invoke(mDatePicker);
            updateSpinner.setAccessible(false);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * Create references of private variables named:
     * <i>mDatePicker</i> &amp; <i>mCalendar</i>
     * of the super class to this class.
     */
    private void pullDatePickerRefFromSuper() {
        Field superF[] = AppLocaleDatePickerDialog.class.getSuperclass().getDeclaredFields();
        for(Field fi : superF) {

            if(fi.getName().equals("mDatePicker")) {
                fi.setAccessible(true);
                try {
                    mDatePicker = (NumberDatePicker)fi.get(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else if (fi.getName().equals("mCalendar")) {
                fi.setAccessible(true);
                try {
                    mCalendar = (Calendar)fi.get(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) {
        view.init(year, month, day, this);
        updateTitle(year, month, day);
    }

    private void updateTitle(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        setTitle(getMonthFormat(this.getContext()).format(mCalendar.getTime()));
    }

    /**
     * @return the monthLongFormat
     */
    public static SimpleDateFormat getMonthLongFormat(Context context) {
        //SimpleDateFormat sMonthLongFormat = null;
        if (sMonthLongFormat != null) {
            return sMonthLongFormat;
        } else {
//            String prefLocale = PreferenceManager.getDefaultSharedPreferences(context
//                    .getApplicationContext()).getString(PreferenceKey.DISPLAY_LANGUAGE, "en");
//            String prefLocale = PreferenceManager.getDefaultSharedPreferences(context
//                    .getApplicationContext()).getString(PreferenceKey.DISPLAY_LANGUAGE, "en");
//            Locale displayLocale = new Locale(prefLocale);
            Locale displayLocale = new Locale("th");

            return sMonthLongFormat = new SimpleDateFormat(MONTH_LONG_FORMAT, displayLocale);
        }
    }

    public static SimpleDateFormat getMonthFormat(Context context) {

        if (sMonthFormat != null) {
            return sMonthLongFormat;
        } else {
//            String prefLocale = PreferenceManager.getDefaultSharedPreferences(context
//                    .getApplicationContext()).getString(PreferenceKey.DISPLAY_LANGUAGE, "en");
//            String prefLocale = PreferenceManager.getDefaultSharedPreferences(context
//                    .getApplicationContext()).getString(PreferenceKey.DISPLAY_LANGUAGE, "en");
//            Locale displayLocale = new Locale(prefLocale);
            Locale displayLocale = new Locale("th");

            return sMonthLongFormat = new SimpleDateFormat(MONTH_SHORT_FORMAT, Locale.forLanguageTag("th"));
        }
    }

}