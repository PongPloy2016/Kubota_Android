package th.co.siamkubota.kubota.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Image;
import io.swagger.client.model.LoginData;
import io.swagger.client.model.Signature;
import io.swagger.client.model.Task;
import io.swagger.client.model.TaskInfo;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.LoginActivity;
import th.co.siamkubota.kubota.activity.ResultActivity;
import th.co.siamkubota.kubota.adapter.ViewPagerAdapter;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.utils.function.Ui;
import th.co.siamkubota.kubota.utils.ui.NoneScrollableViewPager;

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
    private static final String KEY_TASK = "TASK";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String[] mTitle;
    private OnFragmentInteractionListener mListener;
    private AppController app;

    private LinearLayout rootLayout;
    private NoneScrollableViewPager pager;
    private ViewPagerAdapter adapter;
    private TextView textStepTitle;
    private RelativeLayout backStep1, backStep2, backStep3, backStep4;
    private ImageButton step1Button, step2Button, step3Button, step4Button;
    private ImageView divStep1, divStep2, divStep3, divStep4;
    private Button previousButton, nextButton;
    private LinearLayout navigationControleLayout;
    private int Numboftabs;
    private CustomOnPageChangeListener pageChangeListener;

    private LoginData loginData;
    private Task task;


    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public static ServiceFragment newInstance(LoginData loginData, Task task) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(LoginActivity.KEY_LOGIN_DATA, loginData);
        args.putParcelable(ServiceFragment.KEY_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            loginData = getArguments().getParcelable(LoginActivity.KEY_LOGIN_DATA);
            task = getArguments().getParcelable(ServiceFragment.KEY_TASK);
        }

        mTitle = getActivity().getResources().getStringArray(R.array.stage_title);
        Numboftabs = mTitle.length;

        if(task == null){
            task = new Task();
            task.setTaskInfo(new TaskInfo());
            task.setTaskImages(new ArrayList<Image>());
            task.setSignature(new Signature());

            ArrayList<Boolean> answers = new ArrayList<Boolean>();
            answers.add(true);
            answers.add(false);
            task.setAnswers(answers);
        }else{

        }


        /*adapter = new ViewPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), mTitle,
                Numboftabs, ServiceFragment.this);*/
        FragmentManager cfManager = getChildFragmentManager();
        adapter = new ViewPagerAdapter(getActivity(), cfManager, mTitle,
                Numboftabs, ServiceFragment.this, task);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_service, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
        textStepTitle = (TextView) v.findViewById(R.id.textStepTitle);
        backStep1 = (RelativeLayout) v.findViewById(R.id.backStep1);
        backStep2 = (RelativeLayout) v.findViewById(R.id.backStep2);
        backStep3 = (RelativeLayout) v.findViewById(R.id.backStep3);
        backStep4 = (RelativeLayout) v.findViewById(R.id.backStep4);
        step1Button = (ImageButton) v.findViewById(R.id.step1Button);
        step2Button = (ImageButton) v.findViewById(R.id.step2Button);
        step3Button = (ImageButton) v.findViewById(R.id.step3Button);
        step4Button = (ImageButton) v.findViewById(R.id.step4Button);
        divStep1 = (ImageView) v.findViewById(R.id.divStep1);
        divStep2 = (ImageView) v.findViewById(R.id.divStep2);
        divStep3 = (ImageView) v.findViewById(R.id.divStep3);
        pager = (NoneScrollableViewPager) v.findViewById(R.id.pager);

        previousButton = (Button) v.findViewById(R.id.previousButton);
        nextButton = (Button) v.findViewById(R.id.nextButton);
        navigationControleLayout = (LinearLayout) v.findViewById(R.id.navigationControleLayout);


