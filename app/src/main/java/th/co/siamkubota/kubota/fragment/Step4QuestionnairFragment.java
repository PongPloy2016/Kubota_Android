package th.co.siamkubota.kubota.fragment;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Image;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.ImageViewActivity;
import th.co.siamkubota.kubota.adapter.PhotoPagerAdapter;
import th.co.siamkubota.kubota.adapter.QuestionPagerAdapter;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.model.Question;
import th.co.siamkubota.kubota.utils.function.Copier;
import th.co.siamkubota.kubota.utils.function.ImageFile;
import th.co.siamkubota.kubota.utils.ui.NoneScrollableViewPager;

/**
 * Created by sipangka on 02/07/2559.
 */
public class Step4QuestionnairFragment extends Fragment implements
        View.OnClickListener,
        QuestionItemFragment.OnFragmentInteractionListener{

    private static final String KEY_TITLE = "title";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_EDITABLED = "editabled";

    private NoneScrollableViewPager pager;
    private Button button1, button2, button3;
    private ImageButton previousButton, nextButton;
    //private PhotoPagerAdapter adapter;
    private QuestionPagerAdapter adapter;
    //private PhotoViewPagerAdapter adapter;

    private CustomOnPageChangeListener pageChangeListener;

    private OnFragmentInteractionListener mListener;


    private ArrayList<Question> datalist;


    private String[] questions;
    private ArrayList<Boolean> answers;

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



    //////////////////////////////////////////////////////////////////// constructor

    public static Step4QuestionnairFragment newInstance(ArrayList<Boolean> answers, boolean editabled) {
        Step4QuestionnairFragment fragment = new Step4QuestionnairFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_TITLE, title);

        if(answers != null && answers.size() > 0){
            boolean[] booArr = new boolean[answers.size()];

            int i = 0;
            for(Boolean  b : answers){
                if(b != null){
                    booArr[i] = b;
                }
                i++;
            }
            args.putBooleanArray(KEY_ANSWERS, booArr);
        }

        args.putBoolean(KEY_EDITABLED, editabled);
        fragment.setArguments(args);
        return fragment;
    }

    public Step4QuestionnairFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(KEY_IMAGES, images);
        //mListener.onFragmentSaveInstanceState(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            //images = savedInstanceState.getParcelableArrayList(KEY_IMAGES);

        }
        mListener = (Step4QuestionnairFragment.OnFragmentInteractionListener) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questions = getActivity().getResources().getStringArray(R.array.questions);

        boolean[] booArr = null;

        if(savedInstanceState == null ){

            if (getArguments() != null) {

                editabled =  getArguments().getBoolean(KEY_EDITABLED);

                 booArr = getArguments().getBooleanArray(KEY_ANSWERS);

                answers = new ArrayList<Boolean>();
                if(booArr != null && booArr.length > 0){

                    for(boolean b : booArr){
                        answers.add(new Boolean(b));
                    }
                }
            }

            datalist = new ArrayList<Question>();

            for(int i = 0 ; i < 3 ; i++){
                Question q = new Question(i ,"แบบสอบถามที่ " + (i+1));
                q.setDetail(questions[i]);


                if(answers != null && answers.size() >= i + 1 && answers.get(i) != null){
                    q.setAnswer(answers.get(i));
                    q.setComplete(true);
                }else {
                    q.setComplete(false);
                }

                datalist.add(q);
            }



            if(Config.showDefault){

                for(Question q : datalist){
                    q.setAnswer(true);
                    q.setComplete(true);
                }

                dataComplete = true;
                setDataComplete(dataComplete);

            }else{
                setDataComplete(!editabled);
            }


            //validateInput();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_step4_questionnair, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        button1 = (Button) v.findViewById(R.id.button1);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        pager = (NoneScrollableViewPager) v.findViewById(R.id.pager);

        previousButton = (ImageButton) v.findViewById(R.id.previousButton);
        nextButton = (ImageButton) v.findViewById(R.id.nextButton);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);



        mListener = (Step4QuestionnairFragment.OnFragmentInteractionListener) getParentFragment();

        FragmentManager cfManager = getChildFragmentManager();
        adapter = new QuestionPagerAdapter(getActivity(), cfManager, datalist, Step4QuestionnairFragment.this, editabled);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(pageChangeListener = new CustomOnPageChangeListener());
        pager.setOffscreenPageLimit(3);
        pager.setPagingEnabled(false);

        int i = 0;
        for (Question q : datalist){

            if(!q.isComplete()){
                pager.setCurrentItem(i);
                break;
            }

            i++;
        }

        if(i == 3){
            dataComplete = true;
            pager.setCurrentItem(0);
        }else{
            dataComplete = false;
        }


        if(!dataComplete){
            previousButton.setEnabled(false);
            nextButton.setEnabled(false);

            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);

            pager.setPagingEnabled(false);
        }else {
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);

            button1.setEnabled(true);
            button2.setEnabled(true);
            button3.setEnabled(true);

            pager.setPagingEnabled(true);
        }

        validateInput();


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
            }
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    private void validateInput(){

        for(Question q : datalist){
            if(!q.isComplete()){
                setDataComplete(false);
                dataComplete = false;
                mListener.onFragmentDataComplete(this, dataComplete, datalist);
                return;
            }
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete, datalist);

    }

    @Override
    public void onChoiceSelected(Fragment fragment, Question question) {

        //datalist.get(question.getId())

        Copier.copy(question, datalist.get(question.getId()));
        validateInput();

        if(question.getId() == 0){
            button1.setEnabled(true);
            //previousButton.setEnabled(true);
        }else if(question.getId() == 1){
            button2.setEnabled(true);
        }else if(question.getId() == 2){
            button3.setEnabled(true);
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);
            pager.setPagingEnabled(true);
        }

        if(!dataComplete){
            int nextpage = (pageChangeListener.getCurrentPage() + 1);
            if(nextpage < 3){
                pager.setCurrentItem(pageChangeListener.getCurrentPage() + 1);
            }
        }else {
            pager.setPagingEnabled(true);
        }

    }
}
