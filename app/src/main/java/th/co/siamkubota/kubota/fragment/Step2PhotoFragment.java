package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Image;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.ImageViewActivity;
import th.co.siamkubota.kubota.activity.MainActivity;
import th.co.siamkubota.kubota.adapter.PhotoPagerAdapter;
import th.co.siamkubota.kubota.adapter.PhotoViewPagerAdapter;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.Copier;
import th.co.siamkubota.kubota.utils.function.ImageFile;
import th.co.siamkubota.kubota.utils.function.Validate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step2PhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step2PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2PhotoFragment extends Fragment implements
        View.OnClickListener,
PhotoPageFragment.OnFragmentInteractionListener{

    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_PHOTOS = "photos";
    private static final String KEY_EDITABLED = "editabled";

    private ViewPager pager;
    private Button button1, button2, button3, button4;
    private ImageButton previousButton, nextButton;
    private PhotoPagerAdapter adapter;
    //private PhotoViewPagerAdapter adapter;

    private CustomOnPageChangeListener pageChangeListener;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Photo> photos;
    private ArrayList<Photo> photosView;
    private ArrayList<Image> images;
    private String machineNumber;

    private FragmentManager mRetainedChildFragmentManager;

    private boolean dataComplete = false;
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

    public String getMachineNumber() {
        return machineNumber;


    }

    public void setMachineNumber(String machineNumber) {
        this.machineNumber = machineNumber;

        if(photos != null){
            photos.get(0).setDescription(machineNumber);
        }
    }

    //////////////////////////////////////////////////////////////////// constructor

    public static Step2PhotoFragment newInstance(ArrayList<Image> images, boolean editabled) {
        Step2PhotoFragment fragment = new Step2PhotoFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_TITLE, title);
        args.putParcelableArrayList(KEY_IMAGES, images);
        args.putBoolean(KEY_EDITABLED, editabled);
        fragment.setArguments(args);
        return fragment;
    }

    public Step2PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_IMAGES, images);
        outState.putParcelableArrayList(KEY_PHOTOS, photos);
        outState.putBoolean(KEY_EDITABLED, editabled);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            images = savedInstanceState.getParcelableArrayList(KEY_IMAGES);
            photos = savedInstanceState.getParcelableArrayList(KEY_PHOTOS);
            editabled = savedInstanceState.getBoolean(KEY_EDITABLED);

            mListener = (Step2PhotoFragment.OnFragmentInteractionListener) getParentFragment();

            FragmentManager cfManager = getChildFragmentManager();
            adapter = new PhotoPagerAdapter(getActivity(), cfManager, photos , Step2PhotoFragment.this, editabled);
            pageChangeListener = new CustomOnPageChangeListener();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setRetainInstance(true);

        if(savedInstanceState == null ){
            if (getArguments() != null) {
                //title = getArguments().getString(ARG_PARAM_TITLE);
                images = getArguments().getParcelableArrayList(KEY_IMAGES);
                editabled =  getArguments().getBoolean(KEY_EDITABLED);
            }

            photos = new ArrayList<Photo>();
            photos.add(new Photo(1,getString(R.string.service_image_1_engine_number)));
            photos.add(new Photo(2,getString(R.string.service_image_2_working_hours)));
            photos.add(new Photo(3,getString(R.string.service_image_3_machine)));
            photos.add(new Photo(4,getString(R.string.service_image_4_customer_with_machine)));

            if(Config.showDefault){

                String pathStr1 = ImageFile.createMockupImage(getActivity(), "photo3.jpg", R.drawable.photo3);

                for(Photo p : photos){
                    p.setPath(pathStr1);
                    p.setComplete(true);
                }

            }

            if(images == null){
                images = new ArrayList<Image>();
            }else if(images.size()> 0){


                int i = 0;
                for(Image img : images){

                    if(img.getImagePath() != null  && !img.getImagePath().isEmpty()){
                        photos.get(i).setPath(img.getImagePath());
                        photos.get(i).setDate(img.getCapturedAt());
                        photos.get(i).setComplete(true);
                    }
                    i++;
                }

                if(images.size() == 4){
                    dataComplete = true;
                }

            }

            FragmentManager cfManager = getChildFragmentManager();
            adapter = new PhotoPagerAdapter(getActivity(), cfManager, photos , Step2PhotoFragment.this, editabled);
            pageChangeListener = new CustomOnPageChangeListener();

            //validateInput();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_step2_photo, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        button1 = (Button) v.findViewById(R.id.button1);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        button4 = (Button) v.findViewById(R.id.button4);
        pager = (ViewPager) v.findViewById(R.id.pagerPhoto);

        try{
            previousButton = (ImageButton) v.findViewById(R.id.previousButton);
            nextButton = (ImageButton) v.findViewById(R.id.nextButton);

            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            button4.setOnClickListener(this);

            previousButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);

            mListener = (Step2PhotoFragment.OnFragmentInteractionListener) getParentFragment();

            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(4);
            pager.addOnPageChangeListener(pageChangeListener);

            pager.setCurrentItem(0);

            if( pager == null){

            }
            else {
                setSelectPhoto();
            }

            validateInput();

        }catch (NullPointerException e){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setSelectPhoto(){
        //pager.requestLayout();
        pager.setCurrentItem(pageChangeListener.getCurrentPage());
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
       // mListener = null;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
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
        public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data);
        public void onFragmentSaveInstanceState(Fragment fragment);
    }


    //////////////////////////////////////////////////////////// implement method

    @Override
    public void onClick(View v) {
        if(v == button1){
            pager.setCurrentItem(0, true);
        }else if(v == button2){
            pager.setCurrentItem(1, true);
        }else if(v == button3){
            pager.setCurrentItem(2, true);
        }else if(v == button4){
            pager.setCurrentItem(3, true);
        }else if(v == previousButton){
            int currentPage = pageChangeListener.getCurrentPage();
            --currentPage;

            if(currentPage < 0){
                currentPage = adapter.getCount() -1;
            }
            pager.setCurrentItem(currentPage);


        }else if(v == nextButton){
            int currentPage = pageChangeListener.getCurrentPage();
            ++currentPage;

            if(currentPage > adapter.getCount() -1){
                currentPage = 0;
            }
            pager.setCurrentItem(currentPage);
        }
    }


    @Override
    public void onPhotoTaken(Fragment fragment, Photo data) {
        for(Photo photo : photos){
            if(photo.getId() == data.getId()){
                photo.setServerPath(null);
                Copier.copy(data, photo);
                break;
            }
        }

        validateInput();
    }

    @Override
    public void onPhotoView(Fragment fragment, Photo data) {

        ArrayList<Photo> images = new ArrayList<>();
        images.clear();

        for(Photo photo : photos){
            if(photo.isComplete()){
                images.add(photo);
            }
        }

        int item = images.indexOf(data);

        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ImageViewActivity.KEY_IMAGE_LIST, images);
        bundle.putInt(ImageViewActivity.KEY_SELECT_ITEM, item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Get the current view position from the ViewPager by
     * extending SimpleOnPageChangeListener class and adding your method
     */
    public class CustomOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            currentPage = position;

            if(position == 0){
                //previousButton.setEnabled(false);
            }else{
                //previousButton.setEnabled(true);
            }

            button1.setTextColor(ContextCompat.getColor(getActivity(),R.color.button_text_gray_selector));
            button2.setTextColor(ContextCompat.getColor(getActivity(),R.color.button_text_gray_selector));
            button3.setTextColor(ContextCompat.getColor(getActivity(),R.color.button_text_gray_selector));
            button4.setTextColor(ContextCompat.getColor(getActivity(),R.color.button_text_gray_selector));

            switch (position){
                case 0 :
                    //button1.requestFocus();
                    button1.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    SpannableString content = new SpannableString(button1.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    button1.setText(content);
                    break;
                case 1 :
                    //button2.requestFocus();
                    button2.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
                case 2 :
                    //button3.requestFocus();
                    button3.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
                case 3 :
                    //button4.requestFocus();
                    button4.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
            }
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    private void validateInput(){

        images.clear();

        for(Photo photo : photos){
            if(!photo.isComplete()){
                dataComplete = false;
                mListener.onFragmentDataComplete(this, dataComplete, images);

                if(Config.showDefault == true){
                    break;
                }else{
                    return;
                }

            }else{
                if(photo.getServerPath() != null && !photo.getServerPath().isEmpty()){
                    images.add(new Image(null, photo.getDate(), photo.getServerPath()));
                }else{
                    images.add(new Image(photo.getPath(), photo.getDate()));
                }
            }
        }

/*
        images.clear();

        for(int i = 0 ; i < photos.size() ; i++){

            if(photos.get(i).getServerPath() != null && !photos.get(i).getServerPath().isEmpty()){
                images.add(new Image(null, photos.get(i).getDate(), photos.get(i).getServerPath()));
            }else{
                images.add(new Image(photos.get(i).getPath(), photos.get(i).getDate()));
            }

        }*/


        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete, images);

     /*   try {

            dataComplete = true;
            mListener.onFragmentDataComplete(this, dataComplete, images);

        }catch (NullPointerException e){

            if(mListener == null){
                if(getActivity() != null){
                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    if(fm.findFragmentByTag("serviceFragment") != null){
                        mListener = (Step2PhotoFragment.OnFragmentInteractionListener) fm.findFragmentByTag("serviceFragment");

                        mListener.onFragmentDataComplete(this, dataComplete, images);
                    }

                }
            }

        }*/

    }
}
