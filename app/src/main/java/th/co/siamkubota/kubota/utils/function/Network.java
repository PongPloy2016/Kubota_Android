package th.co.siamkubota.kubota.utils.function;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by atthapok on 05/10/2558.
 */
public class Network {

    public static boolean isNetworkEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = false;

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return network_enabled;
    }
}
