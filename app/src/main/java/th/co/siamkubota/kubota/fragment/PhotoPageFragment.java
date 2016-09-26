package th.co.siamkubota.kubota.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.joooonho.SelectableRoundedImageView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.CameraTakeActivity;
import th.co.siamkubota.kubota.activity.ImageViewActivity;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.logger.Logger;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.ImageFile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoPageFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM_DATA = "data";
    private static final String ARG_PARAM_EDITABLED = "editable";
    private CircleProgressBar progress2 ;
    private Photo data;
    private Timer timer;
    private RelativeLayout rootLayout;
    private Button cameraButton;
    private SelectableRoundedImageView imageView;
    private TextView textDate;
    private ImageButton imageZoomButton;

    private OnFragmentInteractionListener mListener;

    private boolean editabled;
    int progress = 0;
    private Handler handler;

    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    //////////////////////////////////////////////////////////////////// constructor


    public static PhotoPageFragment newInstance(Photo photo, boolean editabled) {
        PhotoPageFragment fragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_DATA, photo);
        args.putBoolean(ARG_PARAM_EDITABLED, editabled);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_PARAM_DATA, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_PARAM_DATA)){
            data = savedInstanceState.getParcelable(ARG_PARAM_DATA);
        }





    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            if (getArguments() != null) {
                data = getArguments().getParcelable(ARG_PARAM_DATA);
                editabled = getArguments().getBoolean(ARG_PARAM_EDITABLED);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_page_photo, container, false);
/*
        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_PARAM_DATA)) {
            data = savedInstanceState.getParcelable(ARG_PARAM_DATA);
        }*/
        progress2 = (CircleProgressBar) v.findViewById(R.id.progressBar);


