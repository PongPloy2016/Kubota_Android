package th.co.siamkubota.kubota.activity;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.service.Constants;
import th.co.siamkubota.kubota.service.GeocodeAddressIntentService;
import th.co.siamkubota.kubota.utils.function.LocationService;
import th.co.siamkubota.kubota.utils.function.Network;

public class MapsDragableActivity extends BaseActivity
        implements OnMapClickListener,
        OnMapLongClickListener,
        OnMarkerDragListener ,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String KEY_ADDRESS = "ADDRESS";

    final int RQS_GooglePlayServices = 1;
    private GoogleMap myMap;

    Location myLocation;
    TextView tvLocInfo;

    boolean markerClicked;
    PolygonOptions polygonOptions;
    Polygon polygon;

    private Marker marker;


    //protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private boolean mAddressRequested;

    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private boolean waitGPSSetting;
    private android.support.v7.app.AlertDialog alert;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private boolean mWaitLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //tvLocInfo = (TextView)findViewById(R.id.locinfo);
        SupportMapFragment myMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        myMapFragment.getMapAsync(this);

        mResultReceiver = new AddressResultReceiver(null);

        if (!EnableGPSIfPossible()) {
            getCurrentLocation();
        }

        markerClicked = false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);

        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        myMap.setOnMapClickListener(this);
        myMap.setOnMapLongClickListener(this);
        myMap.setOnMarkerDragListener(this);

    }


    @Override
    protected void onResume() {

        super.onResume();

      /*  if (checkPlayServices()){
            Toast.makeText(getApplicationContext(),
                    "isGooglePlayServicesAvailable SUCCESS",
                    Toast.LENGTH_LONG).show();
        }*/

        if (checkLocationServiceEnable(MapsDragableActivity.this)) {
            getCurrentLocation();
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onMapClick(LatLng point) {
        //tvLocInfo.setText(point.toString());
       // myMap.animateCamera(CameraUpdateFactory.newLatLng(point));


        //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
        // Zoom in the Google Map
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

        markerClicked = false;
    }

    @Override
    public void onMapLongClick(LatLng point) {

        pinPoint(point);

    }

    private void pinPoint(LatLng point){

        //tvLocInfo.setText("New marker added@" + point.toString());

        if(myMap != null){
            if(marker != null) {
                marker.remove();
            }

            markerClicked = false;

            myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            myMap.setOnInfoWindowClickListener(this);

            // create marker
            MarkerOptions markerOptions = new MarkerOptions().position(point).draggable(true).title("Hello Maps");
            //marker = new MarkerOptions().position(point).draggable(true).title("Hello Maps");

            // Changing marker icon
            // set yours icon here
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_pin_point));

            marker = myMap.addMarker(markerOptions);
            marker.showInfoWindow();

            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
            // Zoom in the Google Map
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //myMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " Drag@" + marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " DragEnd");
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " DragStart");

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onInfoWindowClick(Marker marker) {
        //Toast.makeText(MapsDragableActivity.this, marker.getPosition().toString(), Toast.LENGTH_LONG).show();
        mAddressRequested = true;
        startIntentService(marker.getPosition());
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_info_window,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (MapsDragableActivity.this.marker != null
                    && MapsDragableActivity.this.marker.isInfoWindowShown()) {
                MapsDragableActivity.this.marker.hideInfoWindow();
                MapsDragableActivity.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            MapsDragableActivity.this.marker = marker;

          /*  buttonSelect = ((Button) view.findViewById(R.id.buttonSelect));
            buttonSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapsDragableActivity.this, marker.getPosition().toString(), Toast.LENGTH_LONG).show();
                }
            });*/

            return view;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locLsnr = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EnableGPSIfPossible()) {
            /*if (mLastLocation != null) {
                startIntentService();
            } else {
                mAddressRequested = true;
                getCurrentLocation();
            }*/
            //mAddressRequested = true;
            getCurrentLocation();
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(MapsDragableActivity.this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(MapsDragableActivity.this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }


    ////////////////////////////////////////////////////////////// start intent service

    protected void startIntentService(LatLng point) {

        Intent intent = new Intent(MapsDragableActivity.this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);


        if (fetchType == Constants.USE_ADDRESS_NAME) {
            String addressText = "";
            if (addressText.length() == 0) {
                Toast.makeText(MapsDragableActivity.this, "Please enter an address name", Toast.LENGTH_LONG).show();
                return;
            }
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, addressText);
        } else {

            if(point == null){
                if (mLastLocation == null) {
                    return;
                }

                intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                        mLastLocation.getLatitude());
                intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                        mLastLocation.getLongitude());
            }else{
                intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                        point.latitude);
                intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                        point.longitude);
            }

        }

        startService(intent);
    }


    ////////////////////////////////////////////////////////////// get address

    private void getCurrentLocation() {

        mWaitLocation = true;

        if (checkPlayServices()) {

            buildGoogleApiClient();
            createLocationRequest();

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        } else {
            locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locMgr.getBestProvider(criteria, true);
            locMgr.requestLocationUpdates(provider, 0, 0, locLsnr);
//            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    0, 0, locLsnr);
        }

    }

    private LocationManager locMgr;
    private LocationListener locLsnr = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            double myLat = location.getLatitude();
