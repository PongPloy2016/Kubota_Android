package th.co.siamkubota.kubota.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.ContinueQuestionFragment;
import th.co.siamkubota.kubota.fragment.FinishDialogFragment;
import th.co.siamkubota.kubota.fragment.QuestionnairFragment;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.fragment.UnfinishTaskFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QuestionnairActivity extends BaseActivity implements
ContinueQuestionFragment.OnFragmentInteractionListener{

    private AppController app;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ContinueQuestionFragment newFragment = ContinueQuestionFragment.newInstance();
        newFragment.setmListener(this);
        ft.replace(R.id.content, newFragment, "continueQuestionFragment");
        ft.commit();

    }

    ///////////////////////////////////////////////////////////////////// implement method


    @Override
    public void onConfirmContinue(boolean result) {

        if(result){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft = getSupportFragmentManager().beginTransaction();
            //ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            QuestionnairFragment fragment = QuestionnairFragment.newInstance();
            ft.replace(R.id.content, fragment, "questionnairFragment");
            //ft.addToBackStack(null);
            // Start the animated transition.
            ft.commit();

        }else{
            // alert finish dialog

            FinishDialogFragment alert = new FinishDialogFragment();
            alert.setmListener(new FinishDialogFragment.onActionListener() {
                @Override
                public void onFinishDialog() {
                    finish();
                }
            });

            alert.show(getSupportFragmentManager(), "finish");
        }
    }
}
