package th.co.siamkubota.kubota.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;
import th.co.siamkubota.kubota.service.FetchAddressIntentService;
import th.co.siamkubota.kubota.utils.function.Converter;
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
        AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

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

    private RadioGroup radioGroupUserType;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private boolean mAddressRequested;
    private GoogleApiClient mGoogleApiClient;

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

        radioGroupUserType = (RadioGroup) v.findViewById(R.id.radioGroupUserType);

        spinnerJobType.setAdapter(selectNoneJobTypeSpinnerAdapter);
        spinnerProduct.setAdapter(selectNoneProductSpinnerAdapter);
        spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);


        spinnerModel.setPrompt(getString(R.string.service_hint_model));

        Ui.setupUI(getActivity(), rootLayout);

        setDataChangeListener();

    }

    private void setDataChangeListener(){

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

        radioGroupUserType.setOnCheckedChangeListener(this);
    }


    protected void startIntentService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, mLastLocation);
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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent == spinnerJobType){

        }else if(parent == spinnerProduct){

                //int tmppos = position - 1;

            switch(position){
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

            if(position != 0){
                spinnerModel.setPrompt(productDataList[position -1]);
                //selectNoneModelSpinnerAdapter.setPromptText(productDataList[position - 1]);
                spinnerModel.setEnabled(true);
                spinnerModel.invalidate();
            }else{
                spinnerModel.setPrompt(getString(R.string.service_hint_model));
                selectNoneModelSpinnerAdapter.setPromptText(getString(R.string.service_hint_model));
                spinnerModel.setEnabled(false);
            }


            modelSpinnerAdapter.setItemList(modelDataList);
            selectNoneModelSpinnerAdapter.setAdapter(modelSpinnerAdapter);
            spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);
            spinnerModel.invalidate();


        }else if(parent == spinnerModel){
            if(position == selectNoneModelSpinnerAdapter.getCount() -1){
                //layoutOtherModel.setVisibility(View.VISIBLE);
                editTextOtherModel.setVisibility(View.VISIBLE);
            }else{
                //layoutOtherModel.setVisibility(View.GONE);
                editTextOtherModel.setVisibility(View.GONE);
            }
        }

        if(view != null){

            LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            TextView textViewDialog = (TextView) view.findViewById(R.id.textView);
            textViewDialog.setTextSize(Converter.pxTosp(getActivity(), Converter.dpTopx(getActivity(), 15)));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)textViewDialog.getLayoutParams();
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
        if(group == radioGroupUserType){

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

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :

            if(!text.isEmpty() && view != null){


            }

            validateInput();
        }
    }

    private void validateInput(){

        View view = Validate.inputValidate(rootLayout);
        if(view != null ){
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete);
            return;
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete);
    }

    ////////////////////////////////////////////////////////////// get address

    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                startIntentService();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void displayAddressOutput(){

    }

    public void fetchAddressButtonHandler(View view) {
        // Only start the service to fetch the address if GoogleApiClient is
        // connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, process the user's request by
        // setting mAddressRequested to true. Later, when GoogleApiClient connects,
        // launch the service to fetch the address. As far as the user is
        // concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
        //updateUIWidgets();
    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
                Toast.makeText(getActivity(),getString(R.string.address_found), Toast.LENGTH_LONG ).show();
            }

        }
    }
}
