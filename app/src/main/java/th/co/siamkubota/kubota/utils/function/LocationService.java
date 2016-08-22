package th.co.siamkubota.kubota.utils.function;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by atthapok on 04/10/2558.
 */
public class LocationService {
    public static boolean LocationInRange(Location target ,Location current, float radius){

 /*       float[] distances = new float[1];
        Location.distanceBetween(target.getLatitude(), target.getLongitude(),
                current.getLatitude(), current.getLongitude(), distances);

        if(distances[0] > radius){
            return false;
        }else{
            return true;
        }
*/

        float distance = current.distanceTo(target);
        Log.d("DISTANCE ### ", String.valueOf(distance));

        if (distance > radius){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        return gps_enabled;
    }



    public static boolean canToggleGPS(Context context) {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if(pacInfo != null){
            for(ActivityInfo actInfo : pacInfo.receivers){
                //test if recevier is exported. if so, we can toggle GPS.
                if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
                    return true;
                }
            }
        }

        return false; //default
    }


    public static void turnGPSOn(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public static void turnGPSOff(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public static void enableGPS(Context context){
        Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);
    }

    public static void disableGPS(Context context){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        context.sendBroadcast(intent);
    }





}
