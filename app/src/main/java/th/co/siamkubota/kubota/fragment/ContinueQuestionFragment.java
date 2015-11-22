package th.co.siamkubota.kubota.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.ResultActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContinueQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContinueQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContinueQuestionFragment extends Fragment implements
View.OnClickListener{

    private static final String ARG_PARAM_TITLE = "title";

    private Button yesButton;
    private Button noButton;

    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    //////////////////////////////////////////////////////////////////// constructor


    public static ContinueQuestionFragment newInstance() {
        ContinueQuestionFragment fragment = new ContinueQuestionFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public ContinueQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //title = getArguments().getString(ARG_PARAM_TITLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_continue_question, container, false);
        yesButton = (Button) v.findViewById(R.id.yesButton);
        noButton = (Button) v.findViewById(R.id.noButton);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);


        //mListener.onFragmentPresent(this, title);

        return v;
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
        public void onConfirmContinue(boolean result);
    }

    ////////////////////////////////////////////////////////////// implement method


    @Override
    public void onClick(View v) {
        if(v == yesButton){
           mListener.onConfirmContinue(true);
        }else{
            mListener.onConfirmContinue(false);
        }
    }
}
