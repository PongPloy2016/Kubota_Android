package th.co.siamkubota.kubota.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.util.Locale;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;
import th.co.siamkubota.kubota.service.Constants;
import th.co.siamkubota.kubota.service.FetchAddressIntentService;
import th.co.siamkubota.kubota.service.GeocodeAddressIntentService;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.LocationService;
import th.co.siamkubota.kubota.utils.function.Network;
import th.co.siamkubota.kubota.utils.function.Ui;
import th.co.siamkubota.kubota.utils.function.Validate;
import th.co.siamkubota.kubota.utils.ui.CustomSpinner;
import th.co.siamkubota.kubota.utils.ui.CustomSpinnerDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step1CustomerDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step1CustomerDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1CustomerDetailFragment extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String TAG = Step1CustomerDetailFragment.class.getSimpleName();

    private static final String ARG_PARAM_TITLE = "title";

    private LinearLayout rootLayout;
    private TextView textStepTitle;
    private CustomSpinner spinnerJobType;
    private CustomSpinner spinnerProduct;
    private CustomSpinner spinnerModel;
    private LinearLayout layoutOtherModel;

    private String[] jobTypeDataList;
    private String[] productDataList;
    private String[] modelDataList;
    private CustomSpinnerAdapter jobTypeSpinnerAdapter;
    private CustomSpinnerAdapter productSpinnerAdapter;
    private CustomSpinnerAdapter modelSpinnerAdapter;
    private SelectNoneSpinnerAdapter selectNoneJobTypeSpinnerAdapter;
    private SelectNoneSpinnerAdapter selectNoneProductSpinnerAdapter;
    private SelectNoneSpinnerAdapter selectNoneModelSpinnerAdapter;

    private EditText editTextOtherModel;
    private EditText editTextJobId;
    private EditText editTextName;
    private EditText editTextTel1;
    private EditText editTextTel2;
    private EditText editTextMachineNumber;
    private EditText editTextEngineNumber;
    private EditText editTextWorkHours;
    private EditText editTextAddress;

    private ImageButton locationButton;

    private RadioGroup radioGroupUserType;

    //protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private boolean mAddressRequested;
    //private GoogleApiClient mGoogleApiClient;

    //protected Location mCurrentLocation;


    //private AddressResultReceiver mResultReceiver;
    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    private String latitudeText = "13.968278";
    private String longitudeText = "100.633676";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    private String title;
    private boolean dataComplete = false;

    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public boolean isDataComplete() {
        return dataComplete;
    }

    public void setDataComplete(boolean dataComplete) {
        this.dataComplete = dataComplete;
    }

    //////////////////////////////////////////////////////////////////// constructor

    public static Step1CustomerDetailFragment newInstance(String title) {
        Step1CustomerDetailFragment fragment = new Step1CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public Step1CustomerDetailFragment() {
        // Required empty public constructor
    }


    //////////////////////////////////////////////////////////////////// onCreate

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM_TITLE);
        }

        jobTypeDataList = getResources().getStringArray(R.array.job_type);
        productDataList = getResources().getStringArray(R.array.product);
        modelDataList = new String[0];


        jobTypeSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), jobTypeDataList);
        productSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), productDataList);
        modelSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), modelDataList);


        selectNoneJobTypeSpinnerAdapter = new SelectNoneSpinnerAdapter(
                jobTypeSpinnerAdapter,
                R.layout.item_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                getActivity(), getString(R.string.service_hint_job_type));

        selectNoneProductSpinnerAdapter = new SelectNoneSpinnerAdapter(
                productSpinnerAdapter,
                R.layout.item_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                getActivity(), getString(R.string.service_hint_product));

        selectNoneModelSpinnerAdapter = new SelectNoneSpinnerAdapter(
                modelSpinnerAdapter,
                R.layout.item_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                getActivity(), getString(R.string.service_hint_model));

        mResultReceiver = new AddressResultReceiver(null);


        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_step1_customer_detail, container, false);


        return v;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
        spinnerJobType = (CustomSpinner) v.findViewById(R.id.spinnerJobType);
        spinnerProduct = (CustomSpinner) v.findViewById(R.id.spinnerProduct);
        spinnerModel = (CustomSpinner) v.findViewById(R.id.spinnerModel);
        layoutOtherModel = (LinearLayout) v.findViewById(R.id.layoutOtherModel);

        editTextOtherModel = (EditText) v.findViewById(R.id.editTextOtherModel);
        editTextJobId = (EditText) v.findViewById(R.id.editTextJobId);
        editTextName = (EditText) v.findViewById(R.id.editTextName);
        editTextTel1 = (EditText) v.findViewById(R.id.editTextTel1);
        editTextTel2 = (EditText) v.findViewById(R.id.editTextTel2);
        editTextMachineNumber = (EditText) v.findViewById(R.id.editTextMachineNumber);
        editTextEngineNumber = (EditText) v.findViewById(R.id.editTextEngineNumber);
        editTextWorkHours = (EditText) v.findViewById(R.id.editTextWorkHours);
        editTextAddress = (EditText) v.findViewById(R.id.editTextAddress);

        locationButton = (ImageButton) v.findViewById(R.id.locationButton);

        radioGroupUserType = (RadioGroup) v.findViewById(R.id.radioGroupUserType);

        spinnerJobType.setAdapter(selectNoneJobTypeSpinnerAdapter);
        spinnerProduct.setAdapter(selectNoneProductSpinnerAdapter);
        spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);


        spinnerModel.setPrompt(getString(R.string.service_hint_model));

        Ui.setupUI(getActivity(), rootLayout);

        setDataChangeListener();

    }

    private void setDataChangeListener() {

        spinnerJobType.setOnItemSelectedListener(this);
        spinnerProduct.setOnItemSelectedListener(this);
        spinnerModel.setOnItemSelectedListener(this);

        editTextOtherModel.addTextChangedListener(new GenericTextWatcher(editTextOtherModel));
        editTextJobId.addTextChangedListener(new GenericTextWatcher(editTextJobId));
        editTextName.addTextChangedListener(new GenericTextWatcher(editTextName));
        editTextTel1.addTextChangedListener(new GenericTextWatcher(editTextTel1));
        editTextTel2.addTextChangedListener(new GenericTextWatcher(editTextTel2));
        editTextMachineNumber.addTextChangedListener(new GenericTextWatcher(editTextMachineNumber));
        editTextEngineNumber.addTextChangedListener(new GenericTextWatcher(editTextEngineNumber));
        editTextWorkHours.addTextChangedListener(new GenericTextWatcher(editTextWorkHours));
        editTextAddress.addTextChangedListener(new GenericTextWatcher(editTextAddress));

        locationButton.setOnClickListener(this);

        radioGroupUserType.setOnCheckedChangeListener(this);
    }


    protected void startIntentService() {

        Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);


        if (fetchType == Constants.USE_ADDRESS_NAME) {
            String addressText = "";
            if (addressText.length() == 0) {
                Toast.makeText(getActivity(), "Please enter an address name", Toast.LENGTH_LONG).show();
                return;
            }
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, addressText);
        } else {
           /* if(latitudeText.length() == 0 || longitudeText.length() == 0) {
                Toast.makeText(getActivity(),
                        "Please enter both latitude and longitude",
                        Toast.LENGTH_LONG).show();
                return;
            }*/

            if (mLastLocation == null) {
                return;
            }
            /*intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    Double.parseDouble(latitudeText));
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    Double.parseDouble(longitudeText));
*/
            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    mLastLocation.getLatitude());
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    mLastLocation.getLongitude());
        }

        getActivity().startService(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //mListener.onFragmentPresent(this, title);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

       /* if (checkLocationServiceEnable(getActivity())) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }*/

        if (checkLocationServiceEnable(getActivity())) {
            getCurrentLocation();
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (waitGPSSetting) {

            waitGPSSetting = false;

            if (checkLocationServiceEnable(getActivity())) {
                getCurrentLocation();
            }

        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //public void onFragmentInteraction(Uri uri);
        //public void onFragmentPresent(Fragment fragment, String title);
        public void onFragmentDataComplete(Fragment fragment, boolean complete);
        //public void onRequestAddress();

    }

    public void setResultAddress(String address) {
        editTextAddress.setText(address);
    }

    ///////////////////////////////////////////////////////////////////////////////  implement method


    @Override
    public void onClick(View v) {
        if (v == locationButton) {

            /*if(!checkLocationServiceEnable(getActivity())){
                //popupLocationSetting();
                mAddressRequested = true;

            }else{
                if (mGoogleApiClient.isConnected() && mLastLocation != null) {
                    startIntentService();
                }else{
                    mAddressRequested = true;
                }
            }*/

           /* if (!EnableGPSIfPossible()) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLastLocation != null) {
                    startIntentService();
                } else {
                    mAddressRequested = true;
                }
            }*/

            if (!EnableGPSIfPossible()) {
                if ( mLastLocation != null) {
                    startIntentService();
                } else {
                    mAddressRequested = true;
                    getCurrentLocation();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent == spinnerJobType) {

        } else if (parent == spinnerProduct) {

            //int tmppos = position - 1;

            switch (position) {
                case 0:
                    modelDataList = new String[]{getString(R.string.service_hint_model)};
                    break;
                case 1:
                    modelDataList = getResources().getStringArray(R.array.product_tractor);
                    break;
                case 2:
                    modelDataList = getResources().getStringArray(R.array.product_harvester);
                    break;
                case 3:
                    modelDataList = getResources().getStringArray(R.array.product_digger);
                    break;
                case 4:
                default:
                    modelDataList = getResources().getStringArray(R.array.product_planter);
                    break;
            }

            if (position != 0) {
                spinnerModel.setPrompt(productDataList[position - 1]);
                //selectNoneModelSpinnerAdapter.setPromptText(productDataList[position - 1]);
                spinnerModel.setEnabled(true);
                spinnerModel.invalidate();
            } else {
                spinnerModel.setPrompt(getString(R.string.service_hint_model));
                selectNoneModelSpinnerAdapter.setPromptText(getString(R.string.service_hint_model));
                spinnerModel.setEnabled(false);
            }


            modelSpinnerAdapter.setItemList(modelDataList);
            selectNoneModelSpinnerAdapter.setAdapter(modelSpinnerAdapter);
            spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);
            spinnerModel.invalidate();


        } else if (parent == spinnerModel) {
            if (position == selectNoneModelSpinnerAdapter.getCount() - 1) {
                //layoutOtherModel.setVisibility(View.VISIBLE);
                editTextOtherModel.setVisibility(View.VISIBLE);
            } else {
                //layoutOtherModel.setVisibility(View.GONE);
                editTextOtherModel.setVisibility(View.GONE);
            }
        }

        if (view != null) {

            LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            TextView textViewDialog = (TextView) view.findViewById(R.id.textView);
            textViewDialog.setTextSize(Converter.pxTosp(getActivity(), Converter.dpTopx(getActivity(), 15)));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textViewDialog.getLayoutParams();
            int left = Converter.dpTopx(getActivity(), 10);
            int top = Converter.dpTopx(getActivity(), 5);
            params.setMargins(left, top, 0, top);
            textViewDialog.setLayoutParams(params);

            textViewDialog.invalidate();

            LinearLayout.LayoutParams rootparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootLayout.setLayoutParams(rootparams);
            rootLayout.invalidate();

        }

        validateInput();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == radioGroupUserType) {

        }
        validateInput();
    }

    /////////////////////////////////////////////////////////// TextWatcher

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :

            if (!text.isEmpty() && view != null) {


            }

            validateInput();
        }
    }

    private void validateInput() {

        View view = Validate.inputValidate(rootLayout);
        if (view != null) {
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete);
            return;
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete);
    }

    ////////////////////////////////////////////////////////////// get address

    private void getCurrentLocation() {

        if (checkPlayServices()) {

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        } else {
            locMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locLsnr);
        }



    }

    private LocationManager locMgr;
    private LocationListener locLsnr = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            double myLat = location.getLatitude();
