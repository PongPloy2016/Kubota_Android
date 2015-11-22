package th.co.siamkubota.kubota.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.ViewPagerAdapter;
import th.co.siamkubota.kubota.app.AppController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment implements
        View.OnClickListener,
        Step1CustomerDetailFragment.OnFragmentInteractionListener,
        Step2PhotoFragment.OnFragmentInteractionListener,
        Step3SignFragment.OnFragmentInteractionListener,
        Step4ConfirmFragment.OnFragmentInteractionListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String[] mTitle;
    private OnFragmentInteractionListener mListener;
    private AppController app;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private TextView textStepTitle;
    private ImageButton step1Button, step2Button, step3Button, step4Button;
    private Button previousButton, nextButton;
    private LinearLayout navigationControleLayout;
    private int Numboftabs;
    private CustomOnPageChangeListener pageChangeListener;


    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mTitle = getActivity().getResources().getStringArray(R.array.stage_title);
        Numboftabs = mTitle.length;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_service, container, false);
        textStepTitle = (TextView) v.findViewById(R.id.textStepTitle);
        step1Button = (ImageButton) v.findViewById(R.id.step1Button);
        step2Button = (ImageButton) v.findViewById(R.id.step2Button);
        step3Button = (ImageButton) v.findViewById(R.id.step3Button);
        step4Button = (ImageButton) v.findViewById(R.id.step4Button);
        pager = (ViewPager) v.findViewById(R.id.pager);

        previousButton = (Button) v.findViewById(R.id.previousButton);
        nextButton = (Button) v.findViewById(R.id.nextButton);
        navigationControleLayout = (LinearLayout) v.findViewById(R.id.navigationControleLayout);


        step1Button.setOnClickListener(this);
        step2Button.setOnClickListener(this);
        step3Button.setOnClickListener(this);
        step4Button.setOnClickListener(this);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        adapter = new ViewPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), mTitle,
                Numboftabs, ServiceFragment.this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(pageChangeListener = new CustomOnPageChangeListener());


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
        //public void onFragmentInteraction(Uri uri);
    }

    public void setStepTitle(String title){
        textStepTitle.setText(title);
    }

    public void hideBottomBar(){
        navigationControleLayout.animate()
                .translationY(navigationControleLayout.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        navigationControleLayout.setVisibility(View.GONE);
                    }
                });
    }

    public void showBottomBar(){
        navigationControleLayout.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        navigationControleLayout.setVisibility(View.VISIBLE);
                    }
                });
    }


    ///////////////////////////////////////////////////////////////////// implement method


    @Override
    public void onClick(View v) {
        if(v == step1Button){
            pager.setCurrentItem(0, true);
        }else if(v == step2Button){
            pager.setCurrentItem(1, true);
        }else if(v == step3Button){
            pager.setCurrentItem(2, true);
        }else if(v == step4Button){
            pager.setCurrentItem(3, true);
        }else if(v == previousButton){
            int currentPage = pageChangeListener.getCurrentPage();
            --currentPage;
            pager.setCurrentItem(currentPage = (currentPage >= 0 ? currentPage : 0));
        }else if(v == nextButton){
            int currentPage = pageChangeListener.getCurrentPage();
            ++currentPage;
            pager.setCurrentItem(currentPage = (currentPage < adapter.getCount() ? currentPage : (adapter.getCount() - 1)));
        }
    }


    @Override
    public void onFragmentPresent(Fragment fragment, String title) {

        //textStepTitle.setText(title);

        if(fragment instanceof Step1CustomerDetailFragment){

        }else if(fragment instanceof Step2PhotoFragment){

        }else if(fragment instanceof Step3SignFragment){

        }else if(fragment instanceof Step4ConfirmFragment){

        }
    }

    @Override
    public void onConfirmFragmentCancel() {
        pager.setCurrentItem((pageChangeListener.getCurrentPage() -1));
    }

    /**
     * Get the current view position from the ViewPager by
     * extending SimpleOnPageChangeListener class and adding your method
     */
    public class CustomOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {
            setStepTitle(mTitle[position]);
            currentPage = position;

            if(position == 0){
                previousButton.setEnabled(false);
            }else{
                previousButton.setEnabled(true);
            }

            if(position == 3){
                //hideBottomBar();
                navigationControleLayout.setVisibility(View.GONE);
            }else{
                if(navigationControleLayout.getVisibility() == View.GONE){
                    //showBottomBar();
                    navigationControleLayout.setVisibility(View.VISIBLE);
                }
            }
        }



        public final int getCurrentPage() {
            return currentPage;
        }
    }
}
