package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.PhotoPagerAdapter;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.ImageFile;

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

    private static final String ARG_PARAM_TITLE = "title";

    private ViewPager pager;
    private Button button1, button2, button3, button4;
    private ImageButton previousButton, nextButton;
    private PhotoPagerAdapter adapter;

    private CustomOnPageChangeListener pageChangeListener;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Photo> photos;

    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    //////////////////////////////////////////////////////////////////// constructor

    public static Step2PhotoFragment newInstance(String title) {
        Step2PhotoFragment fragment = new Step2PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public Step2PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            //title = getArguments().getString(ARG_PARAM_TITLE);
        }

        photos = new ArrayList<Photo>();

        photos.add(new Photo(getString(R.string.service_image_1_engine_number), "K12345678"));
        photos.add(new Photo(getString(R.string.service_image_2_working_hours)));
        photos.add(new Photo(getString(R.string.service_image_3_machine)));
        photos.add(new Photo(getString(R.string.service_image_4_customer_with_machine)));

        adapter = new PhotoPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), photos , Step2PhotoFragment.this);
        pageChangeListener = new CustomOnPageChangeListener();
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

        previousButton = (ImageButton) v.findViewById(R.id.previousButton);
        nextButton = (ImageButton) v.findViewById(R.id.nextButton);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);


        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(pageChangeListener);

        pager.setCurrentItem(0);

        setSelectPhoto();

    }

    @Override
    public void onResume() {
        super.onResume();


        /*pager.setAdapter(adapter);
        pager.addOnPageChangeListener(pageChangeListener);

        pager.setCurrentItem(0);

        setSelectPhoto();*/


    }

    public void setSelectPhoto(){
        pager.requestLayout();
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
    public void onFragmentPresent(Fragment fragment, String title) {

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
                previousButton.setEnabled(false);
            }else{
                previousButton.setEnabled(true);
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
}
