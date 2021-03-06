package th.co.siamkubota.kubota.adapter;

/**
 * Created by atthapok on 10/06/2558.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import io.swagger.client.model.Image;
import io.swagger.client.model.Task;
import io.swagger.client.model.TaskInfo;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.fragment.Step1CustomerDetailFragment;
import th.co.siamkubota.kubota.fragment.Step2PhotoFragment;
import th.co.siamkubota.kubota.fragment.Step3SignFragment;
import th.co.siamkubota.kubota.fragment.Step4ConfirmFragment;
import th.co.siamkubota.kubota.fragment.Step4QuestionnairFragment;
import th.co.siamkubota.kubota.fragment.Step5ConfirmFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String Titles[];
    int NumbOfTabs;

    private Step1CustomerDetailFragment step1CustomerDetailFragment;
    private Step2PhotoFragment step2PhotoFragment;
    private Step3SignFragment step3SignFragment;
    private Step4QuestionnairFragment step4QuestionnairFragment;
    private Step5ConfirmFragment step5ConfirmFragment;

    private Context context;
    private Fragment listener;



    public ViewPagerAdapter(Context context, FragmentManager fm, String mTitles[],
                            int mNumbOfTabsumb, Fragment listener, Task task) {
        super(fm);

        this.context = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.listener = listener;

        step1CustomerDetailFragment = Step1CustomerDetailFragment.newInstance(task.getTaskInfo(), (!task.getComplete()));
        step2PhotoFragment = Step2PhotoFragment.newInstance( (ArrayList<Image>)task.getTaskImages(), (!task.getComplete()));
        step3SignFragment = Step3SignFragment.newInstance(task.getSignature() , (!task.getComplete()));
        step4QuestionnairFragment = Step4QuestionnairFragment.newInstance((ArrayList<Boolean>) task.getAnswers() , (!task.getComplete()));
        step5ConfirmFragment = Step5ConfirmFragment.newInstance(task.getComplete() , (!task.getComplete()));

        /*
        step1CustomerDetailFragment.setmListener((Step1CustomerDetailFragment.OnFragmentInteractionListener) listener);
        step2PhotoFragment.setmListener((Step2PhotoFragment.OnFragmentInteractionListener) listener);
        step3SignFragment.setmListener((Step3SignFragment.OnFragmentInteractionListener) listener);
        step4ConfirmFragment.setmListener((Step4ConfirmFragment.OnFragmentInteractionListener) listener);
        */

    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //((ServiceFragment) listener).setStepTitle(Titles[0]);
                return step1CustomerDetailFragment;
            case 1:
                //((ServiceFragment) listener).setStepTitle(Titles[1]);
                return step2PhotoFragment;
            case 2:
                //((ServiceFragment) listener).setStepTitle(Titles[2]);
                return step3SignFragment;
            case 3:
                //((ServiceFragment) listener).setStepTitle(Titles[3]);
                return step4QuestionnairFragment;
            case 4:
                //((ServiceFragment) listener).setStepTitle(Titles[3]);
                return step5ConfirmFragment;

        }

        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}