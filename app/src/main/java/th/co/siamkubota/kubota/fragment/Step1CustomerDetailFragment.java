package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.Ui;
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
        AdapterView.OnItemSelectedListener{

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


    private String title;
    //private View v;

    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
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
        setRetainInstance(true);
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

        spinnerJobType.setAdapter(selectNoneJobTypeSpinnerAdapter);
        spinnerProduct.setAdapter(selectNoneProductSpinnerAdapter);
        spinnerModel.setAdapter(selectNoneModelSpinnerAdapter);

        spinnerJobType.setOnItemSelectedListener(this);
        spinnerProduct.setOnItemSelectedListener(this);
        spinnerModel.setOnItemSelectedListener(this);

        spinnerModel.setPrompt(getString(R.string.service_hint_model));


        Ui.setupUI(getActivity(), rootLayout);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
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
        public void onFragmentPresent(Fragment fragment, String title);

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
            //spinnerModel.setSelection(0);


        }else if(parent == spinnerModel){
            if(position == selectNoneModelSpinnerAdapter.getCount() -1){
                layoutOtherModel.setVisibility(View.VISIBLE);
            }else{
                layoutOtherModel.setVisibility(View.GONE);
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
