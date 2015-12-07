package th.co.siamkubota.kubota.utils.function;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
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

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
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

    public static String DateToString(Date date, String stringFormat,Locale locale ){

        String dateString = null;
        SimpleDateFormat dateformat = new SimpleDateFormat(stringFormat, locale);
        dateString = dateformat.format(date);
        System.out.println("Current Date Time : " + dateString);

        return dateString;
    }

    public static String DateThai(Date date)
    {
        String Months[] = {
                "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.",
                "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.",
                "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."};

        //DateFormat df = new SimpleDateFormat(format);

        int year=0,month=0,day=0,hours=0,min=0;

        //Date date = df.parse(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        hours = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);


        return String.format("%s %s %s %02d.%02d น.", day,Months[month],String.valueOf(year+543).substring(2), hours, min);
    }

    public static Date dateTimeISO(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 7);
        return calendar.getTime();
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){

        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return  byteArray;
    }

}
