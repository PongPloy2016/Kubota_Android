package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step3SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step3SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3SignFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM_TITLE = "title";

    private SelectableRoundedImageView imageCustomerSignature;
    private SelectableRoundedImageView imageTechnicianSignature;
    private EditText editTextCustomerSignDate;
    private EditText editTextTechnicianSignDate;
    private LinearLayout signatureCustomerHintLayout;
    private LinearLayout signatureTechnicianHintLayout;

    private String title;
    private Photo imageCustomer;
    private Photo imageTechnician;


    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
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
        setRetainInstance(true);
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

        imageCustomerSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageCustomerSignature);
        imageTechnicianSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageTechnicianSignature);
        editTextCustomerSignDate = (EditText) v.findViewById(R.id.editTextCustomerSignDate);
        editTextTechnicianSignDate = (EditText) v.findViewById(R.id.editTextTechnicianSignDate);
        signatureCustomerHintLayout = (LinearLayout) v.findViewById(R.id.signatureCustomerHintLayout);
        signatureTechnicianHintLayout = (LinearLayout) v.findViewById(R.id.signatureTechnicianHintLayout);

        imageCustomerSignature.setOnClickListener(this);
        imageTechnicianSignature.setOnClickListener(this);

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
        public void onInvokeSignPad();
    }

    @Override
    public void onClick(View v) {

        if(v == imageCustomerSignature || v == imageTechnicianSignature ){
            //mListener.onInvokeSignPad();
            int requestCode = 0;
            Bundle bundle = new Bundle();

            if(v == imageCustomerSignature){

                requestCode = 0;
                if(imageCustomer == null){
                    imageCustomer = new Photo(getString(R.string.sign_pad_customer_signature));
                }

                if(imageCustomer != null && imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageCustomer.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageCustomer.getTitle());

            }else if(v == imageTechnicianSignature){
                requestCode = 1;

                if(imageTechnician == null){
                    imageTechnician = new Photo(getString(R.string.sign_pad_technician_signature));
                }

                if(imageTechnician != null && imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageTechnician.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageTechnician.getTitle());
            }


            Intent intent = new Intent(getActivity(), SignaturePadActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, requestCode);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            Bundle bundle = data.getExtras();

            String imagePath = bundle.getString("imagePath");
            String dateInfo = bundle.getString("takenDate");
            //imageView.setTag(R.id.imagePath, imagePath);

            if(requestCode == 0){

                imageCustomer.setPath(imagePath);
                imageCustomer.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageCustomerSignature.setImageURI(Uri.fromFile(new File(imageCustomer.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextCustomerSignDate.setText(dateInfo);
                signatureCustomerHintLayout.setVisibility(View.GONE);

            }else if(requestCode == 1){
                imageTechnician.setPath(imagePath);
                imageTechnician.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageTechnicianSignature.setImageURI(Uri.fromFile(new File(imageTechnician.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextTechnicianSignDate.setText(dateInfo);
                signatureTechnicianHintLayout.setVisibility(View.GONE);
            }

        }
    }
}