//            double myLon = location.getLongitude();
            mLastLocation = location;

            if (mLastLocation != null && mAddressRequested) {

                mAddressRequested = false;
                // Determine whether a Geocoder is available.
                if (!Geocoder.isPresent()) {
                    /*Toast.makeText(getActivity(), R.string.no_geocoder_available,
                            Toast.LENGTH_LONG).show();*/
                    return;
                }
                startIntentService();

            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch( status ) {
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null && mAddressRequested) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                /*Toast.makeText(getActivity(), R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();*/
                return;
            }

            startIntentService();

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
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));*/
                        mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                        //((ServiceFragment) requestAddressFragment).setResultAddress(mAddressOutput);
                        editTextAddress.setText(mAddressOutput);

                        mAddressRequested = false;
                    }
                });



            }
            else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));*/
                        Log.d("ADDRESS", "Address not found");
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    protected void stopLocationUpdates() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
           // alert error dialog update play service *******
           /* if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                Locale locale = new Locale("th", "TH");
                Locale.setDefault(locale);

                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                //finish();
            }*/
            return false;
        }
        return true;
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
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            mAddressRequested = true;
            return true;
        }
        return false;
    }

   private boolean waitGPSSetting;
    private  AlertDialog alert;
    private  void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.service_setting_gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.service_button_ok), new CommandWrapper(new EnableGpsCommand(getActivity())))
                .setNegativeButton(getString(R.string.service_button_cancel), new CommandWrapper(new CancelCommand(getActivity())));

        alert = builder.create();
        alert.show();
    }

}
