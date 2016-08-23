package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.MainActivity;
import th.co.siamkubota.kubota.activity.ResultActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step4ConfirmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step4ConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step5ConfirmFragment extends Fragment implements
        View.OnClickListener{

    private static final String ARG_PARAM_TITLE = "title";
    private static final String ARG_PARAM_COMPLETE = "complete";
    private static final String KEY_EDITABLED = "EDITABLED";

    private Button confirmButton;
    private Button cancelButton;

    private String title;
    private boolean dataComplete = false;
    private boolean complete = false;


    private OnFragmentInteractionListener mListener;

    private boolean editabled = true;


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


    public static Step5ConfirmFragment newInstance(boolean complete, boolean editabled) {
        Step5ConfirmFragment fragment = new Step5ConfirmFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_COMPLETE,complete);
        args.putBoolean(KEY_EDITABLED, editabled);
        fragment.setArguments(args);
        return fragment;
    }

    public Step5ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mListener.onFragmentSaveInstanceState(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){

        }

        mListener = (Step5ConfirmFragment.OnFragmentInteractionListener) getParentFragment();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            complete = getArguments().getBoolean(ARG_PARAM_COMPLETE, false);
            editabled = getArguments().getBoolean(KEY_EDITABLED);

            setDataComplete(complete);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_step4_confirm, container, false);


        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        if(savedInstanceState == null){
            confirmButton = (Button) v.findViewById(R.id.confirmButton);
            cancelButton = (Button) v.findViewById(R.id.cancelButton);

            confirmButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            try {
                mListener = (Step5ConfirmFragment.OnFragmentInteractionListener) getParentFragment();

                if(complete){
                    mListener.onFragmentDataComplete(this, dataComplete, null);
                    //setDataComplete(complete);
                }
            }catch (NullPointerException e){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

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
        //public void onConfirmFragmentCancel();
        public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data);
        public void onConfirmSubmit(Fragment fragment, boolean complete);
        public void onFragmentSaveInstanceState(Fragment fragment);
    }

    ////////////////////////////////////////////////////////////// implement method


    @Override
    public void onClick(View v) {

        if(v == confirmButton){

            mListener.onConfirmSubmit(this, true);

        }else{

            mListener.onConfirmSubmit(this, false);

        }

    }
}
