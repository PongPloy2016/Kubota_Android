package th.co.siamkubota.kubota.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


import java.io.File;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.ui.ScaleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewZoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageViewZoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewZoomFragment extends Fragment {

    private static final String TAG = ImageViewZoomFragment.class.getSimpleName();
    private static final String ARG_PARAM_DATA = "data";

    private Photo data;

    public static final String KEY_IMAGE_ID = "imageId";

    private ViewPager pager;
    private TextView textTitle;
    //private ImageView imageView;
    private SubsamplingScaleImageView imageView;


    public static ImageViewZoomFragment newInstance(Photo photo) {
        ImageViewZoomFragment fragment = new ImageViewZoomFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_DATA, photo);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewZoomFragment() {
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
        View v = inflater.inflate(R.layout.fragment_image_view_zoom, container, false);


        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        textTitle = (TextView) v.findViewById(R.id.textTitle);
        //imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView = (SubsamplingScaleImageView)v.findViewById(R.id.imageView);
        //imageView.setImage(ImageSource.asset("squirrel.jpg"));



        if(data != null && data.getTitle() != null && !data.getTitle().isEmpty()){
            textTitle.setText(data.getTitle());
        }else{
            textTitle.setVisibility(View.GONE);
        }

        if(data != null && data.getPath() != null && !data.getPath().isEmpty()){
            //imageView.setImageURI(Uri.fromFile(new File(data.getPath())));\
            imageView.setImage(ImageSource.uri(Uri.fromFile(new File(data.getPath()))));


        }else if(data != null && data.getServerPath() != null && !data.getServerPath().isEmpty()){

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            String imagePath = Config.mediaService + data.getServerPath();

            imageLoader.get(imagePath, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("### ", "Image Load Error: " + error.getMessage());
                    //imageView.setImageResource(R.drawable.demo_logo_product);
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        //imageView.setImageBitmap(response.getBitmap());
                    }else{
                        //imageView.setImageResource(R.drawable.demo_logo_product);
                    }
                }
            });

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


}
