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
import java.util.Date;
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
import th.co.siamkubota.kubota.model.OfflineTask;
import th.co.siamkubota.kubota.model.Question;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Converter;
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
        Step4QuestionnairFragment.OnFragmentInteractionListener,
        Step5ConfirmFragment.OnFragmentInteractionListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String KEY_TASK = "TASK";

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
    private RelativeLayout backStep1, backStep2, backStep3, backStep4, backStep5;
    private ImageButton step1Button, step2Button, step3Button, step4Button, step5Button;
    private ImageView divStep1, divStep2, divStep3, divStep4, divStep5;
    private Button saveButton;
    private Button previousButton, nextButton;
    private LinearLayout navigationControleLayout;
    private int Numboftabs;
    private CustomOnPageChangeListener pageChangeListener;

    private LoginData loginData;
    private Task task;
    private TaskDataSource dataSource;

    private boolean newTask = false;


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

        if(savedInstanceState == null){
            if (getArguments() != null) {
                loginData = getArguments().getParcelable(LoginActivity.KEY_LOGIN_DATA);
                task = getArguments().getParcelable(ServiceFragment.KEY_TASK);
            }

            mTitle = getActivity().getResources().getStringArray(R.array.stage_title);
            Numboftabs = mTitle.length;

            if(task == null){
                task = new Task(Converter.DateToString(new Date(), "yyyyMMddHHmmss"));
                task.setTaskInfo(new TaskInfo());
                task.setTaskImages(new ArrayList<Image>());
                task.setSignature(new Signature());


                if(loginData != null){
                    task.getTaskInfo().setshopID(loginData.getUserId());
                }

                newTask = true;

            }

            FragmentManager cfManager = getChildFragmentManager();
            adapter = new ViewPagerAdapter(getActivity(), cfManager, mTitle,
                    Numboftabs, ServiceFragment.this, task);
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       if(savedInstanceState == null){

       }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        task.setCurrentStep(pageChangeListener.getCurrentPage() + 1);
        saveTask(task, loginData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);

        View v = inflater.inflate(R.layout.fragment_service_5_step, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        if(savedInstanceState == null){

            rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
            textStepTitle = (TextView) v.findViewById(R.id.textStepTitle);
            backStep1 = (RelativeLayout) v.findViewById(R.id.backStep1);
            backStep2 = (RelativeLayout) v.findViewById(R.id.backStep2);
            backStep3 = (RelativeLayout) v.findViewById(R.id.backStep3);
            backStep4 = (RelativeLayout) v.findViewById(R.id.backStep4);
            backStep5 = (RelativeLayout) v.findViewById(R.id.backStep5);
            step1Button = (ImageButton) v.findViewById(R.id.step1Button);
            step2Button = (ImageButton) v.findViewById(R.id.step2Button);
            step3Button = (ImageButton) v.findViewById(R.id.step3Button);
            step4Button = (ImageButton) v.findViewById(R.id.step4Button);
            step5Button = (ImageButton) v.findViewById(R.id.step5Button);
            divStep1 = (ImageView) v.findViewById(R.id.divStep1);
            divStep2 = (ImageView) v.findViewById(R.id.divStep2);
            divStep3 = (ImageView) v.findViewById(R.id.divStep3);
            divStep4 = (ImageView) v.findViewById(R.id.divStep4);
            pager = (NoneScrollableViewPager) v.findViewById(R.id.pager);

            saveButton = (Button) v.findViewById(R.id.saveButton);
            previousButton = (Button) v.findViewById(R.id.previousButton);
            nextButton = (Button) v.findViewById(R.id.nextButton);
            navigationControleLayout = (LinearLayout) v.findViewById(R.id.navigationControleLayout);


            saveButton.setOnClickListener(this);
            previousButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            nextButton.setEnabled(false);

            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(5);
            pager.addOnPageChangeListener(pageChangeListener = new CustomOnPageChangeListener());
            pager.setPagingEnabled(false);

            Ui.setupUI(getActivity(), rootLayout);

            if(task.getComplete() != null && task.getComplete()){
                setStepComplete(1, true);
                setStepComplete(2, true);
                setStepComplete(3, true);
                setStepComplete(4, true);
                setStepComplete(5, true);
                pager.setCurrentItem(adapter.getCount()-1);

                //saveButton.setVisibility(View.GONE);

            }else{

                pager.setCurrentItem(task.getCurrentStep() -1);

            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        task.setCurrentStep(pageChangeListener.getCurrentPage() + 1);
        saveTask(task, loginData);
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

                    if(pageChangeListener.getCurrentPage() == 1){
                        backStep2.setBackgroundResource(R.color.white);
                        divStep2.setImageResource(R.drawable.edge_white);
                        step2Button.setImageResource(R.drawable.deepgrey_number02);
                    }else{
                        backStep2.setBackgroundResource(R.color.light_gray_stage);
                        divStep2.setImageResource(R.drawable.edge_grey);
                        step2Button.setImageResource(R.drawable.lightgrey_number02);
                    }

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

                if(complete){
                    backStep4.setBackgroundResource(R.color.orange_stage);
                    divStep4.setImageResource(R.drawable.edge_orage);
                    step4Button.setImageResource(R.drawable.white_number04);

                }else{

                    if(pageChangeListener.getCurrentPage() == 3){
                        backStep4.setBackgroundResource(R.color.white);
                        divStep4.setImageResource(R.drawable.edge_white);
                        step4Button.setImageResource(R.drawable.deepgrey_number04);
                    }else{
                        backStep4.setBackgroundResource(R.color.light_gray_stage);
                        divStep4.setImageResource(R.drawable.edge_grey);
                        step4Button.setImageResource(R.drawable.lightgrey_number04);
                    }


                }

                break;
            case 5:
            default:

                if(complete){
                    backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_orange);
                    step5Button.setImageResource(R.drawable.white_number05);
                }else{
//                    backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
//                    step5Button.setImageResource(R.drawable.deepgrey_number05);

                    if(pageChangeListener.getCurrentPage() == 4){
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                        step5Button.setImageResource(R.drawable.deepgrey_number05);
                    }else{
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_gray);
                        step5Button.setImageResource(R.drawable.lightgrey_number05);
                    }
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
        public void onSaveTask(OfflineTask task);
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
        }else if(v == step5Button){
            pager.setCurrentItem(4, true);
        }else if(v == previousButton){
            int currentPage = pageChangeListener.getCurrentPage();
            --currentPage;
            pager.setCurrentItem(currentPage = (currentPage >= 0 ? currentPage : 0));
        }else if(v == nextButton){
            int currentPage = pageChangeListener.getCurrentPage();
            ++currentPage;
            pager.setCurrentItem(currentPage = (currentPage < adapter.getCount() ? currentPage : (adapter.getCount() - 1)));
        }else if(v == saveButton){

            OfflineTask offlineTask = new OfflineTask();
            task.setCurrentStep(pageChangeListener.getCurrentPage()+1);
            offlineTask.setTask(task);
            offlineTask.setLoginData(loginData);
            mListener.onSaveTask(offlineTask);
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

       /* if(complete){
            showToastComplete();
        }*/

        if(fragment instanceof Step1CustomerDetailFragment){

            setStepComplete(1, complete);

            task.setTaskInfo((TaskInfo) data);

            if(complete){

                //nextButton.setEnabled(true);

                Step2PhotoFragment step2PhotoFragmentadapter = (Step2PhotoFragment)adapter.getItem(1);
                step2PhotoFragmentadapter.setMachineNumber(task.getTaskInfo().getEngineNo());

                Step3SignFragment step3SignFragment = (Step3SignFragment)adapter.getItem(2);
                step3SignFragment.setCustomerName(task.getTaskInfo().getCustomerName());

                if(pageChangeListener.getCurrentPage() == 0){
                    showToastComplete();
                }

            }else{
                //nextButton.setEnabled(false);
            }

            Fragment currentfragment = adapter.getItem(pageChangeListener.getCurrentPage());
            if(fragment == currentfragment){
                nextButton.setEnabled(((Step1CustomerDetailFragment) fragment).isDataComplete());
            }
        }else if(fragment instanceof Step2PhotoFragment){

            setStepComplete(2, complete);
            task.setTaskImages((ArrayList<Image>) data);

            Fragment currentfragment = adapter.getItem(pageChangeListener.getCurrentPage());
            if(fragment == currentfragment){
                nextButton.setEnabled(((Step2PhotoFragment) fragment).isDataComplete());
            }

            if(complete){
                if(pageChangeListener.getCurrentPage() == 1){
                    showToastComplete();
                }
            }

        }else if(fragment instanceof Step3SignFragment){
            setStepComplete(3, complete);
            task.setSignature((Signature) data);



            Fragment currentfragment = adapter.getItem(pageChangeListener.getCurrentPage());
            if(fragment == currentfragment){
                nextButton.setEnabled(((Step3SignFragment) fragment).isDataComplete());
            }

            if(complete){
                if(pageChangeListener.getCurrentPage() == 2){
                    showToastComplete();
                }
            }

        }else if(fragment instanceof Step4QuestionnairFragment){
            setStepComplete(4, complete);
            //task.setSignature((Signature) data);



            Fragment currentfragment = adapter.getItem(pageChangeListener.getCurrentPage());
            if(fragment == currentfragment){
                nextButton.setEnabled(((Step4QuestionnairFragment) fragment).isDataComplete());
            }

            if(complete){
                if(pageChangeListener.getCurrentPage() == 3){
                    showToastComplete();
                }
            }

            ArrayList<Question> questions = (ArrayList<Question>) data;

            if(questions != null && questions.size() > 0){

                ArrayList<Boolean> answers = new ArrayList<Boolean>();
                for (Question q : questions){

                    if(q.isComplete()){
                        answers.add(q.isAnswer());
                    }else{
                        answers.add(null);
                    }
                }

                task.setAnswers(answers);
            }

        }else if(fragment instanceof Step5ConfirmFragment){
            setStepComplete(5, complete);
            if(complete){
                //nextButton.setEnabled(true);
                if(pageChangeListener.getCurrentPage() == 4){
                    showToastComplete();
                }
                //pager.setCurrentItem((pageChangeListener.getCurrentPage() - 1));
            }else{
                //pager.setCurrentItem((pageChangeListener.getCurrentPage() - 1));
            }

            Fragment currentfragment = adapter.getItem(pageChangeListener.getCurrentPage());
            if(fragment == currentfragment){
                nextButton.setEnabled(((Step5ConfirmFragment) fragment).isDataComplete());
            }
        }

    }

    @Override
    public void onConfirmSubmit(Fragment fragment, boolean complete) {
        if(complete){

            String from = "";
            if(task.getComplete()){
                from = getActivity().getClass().getSimpleName();
            }

            task.setComplete(complete);
            task.setCurrentStep(pageChangeListener.getCurrentPage() + 1);


            Intent intent = new Intent(getActivity(), ResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ResultActivity.KEY_TASK, task);
            bundle.putParcelable(ResultActivity.KEY_LOGIN_DATA, loginData);

            if(!from.isEmpty()){
                bundle.putString(ResultActivity.KEY_FROM, ResultActivity.class.getSimpleName());
            }

            //bundle.putString("shopName", loginData.getShopName());
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
            getActivity().finish();

        }else{
            pager.setCurrentItem((pageChangeListener.getCurrentPage() - 1));
        }
    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment) {
        task.setCurrentStep(pageChangeListener.getCurrentPage() + 1);

        saveTask(task, loginData);
    }

    @Override
    public void onCustomerNameChange(String name) {
        ((Step3SignFragment)adapter.getItem(2)).setCustomerName(name);
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
            Step4QuestionnairFragment step4;
            Step5ConfirmFragment step5;


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

                    step4 = (Step4QuestionnairFragment) adapter.getItem(position +1);
                    if(!step4.isDataComplete()){
                        backStep4.setBackgroundResource(R.color.light_gray_stage);
                        divStep4.setImageResource(R.drawable.edge_grey);
                        step4Button.setImageResource(R.drawable.lightgrey_number04);
                    }

                    break;
                case 3:
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(false);
                    if(navigationControleLayout.getVisibility() == View.GONE){
                        navigationControleLayout.setVisibility(View.VISIBLE);
                    }

                    step4 = (Step4QuestionnairFragment) adapter.getItem(position);
                    if(!step4.isDataComplete()){
                        backStep4.setBackgroundResource(R.color.white);
                        divStep4.setImageResource(R.drawable.edge_white);
                        step4Button.setImageResource(R.drawable.deepgrey_number04);
                    }else{
                        nextButton.setEnabled(true);
                    }

                    step5 = (Step5ConfirmFragment) adapter.getItem(position +1);
                    if(!step5.isDataComplete()){
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_gray);
                        step5Button.setImageResource(R.drawable.lightgrey_number05);
                    }else {
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_orange);
                        step5Button.setImageResource(R.drawable.white_number05);
                    }

                    break;
                case 4:
                default:
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(false);

                    navigationControleLayout.setVisibility(View.GONE);

                    step5 = (Step5ConfirmFragment)adapter.getItem(position);
                    //step4.setDataComplete(true);
                    //backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                    //step5Button.setImageResource(R.drawable.deepgrey_number05);

                    if(!step5.isDataComplete()){
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_white);
                        step5Button.setImageResource(R.drawable.deepgrey_number05);
                    }else{
                        backStep5.setBackgroundResource(R.drawable.rectangle_right_round_corner_orange);
                        step5Button.setImageResource(R.drawable.white_number05);
                    }


                    break;
            }
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    private void saveTask(Task task, LoginData loginData){

        dataSource = new TaskDataSource(getActivity());
        dataSource.open();
        dataSource.addIncompleteTask(task, loginData);

    }

    public void deleteTask(){

        if(task != null){
            dataSource = new TaskDataSource(getActivity());
            dataSource.open();

            dataSource.deleteTask(task.getTaskId());
        }

    }
}
