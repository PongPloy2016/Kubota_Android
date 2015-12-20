package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import java.io.File;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.SignaturePadActivity;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.Validate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step3SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step3SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3SignFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{

    private static final String ARG_PARAM_TITLE = "title";

    private LinearLayout rootLayout;
    private SelectableRoundedImageView imageCustomerSignature;
    private SelectableRoundedImageView imageTechnicianSignature;
    private EditText editTextCustomerSignDate;
    private EditText editTextTechnicianSignDate;
    private EditText editTextCustomerName;
    private EditText editTextTechnicianName;
    private LinearLayout signatureCustomerHintLayout;
    private LinearLayout signatureTechnicianHintLayout;
    private CheckBox checkBoxUserAccept;
    private CheckBox checkBoxTecnicianAccept;

    private String title;
    private Photo imageCustomer;
    private Photo imageTechnician;
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



    public static Step3SignFragment newInstance(String title) {
        Step3SignFragment fragment = new Step3SignFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public Step3SignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM_TITLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step3_sign, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
        imageCustomerSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageCustomerSignature);
        imageTechnicianSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageTechnicianSignature);
        editTextCustomerSignDate = (EditText) v.findViewById(R.id.editTextCustomerSignDate);
        editTextTechnicianSignDate = (EditText) v.findViewById(R.id.editTextTechnicianSignDate);
        editTextCustomerName = (EditText) v.findViewById(R.id.editTextCustomerName);
        editTextTechnicianName = (EditText) v.findViewById(R.id.editTextTechnicianName);
        signatureCustomerHintLayout = (LinearLayout) v.findViewById(R.id.signatureCustomerHintLayout);
        signatureTechnicianHintLayout = (LinearLayout) v.findViewById(R.id.signatureTechnicianHintLayout);
        checkBoxUserAccept = (CheckBox) v.findViewById(R.id.checkBoxUserAccept);
        checkBoxTecnicianAccept = (CheckBox) v.findViewById(R.id.checkBoxTecnicianAccept);


        if(imageCustomer != null && imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
            imageCustomerSignature.setImageURI(Uri.fromFile(new File(imageCustomer.getPath())));
            editTextCustomerSignDate.setText(Converter.DateToString(imageCustomer.getDate(),"dd/MM/yyyy"));
            signatureCustomerHintLayout.setVisibility(View.GONE);
        }

        if(imageTechnician != null && imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
            imageTechnicianSignature.setImageURI(Uri.fromFile(new File(imageTechnician.getPath())));
            editTextTechnicianSignDate.setText(Converter.DateToString(imageTechnician.getDate(),"dd/MM/yyyy"));
            signatureTechnicianHintLayout.setVisibility(View.GONE);
        }

        setDataChangeListener();

    }

    private void setDataChangeListener(){

        imageCustomerSignature.setOnClickListener(this);
        imageTechnicianSignature.setOnClickListener(this);

        editTextCustomerSignDate.addTextChangedListener(new GenericTextWatcher(editTextCustomerSignDate));
        editTextTechnicianSignDate.addTextChangedListener(new GenericTextWatcher(editTextTechnicianSignDate));
        editTextCustomerName.addTextChangedListener(new GenericTextWatcher(editTextCustomerName));
        editTextTechnicianName.addTextChangedListener(new GenericTextWatcher(editTextTechnicianName));

        checkBoxUserAccept.setOnCheckedChangeListener(this);
        checkBoxTecnicianAccept.setOnCheckedChangeListener(this);
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
        //public void onFragmentPresent(Fragment fragment, String title);
        //public void onInvokeSignPad();
        public void onFragmentDataComplete(Fragment fragment, boolean complete);
    }


    /////////////////////////////////////////////////////////////////////// implement method

    @Override
    public void onClick(View v) {

        if(v == imageCustomerSignature || v == imageTechnicianSignature ){
            //mListener.onInvokeSignPad();
            int requestCode = 0;
            Bundle bundle = new Bundle();

            if(v == imageCustomerSignature){


                if(imageCustomer == null){
                    imageCustomer = new Photo(5,getString(R.string.sign_pad_customer_signature));
                }
                requestCode = imageCustomer.getId();

                if(imageCustomer != null && imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageCustomer.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageCustomer.getTitle());

            }else if(v == imageTechnicianSignature){
                //requestCode = 1;

                if(imageTechnician == null){
                    imageTechnician = new Photo(6,getString(R.string.sign_pad_technician_signature));
                }

                requestCode = imageTechnician.getId();

                if(imageTechnician != null && imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageTechnician.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageTechnician.getTitle());
            }


            Intent intent = new Intent(getActivity(), SignaturePadActivity.class);
            intent.putExtras(bundle);
            //startActivityForResult(intent, requestCode);
            getActivity().startActivityForResult(intent, requestCode);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == checkBoxUserAccept){

        }else if(buttonView == checkBoxTecnicianAccept){

        }

        validateInput();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            Bundle bundle = data.getExtras();

            String imagePath = bundle.getString("imagePath");
            String dateInfo = bundle.getString("takenDate");
            //imageView.setTag(R.id.imagePath, imagePath);

            if(imageCustomer != null && requestCode == imageCustomer.getId()){

                imageCustomer.setPath(imagePath);
                imageCustomer.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageCustomerSignature.setImageURI(Uri.fromFile(new File(imageCustomer.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextCustomerSignDate.setText(dateInfo);
                signatureCustomerHintLayout.setVisibility(View.GONE);

                imageCustomer.setComplete(true);
                validateInput();

            }else if(imageTechnician != null && requestCode == imageTechnician.getId()){
                imageTechnician.setPath(imagePath);
                imageTechnician.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageTechnicianSignature.setImageURI(Uri.fromFile(new File(imageTechnician.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextTechnicianSignDate.setText(dateInfo);
                signatureTechnicianHintLayout.setVisibility(View.GONE);

                imageTechnician.setComplete(true);
                validateInput();
            }

        }
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

        if((imageCustomer == null || (imageCustomer != null && !imageCustomer.isComplete())) ||
                (imageTechnician == null || (imageTechnician != null && !imageTechnician.isComplete())) ){
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete);
            return;
        }

        View view = Validate.inputValidate(rootLayout, "required");
        if(view != null ){
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete);
            return;
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete);
    }
}
