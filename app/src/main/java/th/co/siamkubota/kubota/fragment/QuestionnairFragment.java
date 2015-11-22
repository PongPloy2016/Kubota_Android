package th.co.siamkubota.kubota.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import java.util.ArrayList;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.QuestionPagerAdapter;
import th.co.siamkubota.kubota.adapter.ViewPagerAdapter;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.model.Question;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionnairFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionnairFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionnairFragment extends Fragment implements
        View.OnClickListener,
SubmitQuestionFragment.OnFragmentInteractionListener{


    private OnFragmentInteractionListener mListener;
    private AppController app;
    private ViewPager pager;
    private QuestionPagerAdapter adapter;
    private CustomOnPageChangeListener pageChangeListener;

    private ArrayList<Question> datalist;


    public static QuestionnairFragment newInstance() {
        QuestionnairFragment fragment = new QuestionnairFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionnairFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v = View.inflate(getActivity(), R.layout.tab_product, null);
        View v = inflater.inflate(R.layout.fragment_questionnair, container, false);

        pager = (ViewPager) v.findViewById(R.id.pager);

        datalist = new ArrayList<Question>();

        datalist.add(new Question("แบบสอบถามที่ 1"));
        datalist.add(new Question("แบบสอบถามที่ 2"));
        datalist.add(new Question("แบบสอบถามที่ 3"));


        adapter = new QuestionPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), datalist, QuestionnairFragment.this);
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


    ///////////////////////////////////////////////////////////////////// implement method


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConfirmSubmitQuestion(boolean result) {
        if(result){
            // alert finish dialog

            FinishDialogFragment alert = new FinishDialogFragment();
            alert.setmListener(new FinishDialogFragment.onActionListener() {
                @Override
                public void onFinishDialog() {
                    getActivity().finish();
                }
            });

            alert.show(getActivity().getSupportFragmentManager(), "finish");

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

        }



        public final int getCurrentPage() {
            return currentPage;
        }
    }
}
