package th.co.siamkubota.kubota.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.util.ArrayList;
import java.util.Locale;

import io.swagger.client.model.TaskInfo;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;
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

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TASK_INFO = "TASK_INFO";

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
    private EditText editTextTaskCode;
    private EditText editTextName;
    private EditText editTextTel1;
    private EditText editTextTel2;
    private EditText editTextCarNumber;
    private EditText editTextEngineNumber;
    private EditText editTextWorkHours;
    private EditText editTextServiceAddress;
    private EditText editTextCustomerAddress;


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

    public static Step1CustomerDetailFragment newInstance(TaskInfo taskInfo) {
        Step1CustomerDetailFragment fragment = new Step1CustomerDetailFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_TASK_INFO, taskInfo);
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

        if(savedInstanceState != null){

            //taskInfo = savedInstanceState.getParcelable(KEY_TASK_INFO);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(savedInstanceState == null){

            if (getArguments() != null) {
                taskInfo = getArguments().getParcelable(KEY_TASK_INFO);
            }
            //taskInfo = new TaskInfo();

            jobTypeDataList = getResources().getStringArray(R.array.job_type);
            productDataList = getResources().getStringArray(R.array.product);
            modelDataList = getModelDataList(0);

            if(taskInfo != null && taskInfo.getProduct() != null && !taskInfo.getProduct().isEmpty()){

                for (int i = 0 ; i < productDataList.length ; i++){

                    if(taskInfo.getProduct().equals(productDataList[i])){
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
        editTextServiceAddress = (EditText) v.findViewById(R.id.editTextServiceAddress);
        editTextCustomerAddress = (EditText) v.findViewById(R.id.editTextCustomerAddress);


        locationButton = (ImageButton) v.findViewById(R.id.locationButton);

        radioGroupUserType = (RadioGroup) v.findViewById(R.id.radioGroupUserType);
        RadioButton radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);

        radioButton1.getCompoundDrawables()[0].setBounds(0, 0, 10, 10);

        spinnerJobType.setAdapter(selectNoneJobTypeSpinnerAdapter);
        spinnerProduct.setAdapter(selectNoneProductSpinnerAdapter);
        spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);


        spinnerModel.setPrompt(getString(R.string.service_hint_model));

        Ui.setupUI(getActivity(), rootLayout);

        setData();
        setDataChangeListener();
        validateInput();

    }

    private void setDataChangeListener() {

        spinnerJobType.setOnItemSelectedListener(this);
        spinnerProduct.setOnItemSelectedListener(this);
        spinnerModel.setOnItemSelectedListener(this);

        editTextOtherModel.addTextChangedListener(new GenericTextWatcher(editTextOtherModel));
        editTextTaskCode.addTextChangedListener(new GenericTextWatcher(editTextTaskCode));
        editTextName.addTextChangedListener(new GenericTextWatcher(editTextName));
        editTextTel1.addTextChangedListener(new GenericTextWatcher(editTextTel1));
        editTextTel2.addTextChangedListener(new GenericTextWatcher(editTextTel2));
        editTextCarNumber.addTextChangedListener(new GenericTextWatcher(editTextCarNumber));
        editTextEngineNumber.addTextChangedListener(new GenericTextWatcher(editTextEngineNumber));
        editTextWorkHours.addTextChangedListener(new GenericTextWatcher(editTextWorkHours));
        editTextServiceAddress.addTextChangedListener(new GenericTextWatcher(editTextServiceAddress));
        editTextCustomerAddress.addTextChangedListener(new GenericTextWatcher(editTextCustomerAddress));

        locationButton.setOnClickListener(this);

        radioGroupUserType.setOnCheckedChangeListener(this);
    }

    private void setData() {

        spinnerJobType.setSelection(getIndex(spinnerJobType, taskInfo.getTaskType()));
        spinnerProduct.setSelection(getIndex(spinnerProduct,  taskInfo.getProduct()));
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

        if(taskInfo.getOwner()){
            radioGroupUserType.check(R.id.radioButton1);
        }else if(taskInfo.getUser()){
            radioGroupUserType.check(R.id.radioButton2);
        }

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
        public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data);
        public void onFragmentSaveInstanceState(Fragment fragment);

    }

    public void setResultAddress(String address) {
        editTextServiceAddress.setText(address);
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
                if (mLastLocation != null) {
                    startIntentService();
                } else {
                    mAddressRequested = true;
                    getCurrentLocation();
                }
            }
        }
    }

    private String[] getModelDataList(int position){

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

        } else if (parent == spinnerProduct) {


            modelDataList = getModelDataList(position);

            if (position != 0) {
                //spinnerModel.setPrompt(productDataList[position - 1]);
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
            spinnerModel.setSelection(getIndex(spinnerModel, taskInfo.getCarModel()));


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

            if(position != 0){
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
        if (group == radioGroupUserType) {

        }
        validateInput();
    }

    /////////////////////////////////////////////////////////// TextWatcher

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;

            if(this.view instanceof EditText){
                EditText editText = (EditText) this.view;

                String text = editText.getText().toString();

                checkRequire(text);
            }
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
            checkRequire(text);

            validateInput();
        }

        public void checkRequire(String text){
            if (!text.isEmpty() && view != null) {

                LinearLayout parent = (LinearLayout) this.view.getParent();
                /*ArrayList<View> requires = new ArrayList<View>();
                parent.findViewsWithText(requires,"*",View.FIND_VIEWS_WITH_TEXT);
                for (View v : requires){
                    v.setVisibility(View.GONE);
                }*/
                View required = parent.findViewWithTag("*");
                if (required != null) {
                    required.setVisibility(View.GONE);
                }


            } else {
                LinearLayout parent = (LinearLayout) this.view.getParent();
               /* ArrayList<View> requires = new ArrayList<View>();
                parent.findViewsWithText(requires,"*",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                for (View v : requires){
                    v.setVisibility(View.VISIBLE);
                }*/
                View required = parent.findViewWithTag("*");
                if (required != null) {
                    required.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void validateInput() {

        View view = Validate.inputValidate(rootLayout, "required");
        if (view != null) {
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete, collectData());
            return;
        }


        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete, collectData());
    }

    private TaskInfo collectData() {

        if(spinnerJobType.getSelectedItem() != null){
            taskInfo.setTaskType(spinnerJobType.getSelectedItem().toString());
        }
        if(spinnerProduct.getSelectedItem() != null){
            taskInfo.setProduct(spinnerProduct.getSelectedItem().toString());
        }
        if(spinnerModel.getSelectedItem() != null){
            taskInfo.setCarModel(spinnerModel.getSelectedItem().toString());
        }

        if (taskInfo.getCarModel() != null && taskInfo.getCarModel().equals("อื่นๆ") && editTextOtherModel.getText() != null ) {
            taskInfo.setCarModelOther(editTextOtherModel.getText().toString());
        } else {
            taskInfo.setCarModelOther("");
        }

       /* if(taskInfo.getTaskCode() != null && !taskInfo.getTaskCode().equals(editTextTaskCode.getText().toString()) && !editTextTaskCode.getText().toString().isEmpty()){
            long row = updateTask(taskInfo.getTaskCode(), editTextTaskCode.getText().toString() );

            if(row < 0){
                deleteTask(taskInfo.getTaskCode());
            }

        }else if(taskInfo.getTaskCode() == null && editTextTaskCode.getText() != null && !editTextTaskCode.getText().toString().isEmpty()){
            long row = updateTask("undefine", editTextTaskCode.getText().toString() );
        }*/

        if(editTextTaskCode.getText() != null){
            taskInfo.setTaskCode(editTextTaskCode.getText().toString());
        }

        if(editTextName.getText() != null){
            taskInfo.setCustomerName(editTextName.getText().toString());
        }
        if(editTextTel1.getText() != null){
            taskInfo.setTel1(editTextTel1.getText().toString());
        }

        if (editTextTel2.getText() != null && !editTextTel2.getText().toString().isEmpty()) {
            taskInfo.setTel2(editTextTel2.getText().toString());
        } else {
            taskInfo.setTel2("");
        }

        if(editTextCarNumber.getText() != null){
            taskInfo.setCarNo(editTextCarNumber.getText().toString());
        }
        if(editTextEngineNumber.getText() != null){
            taskInfo.setEngineNo(editTextEngineNumber.getText().toString());
        }
        if(editTextWorkHours.getText() != null){
            taskInfo.setUsageHours(editTextWorkHours.getText().toString());
        }
        if(editTextServiceAddress.getText() != null){
            taskInfo.setAddress(editTextServiceAddress.getText().toString());
        }
        if(editTextCustomerAddress.getText() != null){
            taskInfo.setCustomerAddress(editTextCustomerAddress.getText().toString());
        }


        if(radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButton1){
            taskInfo.setOwner(true);
            taskInfo.setUser(false);
        }else  if(radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButton2){
            taskInfo.setOwner(false);
            taskInfo.setUser(true);
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
        if(result != ConnectionResult.SUCCESS) {
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

    private long updateTask(String taskCode, String newTaskCode){

        dataSource = new TaskDataSource(getActivity());
        dataSource.open();

        return dataSource.updateTaskCode(taskCode, newTaskCode);

    }

    private void deleteTask(String taskCode){

        dataSource = new TaskDataSource(getActivity());
        dataSource.open();

        dataSource.deleteTask(taskCode);

    }

}
