package th.co.siamkubota.kubota.adapter;

/**
 * Created by atthapok on 10/06/2558.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import th.co.siamkubota.kubota.fragment.PhotoPageFragment;
import th.co.siamkubota.kubota.fragment.QuestionItemFragment;
import th.co.siamkubota.kubota.fragment.SubmitQuestionFragment;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.model.Question;

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> questionItemFragments;
    private ArrayList<Question> datalist;

    private Context context;
    private Fragment listener;

    public QuestionPagerAdapter(Context context, FragmentManager fm, ArrayList<Question> photos, Fragment listener) {
        super(fm);

        this.context = context;
        this.datalist = photos;
        this.listener = listener;


        questionItemFragments = new ArrayList<Fragment>();

        for(Question item : datalist){
            QuestionItemFragment fragment = QuestionItemFragment.newInstance(item);
            //fragment.setmListener((QuestionItemFragment.OnFragmentInteractionListener) listener);
            questionItemFragments.add(fragment);
        }

        SubmitQuestionFragment submitFragment = SubmitQuestionFragment.newInstance();
        submitFragment.setmListener((SubmitQuestionFragment.OnFragmentInteractionListener) listener);

        questionItemFragments.add(submitFragment);

    }

    public ArrayList<Question> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<Question> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        return questionItemFragments.get(position);

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return datalist.get(position).getTitle();
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return questionItemFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}