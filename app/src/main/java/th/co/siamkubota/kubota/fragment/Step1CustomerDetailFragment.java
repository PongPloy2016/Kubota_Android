package th.co.siamkubota.kubota.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.util.ArrayList;
import java.util.Locale;

import io.swagger.client.model.TaskInfo;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.MapsActivity;
import th.co.siamkubota.kubota.activity.MapsDragableActivity;
import th.co.siamkubota.kubota.activity.MapsFixPointActivity;
import th.co.siamkubota.kubota.activity.TestDragMarkerActivity;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.logger.Logger;
import th.co.siamkubota.kubota.service.Constants;
import th.co.siamkubota.kubota.service.FetchAddressIntentService;
import th.co.siamkubota.kubota.service.GeocodeAddressIntentService;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.LocationService;
import th.co.siamkubota.kubota.utils.function.Network;
import th.co.siamkubota.kubota.utils.function.Ui;
import th.co.siamkubota.kubota.utils.function.Validate;
import th.co.siamkubota.kubota.utils.ui.CustomSpinner;
import th.co.siamkubota.kubota.utils.ui.CustomSpinnerDialog;

import static android.content.Context.LOCATION_SERVICE;
import static th.co.siamkubota.kubota.activity.MapsFixPointActivity.PERMISSION_ALL;

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
        com.google.android.gms.location.LocationListener,
        LocationListener {

    private static final String TAG = Step1CustomerDetailFragment.class.getSimpleName();

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TASK_INFO = "TASK_INFO";
    private static final String KEY_SERVICE_ADDRESS = "SERVICE_ADDRESS";
    private static final String KEY_EDITABLED = "EDITABLED";
    private static final int REQUEST_CODE_MAP = 200;

    private LinearLayout rootLayout;
    private TextView textStepTitle;
    private CustomSpinner spinnerJobType;
    private CustomSpinner spinnerProduct;
    private CustomSpinner spinnerModel;
    private LinearLayout layoutOtherModel;
    private int OnClickMap = 0 ;

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
    private EditText editTextTaskCode;
    private EditText editTextName;
    private EditText editTextTel1;
    private EditText editTextTel2;
    private EditText editTextCarNumber;
    private EditText editTextEngineNumber;
    private EditText editTextWorkHours;
    private EditText editTextGPSLocation;
    private EditText editTextServiceAddress;
    private EditText editTextCustomerAddress;

    private GenericTextWatcher editTextTaskCodeWatcher;

    private ImageButton locationButton;

    private RadioGroup radioGroupUserType;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private int checkAndroid = 0;
    private int checkAndroidM = 0;

    private Button addButton;
    private LinearLayout customerAddressBlock;

    //protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private boolean mAddressRequested;
    Location location; // location
    //private GoogleApiClient mGoogleApiClient;
    //protected Location mCurrentLocation;
    //private AddressResultReceiver mResultReceiver;
    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    private String latitudeText = "13.968278";
    private String longitudeText = "100.633676";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    boolean canGetLocation = false;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private boolean waitGPSSetting;
    private AlertDialog alert;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    //private String title;
    private boolean dataComplete = false;
    private OnFragmentInteractionListener mListener;
    private TaskInfo taskInfo;
    private TaskDataSource dataSource;
    private boolean editabled = true;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude; // latitude
    double longitude; // longitude
    private Location mLocation;

    private String provider;
    private MaterialDialog materialDialog;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == editTextTaskCode && hasFocus) {
                EditText editText = (EditText) v;

                editText.setOnFocusChangeListener(null);
                editText.removeTextChangedListener(editTextTaskCodeWatcher);

                String text = editText.getText().toString();

                if (text.length() == 0) {
                    editText.setText("OJ");
                }

                text = editText.getText().toString();
                editText.setCursorVisible(true);
                editText.setSelection(text.length());

                editText.setOnFocusChangeListener(focusChangeListener);
                editText.addTextChangedListener(editTextTaskCodeWatcher);

            }
        }
    };


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

    public static Step1CustomerDetailFragment newInstance(TaskInfo taskInfo, boolean editabled) {
        Step1CustomerDetailFragment fragment = new Step1CustomerDetailFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_TASK_INFO, taskInfo);
        args.putBoolean(KEY_EDITABLED, editabled);
        fragment.setArguments(args);
        return fragment;
    }

    public Step1CustomerDetailFragment() {
        // Required empty public constructor
    }


    //////////////////////////////////////////////////////////////////// onCreate

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(KEY_TASK_INFO, taskInfo);
        //mListener.onFragmentSaveInstanceState(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            //taskInfo = savedInstanceState.getParcelable(KEY_TASK_INFO);

        }

        mListener = (Step1CustomerDetailFragment.OnFragmentInteractionListener) getParentFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(savedInstanceState == null){

        if (getArguments() != null) {
            taskInfo = getArguments().getParcelable(KEY_TASK_INFO);
            editabled = getArguments().getBoolean(KEY_EDITABLED);
        }
        //taskInfo = new TaskInfo();

        jobTypeDataList = getResources().getStringArray(R.array.job_type);
        productDataList = getResources().getStringArray(R.array.product);
        modelDataList = getModelDataList(0);

        if (taskInfo != null && taskInfo.getProduct() != null && !taskInfo.getProduct().isEmpty()) {

            for (int i = 0; i < productDataList.length; i++) {

                if (taskInfo.getProduct().equals(productDataList[i])) {
                    modelDataList = getModelDataList(i + 1);
                    break;
                }
            }
        }


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
        //}

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

        v.findViewById(R.id.scroll).requestFocus();

        rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
        spinnerJobType = (CustomSpinner) v.findViewById(R.id.spinnerJobType);
        spinnerProduct = (CustomSpinner) v.findViewById(R.id.spinnerProduct);
        spinnerModel = (CustomSpinner) v.findViewById(R.id.spinnerModel);
        layoutOtherModel = (LinearLayout) v.findViewById(R.id.layoutOtherModel);

        editTextOtherModel = (EditText) v.findViewById(R.id.editTextOtherModel);
        editTextTaskCode = (EditText) v.findViewById(R.id.editTextTaskCode);
        editTextName = (EditText) v.findViewById(R.id.editTextName);
        editTextTel1 = (EditText) v.findViewById(R.id.editTextTel1);
        editTextTel2 = (EditText) v.findViewById(R.id.editTextTel2);
        editTextCarNumber = (EditText) v.findViewById(R.id.editTextCarNumber);
        editTextEngineNumber = (EditText) v.findViewById(R.id.editTextEngineNumber);
        editTextWorkHours = (EditText) v.findViewById(R.id.editTextWorkHours);
        editTextGPSLocation = (EditText) v.findViewById(R.id.editTextGPSLocation);
        editTextServiceAddress = (EditText) v.findViewById(R.id.editTextServiceAddress);
        editTextCustomerAddress = (EditText) v.findViewById(R.id.editTextCustomerAddress);


        locationButton = (ImageButton) v.findViewById(R.id.locationButton);

        radioGroupUserType = (RadioGroup) v.findViewById(R.id.radioGroupUserType);
//        RadioButton radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
//        RadioButton radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);

        radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);

        addButton = (Button) v.findViewById(R.id.addButton);

        customerAddressBlock = (LinearLayout) v.findViewById(R.id.customerAddressBlock);

        radioButton1.getCompoundDrawables()[0].setBounds(0, 0, 10, 10);

        spinnerJobType.setAdapter(selectNoneJobTypeSpinnerAdapter);
        spinnerProduct.setAdapter(selectNoneProductSpinnerAdapter);
        spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);


        spinnerModel.setPrompt(getString(R.string.service_hint_model));

        Ui.setupUI(getActivity(), rootLayout);

        mListener = (Step1CustomerDetailFragment.OnFragmentInteractionListener) getParentFragment();

        setData();
        setDataChangeListener();

        if (getActivity() != null) {
            validateInput();
        }

        spinnerJobType.requestFocus();

    }

    private void setDataChangeListener() {

        spinnerJobType.setOnItemSelectedListener(this);
        spinnerProduct.setOnItemSelectedListener(this);
        spinnerModel.setOnItemSelectedListener(this);

        editTextOtherModel.addTextChangedListener(new GenericTextWatcher(editTextOtherModel));
        editTextTaskCodeWatcher = new GenericTextWatcher(editTextTaskCode);
        editTextTaskCode.addTextChangedListener(editTextTaskCodeWatcher);
        editTextName.addTextChangedListener(new GenericTextWatcher(editTextName));
        editTextTel1.addTextChangedListener(new GenericTextWatcher(editTextTel1));
        editTextTel2.addTextChangedListener(new GenericTextWatcher(editTextTel2));
        editTextCarNumber.addTextChangedListener(new GenericTextWatcher(editTextCarNumber));
        editTextEngineNumber.addTextChangedListener(new GenericTextWatcher(editTextEngineNumber));
        editTextWorkHours.addTextChangedListener(new GenericTextWatcher(editTextWorkHours));
        editTextGPSLocation.addTextChangedListener(new GenericTextWatcher(editTextGPSLocation));
        editTextServiceAddress.addTextChangedListener(new GenericTextWatcher(editTextServiceAddress));
        editTextCustomerAddress.addTextChangedListener(new GenericTextWatcher(editTextCustomerAddress));

        locationButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        radioGroupUserType.setOnCheckedChangeListener(this);

        editTextTaskCode.setOnFocusChangeListener(focusChangeListener);

    }


    private void setData() {

        spinnerJobType.setSelection(getIndex(spinnerJobType, taskInfo.getTaskType()));
        spinnerProduct.setSelection(getIndex(spinnerProduct, taskInfo.getProduct()));
        spinnerModel.setSelection(getIndex(spinnerModel, taskInfo.getCarModel()));

        editTextOtherModel.setText(taskInfo.getCarModelOther());
        editTextTaskCode.setText(taskInfo.getTaskCode());
        editTextName.setText(taskInfo.getCustomerName());
        editTextTel1.setText(taskInfo.getTel1());
        editTextTel2.setText(taskInfo.getTel2());
        editTextCarNumber.setText(taskInfo.getCarNo());
        editTextEngineNumber.setText(taskInfo.getEngineNo());
        editTextWorkHours.setText(taskInfo.getUsageHours());
        editTextServiceAddress.setText(taskInfo.getAddress());
        editTextCustomerAddress.setText(taskInfo.getCustomerAddress());

        if (taskInfo.getAddressPosition() != null) {
            String latlon = String.format("%f,%f", taskInfo.getAddressPosition().latitude, taskInfo.getAddressPosition().longitude);
            editTextGPSLocation.setText(latlon);

            Logger.Log("taskInfo latitude ", String.valueOf(taskInfo.getAddressPosition().latitude));
            Logger.Log("taskInfo longitude ", String.valueOf(taskInfo.getAddressPosition().longitude));
        }


        if (taskInfo != null && taskInfo.getCustomerAddress() != null && !taskInfo.getCustomerAddress().isEmpty()) {
            customerAddressBlock.setVisibility(View.VISIBLE);
            //addButton.setText(getText(R.string.service_remove_address));
        } else {
            customerAddressBlock.setVisibility(View.GONE);
            addButton.setText(getText(R.string.service_add_address));
        }

        if (taskInfo.getIsOwner() != null) {
            if (taskInfo.getIsOwner()) {
                radioGroupUserType.check(R.id.radioButton1);
            } else {
                radioGroupUserType.check(R.id.radioButton2);
            }
        }

        //   setDefault();
        setEnabled(editabled);

    }

    private int getIndex(Spinner spinner, String string) {


        int index = 0;

        if (spinner.getAdapter() != null) {

            SelectNoneSpinnerAdapter adapter = (SelectNoneSpinnerAdapter) spinner.getAdapter();

            for (int i = 0; i < adapter.getCount(); i++) {

                try {
                    //if (adapter.getItem(i).toString().equals(string)){
                    String r = (String) adapter.getItem(i);
                    if (r != null && r.equals(string)) {
                        index = i;
                        break;
                    }
                } catch (NullPointerException e) {
                    return 0;
                }
            }

        }

        return index;

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

            if (mLastLocation == null) {
                return;
            }

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

           /* if (checkLocationServiceEnable(getActivity())) {
                getCurrentLocation();
            }*/


            openMaps();


        }
        if (checkAndroid == 1) {

            if (mLocation != null) {

            } else {
                mLocationCkeck();
            }

        } else {


        }


