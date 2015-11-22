package th.co.siamkubota.kubota.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.model.Photo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoPageFragment extends Fragment {

    private static final String ARG_PARAM_DATA = "data";

    private Photo data;

    private RelativeLayout rootLayout;
    private Button cameraButton;

    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    //////////////////////////////////////////////////////////////////// constructor


    public static PhotoPageFragment newInstance(Photo photo) {
        PhotoPageFragment fragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_DATA, photo);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = getArguments().getParcelable(ARG_PARAM_DATA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.item_page_photo, container, false);

        rootLayout = (RelativeLayout) v.findViewById(R.id.rootLayout);
        cameraButton = (Button) v.findViewById(R.id.cameraButton);
        cameraButton.setText(data.getTitle());

        //ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //rootLayout.setLayoutParams(params);

        //mListener.onFragmentPresent(this, title);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

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
        public void onFragmentPresent(Fragment fragment, String title);
    }

}