//        step1Button.setOnClickListener(this);
//        step2Button.setOnClickListener(this);
//        step3Button.setOnClickListener(this);
//        step4Button.setOnClickListener(this);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        nextButton.setEnabled(false);

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(4);
        pager.addOnPageChangeListener(pageChangeListener = new CustomOnPageChangeListener());
        pager.setPagingEnabled(false);

        Ui.setupUI(getActivity(), rootLayout);

        if(task.getComplete() != null && task.getComplete()){
            setStepComplete(1, true);
            setStepComplete(2, true);
            setStepComplete(3, true);
            //setStepComplete(4, true);
            pager.setCurrentItem(adapter.getCount() -1);

        }else{

        }

    }

    private void setStepComplete(int step, boolean complete){

        switch (step){
            case 1:

                if(complete){
                    backStep1.setBackgroundResource(R.drawable.rectangle_left_round_corner_orange);
                    divStep1.setImageResource(R.drawable.edge_orage);
                    step1Button.setImageResource(R.drawable.white_number01);
                }else{
                    backStep1.setBackgroundResource(R.drawable.rectangle_left_round_corner_white);
                    divStep1.setImageResource(R.drawable.edge_white);
                    step1Button.setImageResource(R.drawable.deepgrey_number01);
                }

                break;
            case 2:

                if(complete){
                    backStep2.setBackgroundResource(R.color.orange_stage);
                    divStep2.setImageResource(R.drawable.edge_orage);
                    step2Button.setImageResource(R.drawable.white_number02);
                }else{
                    backStep2.setBackgroundResource(R.color.white);
                    divStep2.setImageResource(R.drawable.edge_white);
                    step2Button.setImageResource(R.drawable.deepgrey_number02);
                }

                break;
            case 3:

                if(complete){
                    backStep3.setBackgroundResource(R.color.orange_stage);
                    divStep3.setImageResource(R.drawable.edge_orage);
                    step3Button.setImageResource(R.drawable.white_number03);
                }else{

                    if(pageChangeListener.getCurrentPage() == 2){
                        backStep3.setBackgroundResource(R.color.white);
                        divStep3.setImageResource(R.drawable.edge_white);
                        step3Button.setImageResource(R.drawable.deepgrey_number03);
                    }else{
                        backStep3.setBackgroundResource(R.color.light_gray_stage);
                        divStep3.setImageResource(R.drawable.edge_grey);
                        step3Button.setImageResource(R.drawable.lightgrey_number03);
                    }

                }

                break;
            case 4:
            default:

                if(complete){
                    step4Button.setImageResource(R.drawable.white_number04);
                }else{
                    backStep4.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                    step4Button.setImageResource(R.drawable.deepgrey_number04);
                }

                break;
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
//        public void onRelayInvokeSignPad();
//        public void onRelayRequestAddress(Fragment fragment);
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
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



    private void showToastComplete(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_data_complete_layout,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 480);
        toast.setDuration(Toast.LENGTH_SHORT);

        //TextView  textView = (TextView) layout.findViewById(R.id.textView1);
        //textView.setText(getString(R.string.sign_pad_not_complete));

        toast.setView(layout);
        toast.show();
    }


    @Override
    public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data) {

        if(complete){
            showToastComplete();
        }

        if(fragment instanceof Step1CustomerDetailFragment){

            setStepComplete(1, complete);

            if(complete){
                /*backStep1.setBackgroundResource(R.drawable.rectangle_left_round_corner_orange);
                divStep1.setImageResource(R.drawable.edge_orage);
                step1Button.setImageResource(R.drawable.white_number01);*/

                setStepComplete(1, complete);

                nextButton.setEnabled(true);
                task.setTaskInfo((TaskInfo) data);
                task.getTaskInfo().setEngineerID(loginData.getUserId());

                Step2PhotoFragment step2PhotoFragmentadapter = (Step2PhotoFragment)adapter.getItem(1);
                step2PhotoFragmentadapter.setMachineNumber(task.getTaskInfo().getEngineNo());

               /* if(pager.getCurrentItem() != 0){
                    pager.setCurrentItem(0);
                }*/


            }else{
               /* backStep1.setBackgroundResource(R.drawable.rectangle_left_round_corner_white);
                divStep1.setImageResource(R.drawable.edge_white);
                step1Button.setImageResource(R.drawable.deepgrey_number01);*/
                nextButton.setEnabled(false);
            }
        }else if(fragment instanceof Step2PhotoFragment){

            setStepComplete(2, complete);

            if(complete){
                /*backStep2.setBackgroundResource(R.color.orange_stage);
                divStep2.setImageResource(R.drawable.edge_orage);
                step2Button.setImageResource(R.drawable.white_number02);*/
                nextButton.setEnabled(true);

                task.setTaskImages((ArrayList<Image>) data);

                /*if(pager.getCurrentItem() != 1){
                    pager.setCurrentItem(1);
                }*/

            }else{
                /*backStep2.setBackgroundResource(R.color.white);
                divStep2.setImageResource(R.drawable.edge_white);
                step2Button.setImageResource(R.drawable.deepgrey_number02);*/
                nextButton.setEnabled(false);
            }
        }else if(fragment instanceof Step3SignFragment){
            setStepComplete(3, complete);
            if(complete){
               /* backStep3.setBackgroundResource(R.color.orange_stage);
                divStep3.setImageResource(R.drawable.edge_orage);
                step3Button.setImageResource(R.drawable.white_number03);*/
                nextButton.setEnabled(true);

                task.setSignature((Signature) data);

               /* if(pager.getCurrentItem() != 2){
                    pager.setCurrentItem(2);
                }*/

            }else{
                /*backStep3.setBackgroundResource(R.color.white);
                divStep3.setImageResource(R.drawable.edge_white);
                step3Button.setImageResource(R.drawable.deepgrey_number03);*/
                nextButton.setEnabled(false);
            }
        }else if(fragment instanceof Step4ConfirmFragment){
            setStepComplete(4, complete);
            if(complete){
                //backStep4.setBackgroundResource(R.drawable.rectangle_right_round_corner_orange);
                //step4Button.setImageResource(R.drawable.white_number04);
                nextButton.setEnabled(true);

            }else{
               /* backStep4.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                step4Button.setImageResource(R.drawable.deepgrey_number04);*/
                pager.setCurrentItem((pageChangeListener.getCurrentPage() - 1));
            }
        }
    }

    @Override
    public void onConfirmSubmit(Fragment fragment, boolean complete) {
        if(complete){

            task.setComplete(complete);

            Intent intent = new Intent(getActivity(), ResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ResultActivity.KEY_TASK, task);
            bundle.putParcelable(ResultActivity.KEY_LOGIN_DATA, loginData);
            //bundle.putString("shopName", loginData.getShopName());
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
            getActivity().finish();

        }else{
            pager.setCurrentItem((pageChangeListener.getCurrentPage() - 1));
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////


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

            Step1CustomerDetailFragment step1;
            Step2PhotoFragment step2;
            Step3SignFragment step3;
            Step4ConfirmFragment step4;


            switch (position){
                case 0:
                    previousButton.setEnabled(false);

                    if(navigationControleLayout.getVisibility() == View.GONE){
                        navigationControleLayout.setVisibility(View.VISIBLE);
                    }

                    step1 = (Step1CustomerDetailFragment)adapter.getItem(position);
                    if(!step1.isDataComplete()){
                        backStep1.setBackgroundResource(R.drawable.rectangle_left_round_corner_white);
                        divStep1.setImageResource(R.drawable.edge_white);
                        step1Button.setImageResource(R.drawable.deepgrey_number01);
                    }else{
                        nextButton.setEnabled(true);
                    }

                    step2 = (Step2PhotoFragment)adapter.getItem(position + 1);
                    if(!step2.isDataComplete()){
                        backStep2.setBackgroundResource(R.color.light_gray_stage);
                        divStep2.setImageResource(R.drawable.edge_grey);
                        step2Button.setImageResource(R.drawable.lightgrey_number02);
                    }

                    break;
                case 1:
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(false);
                    if(navigationControleLayout.getVisibility() == View.GONE){
                        navigationControleLayout.setVisibility(View.VISIBLE);
                    }

                    step2 = (Step2PhotoFragment)adapter.getItem(position);
                    if(!step2.isDataComplete()){
                        backStep2.setBackgroundResource(R.color.white);
                        divStep2.setImageResource(R.drawable.edge_white);
                        step2Button.setImageResource(R.drawable.deepgrey_number02);
                    }else{
                        nextButton.setEnabled(true);
                    }

                    step3 = (Step3SignFragment)adapter.getItem(position + 1);
                    if(!step3.isDataComplete()){
                        backStep3.setBackgroundResource(R.color.light_gray_stage);
                        divStep3.setImageResource(R.drawable.edge_grey);
                        step3Button.setImageResource(R.drawable.lightgrey_number03);
                    }

                    break;
                case 2:
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(false);
                    if(navigationControleLayout.getVisibility() == View.GONE){
                        navigationControleLayout.setVisibility(View.VISIBLE);
                    }

                    step3 = (Step3SignFragment)adapter.getItem(position);
                    if(!step3.isDataComplete()){
                        backStep3.setBackgroundResource(R.color.white);
                        divStep3.setImageResource(R.drawable.edge_white);
                        step3Button.setImageResource(R.drawable.deepgrey_number03);
                    }else{
                        nextButton.setEnabled(true);
                    }

                    step4 = (Step4ConfirmFragment)adapter.getItem(position +1);
                    if(!step4.isDataComplete()){
                        backStep4.setBackgroundResource(R.drawable.rectangle_right_round_corner_gray);
                        step4Button.setImageResource(R.drawable.lightgrey_number04);
                    }

                    break;
                case 3:
                default:
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(false);

                    navigationControleLayout.setVisibility(View.GONE);

                    step4 = (Step4ConfirmFragment)adapter.getItem(position);
                    //step4.setDataComplete(true);
                    backStep4.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                    step4Button.setImageResource(R.drawable.deepgrey_number04);

                    break;
            }
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }
}