//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            handleNewLocation(mLocation);
//            return;
//        }
//        else {
//
//        }





       /* if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }

    private void getMapoffine() {


        // Getting LocationManager object
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider

            //  Location location = locationManager.getLastKnownLocation(provider);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling


            } else {

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                locationManager.requestLocationUpdates(provider, 20000, 1, this);

                if (location != null) {
                    //  onLocationChanged(location);

                    // loadData(location);

                } else {
                    Toast.makeText(getContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
                }


            }
            return;
        } else {
            Toast.makeText(getContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }


//        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);


//        locationManager = (LocationManager) getContext()
//                .getSystemService(LOCATION_SERVICE);
//
//        // getting GPS status
//
//        if (LocationService.isGPSEnabled(getContext())) {
//
//            if (locationManager != null) {
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    return;
//                }
//                    location = locationManager  .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                if (location == null) {
//
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                            2000, 1, this);
//
//                    //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
//                    Logger.Log("GPS Enabled", "GPS Enabled");
//                    if (locationManager != null) {
//
////                        Logger.Log("locationManager not null", "locationManager nit null");
////                        //   location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////                                if (location != null) {
////                                    Logger.Log("location not null", "location nit null");
////                                    latitude = location.getLatitude();
////                                    longitude = location.getLongitude();
////                                    Logger.Log("latitude and longitude ", latitude + "" + longitude);
////                                } else {
////                                    Logger.Log("location is null ", "location is null");
////                                }
//
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//
//                        Logger.Log("latitude and longitude ",latitude +""+longitude);
//                        Toast.makeText(
//                                getContext(),
//                                "Mobile Location (GPS): \nLatitude: " + latitude
//                                        + "\nLongitude: " + longitude,
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//
//
//            else {
//                Logger.Log("locationManager not null  ", "locationManager  not null ");
//            }
//            Logger.Log("LocationManager ", String.valueOf(locMgr));
        //       }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data);

        public void onFragmentSaveInstanceState(Fragment fragment);

        public void onCustomerNameChange(String name);
    }

    public void setResultAddress(String address) {
        editTextServiceAddress.setText(address);
    }

    ///////////////////////////////////////////////////////////////////////////////  implement method

    @Override
    public void onClick(View v) {
        if (v == locationButton) {

            Logger.Log("check cleck map", "check cleck map");
            if (taskInfo.getAddressPosition() != null || !EnableGPSIfPossible()) {


                if (Network.isNetworkAvailable(getActivity())) {
                    Logger.Log("checkLocationServiceEnable online", "checkLocationServiceEnable online");

                    openMaps();
                } else {
                    Logger.Log("checkLocationServiceEnable offline", "checkLocationServiceEnable offine");

                    //  getMapoffine();

                    if(OnClickMap == 0){
                        OnClickMap =1 ;
                        loadData();
                    }
                    else {
                        Logger.Log("no click", "no click");
                    }



                    //    CallLacationOffine();
                }


            }

            /*if (!EnableGPSIfPossible()) {
                if (mLastLocation != null) {
                    startIntentService();
                } else {
                    mAddressRequested = true;
                    getCurrentLocation();
                }
            }*/
        } else if (v == addButton) {
            if (customerAddressBlock.getVisibility() == View.GONE) {
                addButton.setVisibility(View.GONE);
                customerAddressBlock.setVisibility(View.VISIBLE);
                //addButton.setText(getText(R.string.service_remove_address));

            } else {
                //customerAddressBlock.setVisibility(View.GONE);
                //addButton.setText(getText(R.string.service_add_address));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_MAP) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    editTextServiceAddress.setText(bundle.getString(MapsFixPointActivity.KEY_ADDRESS, ""));
                    //call setResult 2
                    Logger.Log("KEY_POSITION 2", "KEY_POSITION 2");

                    if (bundle.containsKey(MapsFixPointActivity.KEY_POSITION)) {
                        LatLng servicepoint = (LatLng) bundle.getParcelable(MapsFixPointActivity.KEY_POSITION);
                        if (servicepoint != null) {
                            String latlon = String.format("%f,%f", servicepoint.latitude, servicepoint.longitude);
                            editTextGPSLocation.setText(latlon);

                            //set map online
                        }
                        taskInfo.setAddressPosition((LatLng) bundle.getParcelable(MapsFixPointActivity.KEY_POSITION));

                        Logger.Log("setAddressPosition((LatLng)", String.valueOf((LatLng) bundle.getParcelable(MapsFixPointActivity.KEY_POSITION)));
                    }
                }
            }
        }
    }

    private String[] getModelDataList(int position) {

        String[] dataList = new String[0];

        switch (position) {
            case 0:
                dataList = new String[]{getString(R.string.service_hint_model)};
                break;
            case 1:
                dataList = getResources().getStringArray(R.array.product_tractor);
                break;
            case 2:
                dataList = getResources().getStringArray(R.array.product_harvester);
                break;
            case 3:
                dataList = getResources().getStringArray(R.array.product_digger);
                break;
            case 4:
            default:
                dataList = getResources().getStringArray(R.array.product_planter);
                break;
        }

        return dataList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (parent == spinnerJobType) {

            boolean productWasSelected = false;

            String product = (String) spinnerProduct.getSelectedItem();
            if (product != null) {
                for (String s : modelDataList) {
                    int i = s.indexOf(spinnerModel.getSelectedItem().toString());
                    if (i >= 0) {
                        // found a match to "software" at offset i
                        productWasSelected = true;
                        break;
                    }
                }
            }

            if (spinnerJobType.getSelectedItemPosition() > 0 && spinnerProduct.getSelectedItem() == null && editabled &&
                    ((taskInfo.getTaskType() == null || taskInfo.getTaskType().isEmpty()))) {
                spinnerProduct.requestFocus();
                spinnerProduct.performClick();
            }

        } else if (parent == spinnerProduct) {


            modelDataList = getModelDataList(position);

            if (position != 0) {
                //spinnerModel.setPrompt(productDataList[position - 1]);
                //selectNoneModelSpinnerAdapter.setPromptText(productDataList[position - 1]);

                //spinnerModel.setEnabled(true);
                spinnerModel.setEnabled(editabled);
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
            spinnerModel.setSelection(getIndex(spinnerModel, taskInfo.getCarModel()));


            boolean modelWasSelected = false;

            String model = (String) spinnerModel.getSelectedItem();
            if (model != null) {
                for (String s : modelDataList) {
                    int i = s.indexOf(spinnerModel.getSelectedItem().toString());
                    if (i >= 0) {
                        // found a match to "software" at offset i
                        modelWasSelected = true;
                        break;
                    }
                }
            }


            if (Config.showDefault == true) {
                spinnerModel.setSelection(1);
            } else if (spinnerProduct.getSelectedItemPosition() > 0 && editabled && !modelWasSelected) {

                spinnerModel.setPrompt(spinnerProduct.getSelectedItem().toString());

                spinnerModel.requestFocus();
                spinnerModel.performClick();
            }


        } else if (parent == spinnerModel) {
            if (position == selectNoneModelSpinnerAdapter.getCount() - 1) {
                layoutOtherModel.setVisibility(View.VISIBLE);
                editTextOtherModel.setVisibility(View.VISIBLE);
            } else {
                layoutOtherModel.setVisibility(View.GONE);
                editTextOtherModel.setVisibility(View.GONE);
            }

            taskInfo.setCarModel(null);
        }

        if (view != null) {


            LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            TextView textViewDialog = (TextView) view.findViewById(R.id.textView);
            textViewDialog.setTextSize(Converter.pxTosp(getActivity(), Converter.dpTopx(getActivity(), 15)));

            if (position != 0) {
                textViewDialog.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_gray));
                ImageView selectedImage = (ImageView) view.findViewById(R.id.selectedImage);
                selectedImage.setVisibility(View.GONE);
            }

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
       /* if (group == radioGroupUserType) {

        }*/
        validateInput();
    }

    /////////////////////////////////////////////////////////// TextWatcher

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;

            if (this.view instanceof EditText) {
                EditText editText = (EditText) this.view;

                //String text = editText.getText().toString();

                checkRequire(editText);
            }
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            EditText editText = (EditText) this.view;
            if (editText.getTag() != null && editText.getTag().toString().contains("formatted")) {

                if (count > 0) {
                    switch (start) {
                        case 0:
                            if (count == 1) {
                                charSequence = String.format("OJ%s", charSequence);
                                editText.setText(charSequence);
                                editText.setSelection(editText.getText().length());
                            }
                            break;
                        case 1:
                            String text3 = charSequence.toString();
                            charSequence = text3.substring(0, 1) + "J" + text3.substring(1, text3.length());
                            editText.setText(charSequence);
                            editText.setSelection(editText.getText().length());
                            break;

                        case 3:
                        case 8:
                            charSequence = charSequence.toString().concat("-");
                            editText.setText(charSequence);
                            editText.setSelection(editText.getText().length());
                            break;
                        case 4:
                            String text = charSequence.toString();
                            charSequence = text.substring(0, 4) + "-" + text.substring(4, text.length());
                            editText.setText(charSequence);
                            editText.setSelection(editText.getText().length());
                            break;
                        case 9:
                            String text2 = charSequence.toString();
                            charSequence = text2.substring(0, 9) + "-" + text2.substring(9, text2.length());
                            editText.setText(charSequence);
                            editText.setSelection(editText.getText().length());
                            break;
                    }

                }

            }

        }

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //int textlength = editable.toString().length();
            //save the value for the given tag :

            EditText editText = (EditText) this.view;

            checkRequire(editText);
            validateInput();


        }

        public void checkRequire(EditText editText) {
            if (!editText.getText().toString().isEmpty() && view != null) {

                LinearLayout parent = (LinearLayout) this.view.getParent();
                /*ArrayList<View> requires = new ArrayList<View>();
                parent.findViewsWithText(requires,"*",View.FIND_VIEWS_WITH_TEXT);
                for (View v : requires){
                    v.setVisibility(View.GONE);
                }*/

                View required = parent.findViewWithTag("*");
                if (required != null) {

                    int maxLength = Validate.getMaxLengthForEditText(editText);
                    int textLength = editText.getText().toString().length();

                    if (maxLength != -1 && maxLength != textLength) {
                        required.setVisibility(View.VISIBLE);
                    } else {
                        required.setVisibility(View.GONE);
                    }


                }

            } else {
                LinearLayout parent = (LinearLayout) this.view.getParent();
               /* ArrayList<View> requires = new ArrayList<View>();
                parent.findViewsWithText(requires,"*",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                for (View v : requires){
                    v.setVisibility(View.VISIBLE);
                }*/
                View required = parent.findViewWithTag("*");
                //View required = parent.findViewWithTag(text);
                if (required != null) {
                    required.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void validateInput() {

        View view = Validate.inputValidateOptions(rootLayout, "required");
        if (view != null) {
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete, collectData());
            return;
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete, collectData());


    }

    private TaskInfo collectData() {

        if (spinnerJobType.getSelectedItem() != null) {
            taskInfo.setTaskType(spinnerJobType.getSelectedItem().toString());
        }
        if (spinnerProduct.getSelectedItem() != null) {
            taskInfo.setProduct(spinnerProduct.getSelectedItem().toString());
        }
        if (spinnerModel.getSelectedItem() != null) {
            taskInfo.setCarModel(spinnerModel.getSelectedItem().toString());
        }

        if (taskInfo.getCarModel() != null && taskInfo.getCarModel().equals("อื่นๆ") && editTextOtherModel.getText() != null) {
            taskInfo.setCarModelOther(editTextOtherModel.getText().toString());
        } else {
            taskInfo.setCarModelOther("");
        }

        if (editTextTaskCode.getText() != null) {
            String text = editTextTaskCode.getText().toString();
            if (text.length() == 10) {
                String textFormatted = new StringBuilder(text).insert(0, "OJ").insert(4, "-").insert(9, "-").toString();
                taskInfo.setTaskCode(textFormatted);
            } else {
                taskInfo.setTaskCode(editTextTaskCode.getText().toString());
            }
        }

        if (editTextName.getText() != null) {

            if (!editTextName.getText().equals(taskInfo.getCustomerName())) {

            }
            taskInfo.setCustomerName(editTextName.getText().toString());
        }
        if (editTextTel1.getText() != null) {
            taskInfo.setTel1(editTextTel1.getText().toString());
        }

        if (editTextTel2.getText() != null && !editTextTel2.getText().toString().isEmpty()) {
            taskInfo.setTel2(editTextTel2.getText().toString());
        } else {
            taskInfo.setTel2("");
        }

        if (editTextCarNumber.getText() != null) {
            taskInfo.setCarNo(editTextCarNumber.getText().toString());
        }
        if (editTextEngineNumber.getText() != null) {
            taskInfo.setEngineNo(editTextEngineNumber.getText().toString());
        }
        if (editTextWorkHours.getText() != null) {
            taskInfo.setUsageHours(editTextWorkHours.getText().toString());
        }
        if (editTextServiceAddress.getText() != null) {
            taskInfo.setAddress(editTextServiceAddress.getText().toString());
        }
        if (editTextCustomerAddress.getText() != null) {
            taskInfo.setCustomerAddress(editTextCustomerAddress.getText().toString());
        }


        if (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButton1) {
            taskInfo.setIsOwner(true);
        } else if (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButton2) {
            taskInfo.setIsOwner(false);
        }


        return taskInfo;
    }

    ////////////////////////////////////////////////////////////// get address

    private void getCurrentLocation() {

        if (checkPlayServices()) {

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        } else {
            locMgr = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
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

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Logger.Log("latitude and longitude ", latitude + ":" + longitude);

            //if(location.getAccuracy() < 100.0 && location.getSpeed() < 6.95){
            double suitableMeter = 100.0; // adjust your need
            if (location.getAccuracy() < suitableMeter) {

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
            } else {
                //Continue listening for a more accurate location
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

        Log.i(TAG, "Location services connected");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //   CallLacationOffine();

    }

    private void CallLacationOffine() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            materialDialog.hide();

            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);

            checkAndroid = 1;


            return;
        } else {
            mLocationCkeck();
        }
    }

    private void mLocationCkeck() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        } else {
            handleNewLocation(mLocation);
        }
    }


    private void handleNewLocation(Location mLocation) {
      //  Log.d(TAG, "The location is currently: " + mLocation.toString());

        double currentLat = mLocation.getLatitude();
        double currentLng = mLocation.getLongitude();

        Logger.Log( "The location is currently: ", String.valueOf(currentLat));
        Logger.Log( "The location is currentLng: ", String.valueOf(currentLng));

        LatLng latlng1 = new LatLng(currentLat, currentLng);
        taskInfo.setAddressPosition(latlng1);
        String latlonOffline = String.format("%f,%f", mLocation.getLatitude(),mLocation.getLongitude());
        editTextGPSLocation.setText(latlonOffline);
        mLastLocation = location;
        materialDialog.hide();

    }

    private void displayLocation() {

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
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



    private void loadData(  ) {
        new AsyncTask<Void, Void, Void>(){


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //     showIndeterminateProgressDialog(true);

                if(materialDialog == null) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Step1CustomerDetailFragment.this.getContext())
                            .title("แจ้งเตือน")
                            .content("กรุณารอซักครู่ค่ะ")
                            .progress(true, 0);

                     materialDialog = builder.build();
                    materialDialog.show();
                }

//                if(pDialog == null){
//                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
//                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                    pDialog.setTitleText("Loading");
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                }
//                else {
//                    Logger.Log("PD.Null","PD.Null");
//                }
            }

            @Override
            protected Void doInBackground(Void... voids) {

                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // do some thing which you want in try block
                        try {

                            CallLacationOffine();
                           // onLocationChanged(location);
                        } catch (Throwable e) {
                            e.printStackTrace();

                            intentChectSendEmail(e.toString());
                            //  intentChectSendEmail(nameId);
                        }
                    }
                });

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Logger.Log("go next","go next");

             //   materialDialog.hide();

                Logger.Log("pDialog.dismiss() News", "pDialog.dismiss() News");

            }
        }.execute();
    }



    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();

        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();


            LatLng latlng1 = new LatLng(location.getLatitude(), location.getLongitude());

        taskInfo.setAddressPosition(latlng1);

        String latlonOffline = String.format("%f,%f", location.getLatitude(),location.getLongitude());

        editTextGPSLocation.setText(latlonOffline);


        mLastLocation = location;

        // materialDialog.hide();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
                        editTextServiceAddress.setText(mAddressOutput);

                        mAddressRequested = false;
                    }
                });


            } else {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
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
    private boolean checkPlayServices(boolean check) {
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

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
           /* if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }*/

            return false;
        }

        return true;
    }

    //////////////////////////////////////////////////////////////////// enable GPS

    public interface ICommand {
        void execute();
    }

    public class CancelCommand implements ICommand {
        protected Activity m_activity;

        public CancelCommand(Activity activity) {
            m_activity = activity;
        }

        public void execute() {
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

    private boolean checkLocationServiceEnable(Context mContext) {
        boolean gps_enabled = LocationService.isGPSEnabled(mContext);
        boolean network_enabled = Network.isNetworkEnabled(mContext);

        return (gps_enabled && network_enabled);

    }


    public class EnableGpsCommand extends CancelCommand {
        public EnableGpsCommand(Activity activity) {
            super(activity);
        }

        public void execute() {
            // take the user to the phone gps settings and then start the asyncronous logic.
            m_activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            waitGPSSetting = true;
            super.execute();
        }
    }

    private boolean EnableGPSIfPossible() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            mAddressRequested = true;
            return true;
        }
        return false;
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.service_setting_gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.service_button_ok), new CommandWrapper(new EnableGpsCommand(getActivity())))
                .setNegativeButton(getString(R.string.service_button_cancel), new CommandWrapper(new CancelCommand(getActivity())));

        alert = builder.create();
        alert.show();
    }

    private long updateTask(String taskCode, String newTaskCode) {

        dataSource = new TaskDataSource(getActivity());
        dataSource.open();

        return dataSource.updateTaskCode(taskCode, newTaskCode);

    }

    private void deleteTask(String taskCode) {

        dataSource = new TaskDataSource(getActivity());
        dataSource.open();

        dataSource.deleteTask(taskCode);

    }

    private void openMaps() {

        Logger.Log("taskInfo.getAddressPosition()", String.valueOf(taskInfo.getAddressPosition()));
        //Intent intent = new Intent(getActivity(), MapsDragableActivity.class);
        Intent intent = new Intent(getActivity(), MapsFixPointActivity.class);
        //startActivity(intent);
        if (taskInfo.getAddressPosition() != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MapsFixPointActivity.KEY_POSITION, taskInfo.getAddressPosition());
            intent.putExtras(bundle);
            Logger.Log("taskInfo.getAddressPosition() not null ", String.valueOf(taskInfo.getAddressPosition()));
        } else {



            getActivity().startActivityForResult(intent, REQUEST_CODE_MAP);
        }

    }




    private void setDefault(){

        if(Config.showDefault == true){
            spinnerJobType.setSelection(1);
            spinnerProduct.setSelection(1);

            //spinnerModel.setEnabled(true);
            spinnerModel.setEnabled(editabled);
            spinnerModel.invalidate();
            spinnerModel.setSelection(1);

            //editTextOtherModel.setText(taskInfo.getCarModelOther());
            editTextTaskCode.setText("OJ12-3456-7890");
            editTextName.setText("สมชาย ทำไร่ไถนา");
            editTextTel1.setText("0891234567");
            editTextTel2.setText("");
            editTextCarNumber.setText("111111");
            editTextEngineNumber.setText("0844367");
            editTextWorkHours.setText("10");
            editTextGPSLocation.setText("13.15466,100.15552");
            editTextServiceAddress.setText("บ้านหนองใหญ่ ต.ใหญ่มาก อ.ใหญ่สุดๆ");
            editTextCustomerAddress.setText("21/43 หมู่6 บ้านหนองใหญ่ ต.ใหญ่มาก อ.ใหญ่สุดๆ");

            radioGroupUserType.check(R.id.radioButton1);

            setEnabled(editabled);

        }
    }

    private void setEnabled(boolean enabled){

        if(!enabled){
            spinnerJobType.setEnabled(enabled);
            spinnerProduct.setEnabled(enabled);

            spinnerModel.setEnabled(enabled);

            //editTextOtherModel.setText(taskInfo.getCarModelOther());
            editTextTaskCode.setEnabled(enabled);
            editTextName.setEnabled(enabled);
            editTextTel1.setEnabled(enabled);
            editTextTel2.setEnabled(enabled);
            editTextCarNumber.setEnabled(enabled);
            editTextEngineNumber.setEnabled(enabled);
            editTextWorkHours.setEnabled(enabled);
            editTextServiceAddress.setEnabled(enabled);
            editTextCustomerAddress.setEnabled(enabled);

            radioGroupUserType.setEnabled(enabled);

            radioButton1.setEnabled(enabled);
            radioButton2.setEnabled(enabled);

            addButton.setVisibility(View.GONE);
        }
    }


    public void intentChectSendEmail(String Error) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pongku71@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, " กรุณาระบุปัญหาของคุณด้วยค่ะ !!! ");
        i.putExtra(Intent.EXTRA_TEXT   ,Error);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }


}
