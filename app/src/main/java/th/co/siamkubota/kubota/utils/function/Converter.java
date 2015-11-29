package th.co.siamkubota.kubota.utils.function;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by atthapok on 25/09/2558.
 */
public class Converter {

    public static int dpTopx(Context context, int dp){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    public static float pxTosp(Context context, int px){
        Resources r = context.getResources();
        return px / r.getDisplayMetrics().scaledDensity;
    }

    public static String CalendarToString(Calendar calendar, String format){
        //String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static Calendar StringToCalendar(String stringDate , String format){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            calendar.setTime(sdf.parse(stringDate));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static Date StringToDate(String stringDate , String stringFormat){

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(stringFormat, Locale.US);
        try {
            date = format.parse(stringDate);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public static String DateToString(Date date, String stringFormat){

        String dateString = null;
        SimpleDateFormat dateformat = new SimpleDateFormat(stringFormat);
        dateString = dateformat.format(date);
        System.out.println("Current Date Time : " + dateString);

        return dateString;
    }
}