//            double myLon = location.getLongitude();

            //if(location.getAccuracy() < 100.0 && location.getSpeed() < 6.95){
            /*double suitableMeter = 20.0; // adjust your need
            if(location.getAccuracy() < suitableMeter ){

                mLastLocation = location;

                if(mLastLocation != null && mMap != null){
                    //LatLng sydney = new LatLng(-34, 151);
                    LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }

                if (mLastLocation != null && mAddressRequested) {

                    mAddressRequested = false;
                    // Determine whether a Geocoder is available.
                    if (!Geocoder.isPresent()) {
                    *//*Toast.makeText(getActivity(), R.string.no_geocoder_available,
                            Toast.LENGTH_LONG).show();*//*
                        return;
                    }
                    startIntentService();
                }
            }
            else{
                //Continue listening for a more accurate location
            }*/

            //updateMarker(location);

            double suitableMeter = 20.0; // adjust your need
            if(location.getAccuracy() < suitableMeter ){
                mWaitLocation = false;
                mLastLocation = location;
                pinPoint(new LatLng(location.getLatitude(), location.getLongitude()));
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    //Toast.makeText(CheckinActivity.this, "LocationProvider.AVAILABLE", Toast.LENGTH_LONG);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    //Toast.makeText(CheckinActivity.this, "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_LONG);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    //Toast.makeText(CheckinActivity.this, "LocationProvider.TEMPORARILY_UNAVAILABLE", Toast.LENGTH_LONG);
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public void onConnected(Bundle connectionHint) {

        displayLocation();

    }

    private void displayLocation() {

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation != null){
            pinPoint(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }


    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        //updateMarker(location);
        double suitableMeter = 20.0; // adjust your need
        if(location.getAccuracy() < suitableMeter ){
            mWaitLocation = false;
            mLastLocation = location;
            pinPoint(new LatLng(location.getLatitude(), location.getLongitude()));
        }


    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));*/
                        mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                        //((ServiceFragment) requestAddressFragment).setResultAddress(mAddressOutput);
                        //editTextServiceAddress.setText(mAddressOutput);
                        mAddressRequested = false;

                        //Toast.makeText(MapsDragableActivity.this, mAddressOutput, Toast.LENGTH_LONG).show();

                        finishWithResult(mAddressOutput);
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));*/
                        Log.d("ADDRESS", "Address not found");

                        showToastError(getString(R.string.map_address_not_found));

                        //Toast.makeText(MapsDragableActivity.this, "Address not found", Toast.LENGTH_LONG).show();
                    }
                });


            }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsDragableActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    protected void stopLocationUpdates() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

        if(locMgr != null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locMgr.removeUpdates(locLsnr);
        }


    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MapsDragableActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    private void updateMarker(Location location) {

        mLastLocation = location;

        pinPoint(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));


        if (mLastLocation != null && mAddressRequested) {

            mAddressRequested = false;
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                    /*Toast.makeText(getActivity(), R.string.no_geocoder_available,
                            Toast.LENGTH_LONG).show();*/
                return;
            }
            startIntentService(null);
        }

    }


    //////////////////////////////////////////////////////////////////// enable GPS

    public interface ICommand {
        void execute();
    }

    public class CancelCommand implements ICommand
    {
        protected Activity m_activity;

        public CancelCommand(Activity activity)
        {
            m_activity = activity;
        }

        public void execute()
        {
            alert.dismiss();
            //start asyncronous operation here
        }
    }

    public static class CommandWrapper implements DialogInterface.OnClickListener {
        private ICommand command;
        public CommandWrapper(ICommand command) {
            this.command = command;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            command.execute();
        }
    }

    private boolean checkLocationServiceEnable(Context mContext){
        boolean gps_enabled = LocationService.isGPSEnabled(mContext);
        boolean network_enabled = Network.isNetworkEnabled(mContext);

        return (gps_enabled && network_enabled);

    }

    public class EnableGpsCommand extends CancelCommand
    {
        public EnableGpsCommand( Activity activity) {
            super(activity);
        }

        public void execute()
        {
            // take the user to the phone gps settings and then start the asyncronous logic.
            m_activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            waitGPSSetting = true;
            super.execute();
        }
    }



    private boolean EnableGPSIfPossible()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            //mAddressRequested = true;
            return true;
        }
        return false;
    }

    private  void buildAlertMessageNoGps()
    {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapsDragableActivity.this);
        builder.setMessage(getString(R.string.service_setting_gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.service_button_ok), new CommandWrapper(new EnableGpsCommand(MapsDragableActivity.this)))
                .setNegativeButton(getString(R.string.service_button_cancel), new CommandWrapper(new CancelCommand(MapsDragableActivity.this)));

        alert = builder.create();
        alert.show();
    }

    private void finishWithResult(String result){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ADDRESS, result);

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showToastError(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_data_complete_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(MapsDragableActivity.this);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);

        TextView  textView = (TextView) layout.findViewById(R.id.textView1);
        textView.setText(message);

        toast.setView(layout);
        toast.show();
    }

}

// http://androidfreakers.blogspot.com/2013/08/display-custom-info-window-with.html