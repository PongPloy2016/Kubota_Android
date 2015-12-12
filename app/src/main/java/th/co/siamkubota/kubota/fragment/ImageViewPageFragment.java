package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import java.io.File;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.CameraTakeActivity;
import th.co.siamkubota.kubota.activity.ImageViewActivity;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.Converter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageViewPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewPageFragment extends Fragment {

    private static final String ARG_PARAM_DATA = "data";

    private Photo data;

    public static final String KEY_IMAGE_ID = "imageId";

    private ViewPager pager;
    private TextView textTitle;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

/*
    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }*/

    //////////////////////////////////////////////////////////////////// constructor


    public static ImageViewPageFragment newInstance(Photo photo) {
        ImageViewPageFragment fragment = new ImageViewPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_DATA, photo);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewPageFragment() {
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
        View v = inflater.inflate(R.layout.fragment_image_view, container, false);


        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        textTitle = (TextView) v.findViewById(R.id.textTitle);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());

        if(data.getPath() != null && !data.getPath().isEmpty()){
            imageView.setImageURI(Uri.fromFile(new File(this.data.getPath())));
            imageView.setVisibility(View.VISIBLE);

        }

        if(data != null && data.getTitle() != null && !data.getTitle().isEmpty()){
            textTitle.setText(data.getTitle());
        }else{
            textTitle.setVisibility(View.GONE);
        }

        if(data != null && data.getPath() != null && !data.getPath().isEmpty()){
            imageView.setImageURI(Uri.fromFile(new File(data.getPath())));
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

    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            matrix.setScale(scaleFactor, scaleFactor);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

}