//        handler = new Handler();
//        for (int i = 0; i < 10; i++) {
//            final int finalI = i;
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(finalI *10>=90){
//
//                        progress2.setVisibility(View.INVISIBLE);
//                    }else {
//                        progress2.setProgress(finalI * 10);
//                    }
//                }
//            },1000*(i+1));
//        }

        return v;
    }



    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        if(savedInstanceState == null){

            rootLayout = (RelativeLayout) v.findViewById(R.id.rootLayout);
            cameraButton = (Button) v.findViewById(R.id.cameraButton);
            imageView = (SelectableRoundedImageView) v.findViewById(R.id.imageView);
            textDate = (TextView) v.findViewById(R.id.textDate);
            imageZoomButton = (ImageButton) v.findViewById(R.id.imageZoomButton);


            cameraButton.setText(data.getTitle());
            if(editabled){
                cameraButton.setOnClickListener(this);
                imageView.setOnClickListener(this);
            }

            imageZoomButton.setOnClickListener(this);

        /*    if(savedInstanceState != null && savedInstanceState.containsKey(ARG_PARAM_DATA)) {
                data = savedInstanceState.getParcelable(ARG_PARAM_DATA);
            }
    */

            imageView.setVisibility(View.VISIBLE);
            textDate.setText(Converter.DateToString(this.data.getDate(), "dd/MM/yyyy"));

            if(data.getPath() != null && !data.getPath().isEmpty()){
              //  imageView.setImageURI(Uri.fromFile(new File(this.data.getPath())));
                Glide.with(getContext()).load(Uri.fromFile(new File(this.data.getPath()))).into(imageView);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                imageView.requestLayout();
                imageView.invalidate();

            }else if(data.getServerPath() != null && !data.getServerPath().isEmpty()){

                ImageLoader imageLoader = AppController.getInstance().getImageLoader();

                String imagePath = Config.mediaService + data.getServerPath();

                Logger.Log("imagePath getServerPath",imagePath);

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
                            imageView.setImageBitmap(response.getBitmap());

                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                            imageView.setLayoutParams(params);
                            imageView.requestLayout();
                            imageView.invalidate();

                        }else{
                            //imageView.setImageResource(R.drawable.demo_logo_product);
                        }
                    }
                });
            }

        }

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

    @Override
    public void onClick(View v) {

        if(v == cameraButton || v == imageView){

            Logger.Log("Onclick","Omclick");
            Intent intent = new Intent(getActivity(), CameraTakeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(CameraTakeActivity.KEY_COMMAND, CameraTakeActivity.COMMAND_REQUEST_CAMERA); //ส่ง COMMAND_REQUEST_CAMERA
            //bundle.putInt(CameraTakeActivity.KEY_GROUP_CODE, -1);
            if(data.getDescription() != null && !data.getDescription().isEmpty()){
                bundle.putString(CameraTakeActivity.KEY_RENDER_TEXT, data.getDescription());
            }
            intent.putExtras(bundle);
            //getParentFragment().startActivityForResult(intent, data.getId());
            getActivity().startActivityForResult(intent, data.getId());  //return data

        }else if(v == imageZoomButton){

            if(data != null && ( (data.getPath() != null && !data.getPath().isEmpty())
                    || (data.getServerPath() != null && !data.getServerPath().isEmpty())
            )){

                mListener.onPhotoView(this, data);

                Logger.Log("data_image_Zoom","data_image_Zoom");
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.Log(" onActivityResult load", String.valueOf(requestCode));

        if(resultCode == Activity.RESULT_OK && requestCode == this.data.getId()){

            Bundle bundle = data.getExtras();

            Logger.Log("onActivityResult bundle_pic", String.valueOf(bundle));

            String imagePath = bundle.getString("imagePath");
            String dateInfo = bundle.getString("takenDate");

            Logger.Log("onActivityResult imagePath", imagePath);
            Logger.Log("onActivityResult dateInfo", dateInfo);

            //imageView.setTag(R.id.imagePath, imagePath);

            if(this.data.getPath() != null && !this.data.getPath().isEmpty()){
                ImageFile.deleteFile(this.data.getPath());
                Logger.Log("deletePic", this.data.getPath());
            }

            this.data.setServerPath(null);

//            if(this.data.getPath() != null && !this.data.getPath().isEmpty()){
//                ImageFile.deleteFile(this.data.getPath());
//            }

            this.data.setPath(imagePath);
            this.data.setDate(Converter.StringToDate(dateInfo, "yyyy-MM-dd HH:mm:ss"));


//            imageView.setVisibility(View.VISIBLE);
              // imageView.setImageURI(Uri.fromFile(new File(this.data.getPath())));
         //  imageView.setImageURI(Uri.fromFile(new File(this.data.getPath())));

        //   Glide.with(this.getContext()).load(Uri.fromFile(new File(this.data.getPath()))).into(imageView);
           // Glide.with(getContext()).load(Uri.fromFile(new File(PhotoPageFragment.this.data.getPath()))).into(imageView);
            onLoadImage(imagePath);
           // Logger.Log("url_path_image", String.valueOf(Uri.fromFile(new File(this.data.getPath()))));

//
           textDate.setText(Converter.DateToString(this.data.getDate(), "dd/MM/yyyy"));

            this.data.setComplete(true);
            mListener.onPhotoTaken(this, this.data);

        }
    }

    private void onLoadImage(String imagePath) {
        ImageSize targetSize = new ImageSize(imageView.getWidth(), imageView.getHeight()); // result Bitmap will be fit to this size
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        imageLoader.loadImage("file:///"+imagePath, targetSize, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                Logger.Log("onLoadingStarted",imageUri);
               progress2.setVisibility(View.VISIBLE);


                new Handler().post(new Runnable() {
                    public void run() {

                        progress2.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);

                        Glide.with(getContext()).load(Uri.fromFile(new File(PhotoPageFragment.this.data.getPath()))).into(imageView);

                    }
                });



            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
             //   imageView.setVisibility(View.VISIBLE);
                progress2.setVisibility(View.INVISIBLE);
                Logger.Log("onLoadingComplete","onLoadingComplete");

              //  setLoagingCompileImage();

            }


        });
    }


    private void setLoagingCompileImage() {


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
        //   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  width,  height,1.0f);
        imageView.setLayoutParams(layoutParams);
        imageView.setVisibility(View.VISIBLE);

//                DisplayMetrics displaymetrics = new DisplayMetrics();
//
//                int height = displaymetrics.heightPixels;
//                int width = displaymetrics.widthPixels;
//                android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) imageNewsDetails.getLayoutParams();
//                params.width = width;
//                params.height=height;
//                params.setMargins(0, 0, 0, 0);
    }



//    public void ImageLoader(String imageq){}
//    {
//
//
//        ImageSize targetSize = new ImageSize(80, 50); // result Bitmap will be fit to this size
//        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
//        imageLoader.loadImage(imageq, targetSize, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                // Do whatever you want with Bitmap
//
//                setLoagingCompileImage();
//
//            }
//        });
//    }

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
        public void onPhotoTaken(Fragment fragment, Photo data);
        public void onPhotoView(Fragment fragment, Photo data);
    }

}
